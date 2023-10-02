package com.yuroyami.syncplay.protocol

import com.yuroyami.syncplay.models.MediaFile
import com.yuroyami.syncplay.models.User
import com.yuroyami.syncplay.protocol.json.ReceivedJson
import com.yuroyami.syncplay.utils.generateTimestampMillis
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.double
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/** Handlers that parse JSONs and control callbacks based on the incoming message from server */
object JsonHandler {

    fun SyncplayProtocol.handleJson(json: String) {
        val protocol = this
        //TODO: loggy("Server: $json")

        /* Second, we check what kind of JSON message we received from the first Json Object */
        val element: ReceivedJson?
        try {
            element = Json.decodeFromString<ReceivedJson>(json)
        } catch (e: Exception) {
            return
        }
        when  {
            element.hello != null -> handleHello(element.hello, protocol)
            element.set != null -> handleSet(element.set, protocol)
            element.list != null -> handleList(element.list, protocol)
            element.state != null -> handleState(element.state, protocol, json)
            element.chat != null -> handleChat(element.chat, protocol)
            element.error != null -> handleError(element.error, protocol)
            element.tls != null -> handleTLS(element.tls, protocol)
            else -> dropError("unknown-command-server-error")
        }

    }

    private fun handleHello(hello: JsonObject, p: SyncplayProtocol) {
        hello["username"]?.jsonPrimitive?.content?.let { usrnm ->
            p.session.currentUsername = usrnm
        }
        /* Corrected Username */
        p.sendPacket(JsonSender.sendJoined(p.session.currentRoom))
        p.sendPacket(JsonSender.sendEmptyList())
        //p.sendPacket(JsonSender.sendReadiness(p.ready, false))
        p.syncplayCallback?.onConnected()
    }

    private fun handleSet(set: JsonObject, p: SyncplayProtocol) {
        /* Sets can carry a variety of commands: "ready", "user left/joined", shared playlist commands etc */
        val command = when {
            set.containsKey("user") -> "user"
            set.containsKey("room") -> "room"
            set.containsKey("controllerAuth") -> "controllerAuth"
            set.containsKey("newControlledRoom") -> "newControlledRoom"
            set.containsKey("ready") -> "ready"
            set.containsKey("playlistIndex") -> "playlistIndex"
            set.containsKey("playlistChange") -> "playlistChange"
            set.containsKey("features") -> "features"
            else -> ""
        }

        when (command) {
            "user" -> {
                /* a "user" command can mean a user joined, left, or added a file. **/
                val user = set["user"]?.jsonObject?.keys?.toList()?.get(0) ?: return
                val content = set["user"]?.jsonObject?.get(user)?.jsonObject ?: return

                /* Checking if there is an event in question */
                if (content.containsKey("event")) {
                    val event = content["event"]?.jsonObject ?: return
                    if (event.containsKey("left")) {
                        p.syncplayCallback?.onSomeoneLeft(user)
                    }
                    if (event.containsKey("joined")) {
                        p.syncplayCallback?.onSomeoneJoined(user)
                    }
                }

                /* Checking if there is a file addition in question */
                if (content.containsKey("file")) {
                    val file = content["file"]?.jsonObject ?: return
                    p.syncplayCallback?.onSomeoneLoadedFile(
                        user,
                        file["name"]?.jsonPrimitive?.content ?: "",
                        file["duration"]?.jsonPrimitive?.double ?: 0.00,
                        //file.get("size").asString
                    )
                }
            }

            "playlistIndex" -> {
                val playlistIndex = set["playlistIndex"]?.jsonObject ?: return
                val userMeant = playlistIndex["user"]
                val newIndex = playlistIndex["index"]
                if (!userMeant.isNull() && !newIndex.isNull()) {
                    p.syncplayCallback?.onPlaylistIndexChanged(
                        userMeant!!.jsonPrimitive.content,
                        newIndex!!.jsonPrimitive.int
                    )
                    p.session.sharedPlaylistIndex = newIndex!!.jsonPrimitive.int
                } else return
            }

            "playlistChange" -> {
                val playlistChange = set["playlistChange"]?.jsonObject ?: return
                val userMeant =
                    if (playlistChange["user"].isNull()) "" else playlistChange["user"]!!.jsonPrimitive.content
                val fileArray = playlistChange["files"]?.jsonArray ?: return
                p.session.sharedPlaylist.clear()
                for (file in fileArray) {
                    p.session.sharedPlaylist.add(file.jsonPrimitive.content)
                }
//                if (userMeant != "")
                p.syncplayCallback?.onPlaylistUpdated(userMeant)
            }
        }

        /* Fetching a list of users anyway (their readiness, files..etc) */
        p.sendPacket(JsonSender.sendEmptyList())
    }

    private fun handleList(list: JsonObject, p: SyncplayProtocol) {
        val userlist = list[p.session.currentRoom]?.jsonObject ?: return
        val userkeys = userlist.keys /* Getting user list */
        p.session.userList.clear() /* Clearing the list before populating it again */

        var indexer = 1 /* This indexer is used to put the main user ahead of others in the list */
        for (user in userkeys) {
            val utilisateur = User(
                name = user,
                index = run {
                    var userindex = 0
                    if (user != p.session.currentUsername) {
                        userindex = indexer
                        indexer += 1
                    }
                    userindex
                },
                readiness = if (userlist[user]?.jsonObject?.get("isReady").isNull()) {
                    false
                } else {
                    userlist[user]?.jsonObject?.get("isReady")!!.jsonPrimitive.boolean
                },
                file = run {
                    val file = userlist[user]?.jsonObject?.get("file")?.jsonObject ?: return

                    var mediaFile: MediaFile? = MediaFile()

                    if (file.keys.contains("name")) {
                        mediaFile?.fileName = file["name"]?.jsonPrimitive?.content ?: ""
                        mediaFile?.fileDuration = file["duration"]?.jsonPrimitive?.double ?: 0.00
                        mediaFile?.fileSize = file["size"]?.jsonPrimitive?.content ?: ""
                    } else {
                        mediaFile = null
                    }

                    mediaFile
                }
            )

            p.session.userList.add(utilisateur)
        }
        p.syncplayCallback?.onReceivedList()
    }

    private fun handleState(state: JsonObject, protocol: SyncplayProtocol, json: String) {

        val clienttime = (generateTimestampMillis() / 1000.0)
        val latency = state["ping"]?.jsonObject?.get("latencyCalculation")?.jsonPrimitive?.double

        if (state.containsKey("ignoringOnTheFly")) {
            if ((json.contains("server\":"))) {
                val playstate = state["playstate"]?.jsonObject ?: return
                val doSeekElement = playstate["doSeek"]
                val doSeek: Boolean? =
                    if (doSeekElement.isNull()) null else doSeekElement!!.jsonPrimitive.boolean
                val seekedPosition = playstate["position"]!!.jsonPrimitive.double
                val seeker: String =
                    if (playstate["setBy"].isNull()) "" else playstate["setBy"]!!.jsonPrimitive.content
                if (doSeek == true) {
                    protocol.syncplayCallback?.onSomeoneSeeked(seeker, seekedPosition)
                }
                if (doSeek == false || doSeek == null) {
                    protocol.paused = json.contains("\"paused\": true", true)
                    if (!protocol.paused) {
                        protocol.syncplayCallback?.onSomeonePlayed(seeker)
                    } else {
                        protocol.syncplayCallback?.onSomeonePaused(seeker)
                    }
                }

                protocol.serverIgnFly = state["ignoringOnTheFly"]?.jsonObject?.get("server")?.jsonPrimitive?.int ?: 0
                protocol.clientIgnFly = 0

                if (!(json.contains("client\":"))) {
                    protocol.sendPacket(
                        JsonSender.sendState(
                            latency,
                            clienttime,
                            null,
                            0,
                            iChangeState = 0,
                            play = null,
                            protocol = protocol
                        )
                    )
                } else {
                    protocol.sendPacket(
                        JsonSender.sendState(
                            latency,
                            clienttime,
                            doSeek,
                            0,
                            0,
                            null,
                            protocol
                        )
                    )
                }
            }
        } else {
            val playstate = state["playstate"]?.jsonObject
            val position = playstate?.get("position")?.jsonPrimitive?.double
            val positionOf: String? =
                if ((playstate?.get("setBy") as? JsonNull) == null) {
                    playstate?.get("setBy")?.jsonPrimitive?.content
                } else ""

            /* Rewind check if someone is behind */
            if (positionOf != (protocol.session.currentUsername) && positionOf != "") {
                if (position != null && positionOf != null) {
                    if (position < (protocol.currentVideoPosition - protocol.rewindThreshold)) {
                        protocol.syncplayCallback?.onSomeoneBehind(positionOf, position)
                    }
                }
            }

            /* Constant Traditional Pinging if no command is received. */
            protocol.sendPacket(
                JsonSender.sendState(
                    latency,
                    clienttime,
                    false,
                    0,
                    0,
                    null,
                    protocol
                )
            )
        }

    }

    private fun handleChat(chat: JsonObject, p: SyncplayProtocol) {
        chat["username"]?.jsonPrimitive?.content?.let { sender ->
            chat["message"]?.jsonPrimitive?.content?.let { msg ->
                p.syncplayCallback?.onChatReceived(sender, msg)
            }
        }
    }

    private fun handleTLS(tls: JsonObject, p: SyncplayProtocol) {
        tls["startTLS"]?.jsonPrimitive?.boolean?.let {
            p.syncplayCallback?.onReceivedTLS(it)
        }
    }

    /** The following functions are not made yet cuz I see them almost unnecessary ATM */
    private fun handleError(error: JsonObject, p: SyncplayProtocol) {
    }


    private fun dropError(error: String) {
    }

    /** Means that this value is null in one of two ways:
     * Conforms to JsonNull type, or just null (doesn't exist) */
    private fun JsonElement?.isNull(): Boolean {
        return this == null || (this as? JsonNull != null)
    }
}