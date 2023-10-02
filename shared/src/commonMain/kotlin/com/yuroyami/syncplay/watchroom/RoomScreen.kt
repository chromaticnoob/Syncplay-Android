package com.yuroyami.syncplay.watchroom

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NoEncryption
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VideoSettings
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.yuroyami.syncplay.MR
import com.yuroyami.syncplay.compose.CardRoomPrefs.InRoomSettingsCard
import com.yuroyami.syncplay.compose.CardUserInfo.UserInfoCard
import com.yuroyami.syncplay.compose.ComposeUtils
import com.yuroyami.syncplay.compose.ComposeUtils.FancyIcon2
import com.yuroyami.syncplay.compose.ComposeUtils.FlexibleFancyAnnotatedText
import com.yuroyami.syncplay.compose.ComposeUtils.gradientOverlay
import com.yuroyami.syncplay.datastore.DataStoreKeys.DATASTORE_INROOM_PREFERENCES
import com.yuroyami.syncplay.datastore.DataStoreKeys.DATASTORE_MISC_PREFS
import com.yuroyami.syncplay.datastore.DataStoreKeys.MISC_NIGHTMODE
import com.yuroyami.syncplay.datastore.DataStoreKeys.PREF_INROOM_MSG_BG_OPACITY
import com.yuroyami.syncplay.datastore.DataStoreKeys.PREF_INROOM_MSG_BOX_ACTION
import com.yuroyami.syncplay.datastore.DataStoreKeys.PREF_INROOM_MSG_FONTSIZE
import com.yuroyami.syncplay.datastore.DataStoreKeys.PREF_INROOM_MSG_MAXCOUNT
import com.yuroyami.syncplay.datastore.DataStoreKeys.PREF_INROOM_MSG_OUTLINE
import com.yuroyami.syncplay.datastore.DataStoreKeys.PREF_INROOM_PLAYER_SEEK_BACKWARD_JUMP
import com.yuroyami.syncplay.datastore.DataStoreKeys.PREF_INROOM_PLAYER_SEEK_FORWARD_JUMP
import com.yuroyami.syncplay.datastore.booleanFlow
import com.yuroyami.syncplay.datastore.ds
import com.yuroyami.syncplay.datastore.intFlow
import com.yuroyami.syncplay.datastore.obtainBoolean
import com.yuroyami.syncplay.datastore.writeBoolean
import com.yuroyami.syncplay.protocol.JsonSender
import com.yuroyami.syncplay.ui.AppTheme
import com.yuroyami.syncplay.ui.Paletting
import com.yuroyami.syncplay.ui.Paletting.ROOM_ICON_SIZE
import com.yuroyami.syncplay.utils.timeStamper
import com.yuroyami.syncplay.watchroom.RoomComposables.AddVideoButton
import com.yuroyami.syncplay.watchroom.RoomComposables.ComposedMessagePalette
import com.yuroyami.syncplay.watchroom.RoomComposables.FreeAnimatedVisibility
import com.yuroyami.syncplay.watchroom.RoomComposables.RoomArtwork
import com.yuroyami.syncplay.watchroom.RoomComposables.RoomTab
import com.yuroyami.syncplay.watchroom.RoomComposables.fadingMessageLayout
import dev.icerock.moko.resources.compose.asFont
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt

/** TODO: Ship these to a platform-agnostic viewmodel */
val hasVideoG = mutableStateOf(false)
val hudVisibilityState = mutableStateOf(true)
val pipMode = mutableStateOf(false)

var setReadyDirectly = false
val seeks = mutableListOf<Pair<Long, Long>>()

var startupSlide = false

/* Related to playback status */
val isNowPlaying = mutableStateOf(false)
val timeFull = mutableLongStateOf(0L)
val timeCurrent = mutableLongStateOf(0L)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomUI(isSoloMode: Boolean) {
    val nightMode = DATASTORE_MISC_PREFS.ds().booleanFlow(MISC_NIGHTMODE, true).collectAsState(initial = true)

    val directive = MR.fonts.Directive4.regular.asFont()!!
    val inter = MR.fonts.Inter.regular.asFont()!!

    AppTheme(nightMode.value) {
        val generalScope = rememberCoroutineScope()
        val focusManager = LocalFocusManager.current
        val density = LocalDensity.current
        var screenHeightDp by remember { mutableStateOf(0f) }
        var screenWidthDp by remember { mutableStateOf(0f) }
        val screenHeightPx = with(density) { screenHeightDp.dp.roundToPx() }
        val screenWidthPx = with(density) { screenWidthDp.dp.roundToPx() }

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            if (screenHeightDp == 0f) screenHeightDp = this.maxHeight.value
            if (screenWidthDp == 0f) screenWidthDp = this.maxWidth.value
        }

        val hasVideo = remember { hasVideoG }
        val hudVisibility = remember { hudVisibilityState }
        var pipModeObserver by remember { pipMode }
        val locked = remember { mutableStateOf(false) }

        val addurlpopupstate = remember { mutableStateOf(false) }
        val chathistorypopupstate = remember { mutableStateOf(false) }
        val seektopopupstate = remember { mutableStateOf(false) }


        val msgPalette = ComposedMessagePalette()

        val msgBoxOpacity = DATASTORE_INROOM_PREFERENCES.ds().intFlow(PREF_INROOM_MSG_BG_OPACITY, 0).collectAsState(initial = 0)
        val msgOutline = DATASTORE_INROOM_PREFERENCES.ds().booleanFlow(PREF_INROOM_MSG_OUTLINE, true).collectAsState(initial = true)
        val msgFontSize = DATASTORE_INROOM_PREFERENCES.ds().intFlow(PREF_INROOM_MSG_FONTSIZE, 9).collectAsState(initial = 9)
        val msgMaxCount by DATASTORE_INROOM_PREFERENCES.ds().intFlow(PREF_INROOM_MSG_MAXCOUNT, 10).collectAsState(initial = 0)
        val keyboardOkFunction by DATASTORE_INROOM_PREFERENCES.ds().booleanFlow(PREF_INROOM_MSG_BOX_ACTION, true).collectAsState(initial = true)
        val seekIncrement = DATASTORE_INROOM_PREFERENCES.ds().intFlow(PREF_INROOM_PLAYER_SEEK_FORWARD_JUMP, 10).collectAsState(initial = 10)
        val seekDecrement = DATASTORE_INROOM_PREFERENCES.ds().intFlow(PREF_INROOM_PLAYER_SEEK_BACKWARD_JUMP, 10).collectAsState(initial = 10)

        /** Room artwork underlay (when no video is loaded) */
        if (!hasVideo.value) {
            RoomArtwork(pipModeObserver)
        }

        /** video surface */
        val seekLeftInteraction: MutableInteractionSource = remember { MutableInteractionSource() }
        val seekRightInteraction: MutableInteractionSource = remember { MutableInteractionSource() }
        LaunchedEffect(hasVideo.value) {
            if (hasVideo.value) {
                //TODO: unalphizePlayer(engine.value)
            }
        }

        /** Lock layout, This is what appears when the user locks the screen */
        val unlockButtonVisibility = remember { mutableStateOf(false) }

        if (locked.value) {
            /** The touch interceptor to switch unlock button visibility */
            Box(
                Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null, onClick = {
                            unlockButtonVisibility.value = !unlockButtonVisibility.value
                        }
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 15.dp, end = 44.dp),
                horizontalAlignment = Alignment.End
            ) {
                /** Unlock Card */
                if (unlockButtonVisibility.value && !pipModeObserver) {
                    Card(
                        modifier = Modifier.width(48.dp).alpha(0.5f).aspectRatio(1f)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(color = Paletting.SP_ORANGE)
                            ) {
                                locked.value = false
                            },
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(width = 1.dp, brush = Brush.linearGradient(colors = Paletting.SP_GRADIENT)),
                        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Icon(
                                imageVector = Icons.Filled.NoEncryption,
                                contentDescription = "",
                                modifier = Modifier.size(32.dp).align(Alignment.Center).gradientOverlay()
                            )
                        }
                    }
                }
            }
        } else {
            /** ACTUAL ROOM HUD AND LAYOUT */

            val msg = remember { mutableStateOf("") }
            val msgCanSend = remember { mutableStateOf(false) }
            val msgs = if (!isSoloMode) remember { p.session.messageSequence } else remember { mutableStateListOf() }
            val ready = remember { mutableStateOf(false /*setReadyDirectly*/) }
            val controlcardvisible = remember { mutableStateOf(false) }
            val addmediacardvisible = remember { mutableStateOf(false) }

            val gestures = remember { mutableStateOf(false) }
            val userinfoVisibility = remember { mutableStateOf(false) }
            val sharedplaylistVisibility = remember { mutableStateOf(false) }
            val inroomprefsVisibility = remember { mutableStateOf(false) }

            if (!startupSlide) {
                LaunchedEffect(null) {
                    generalScope.launch {
                        delay(600)
                        userinfoVisibility.value = true
                        startupSlide = true
                    }
                }
            }

            /* Seek animators, their only purpose is to animate a seek action */
            if (gestures.value) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(modifier = Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxHeight().fillMaxWidth(0.1f)
                        .clickable(
                            enabled = false,
                            interactionSource = seekLeftInteraction,
                            indication = rememberRipple(
                                bounded = false,
                                color = Color(100, 100, 100, 190)
                            )
                        ) {}
                    )

                    Box(modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight().fillMaxWidth(0.1f)
                        .clickable(
                            interactionSource = seekRightInteraction,
                            indication = rememberRipple(
                                bounded = false,
                                color = Color(100, 100, 100, 190)
                            )
                        ) {}
                    )
                }
            }

            /* Gestures Interceptor */
            Box(modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = if (gestures.value && hasVideo.value) { offset ->
                            generalScope.launch {
                                if (offset.x < screenWidthPx.times(0.25f)) {
                                    //TODO: seekBckwd()

                                    val press = PressInteraction.Press(Offset.Zero)
                                    seekLeftInteraction.emit(press)
                                    delay(150)
                                    seekLeftInteraction.emit(PressInteraction.Release(press))
                                }
                                if (offset.x > screenWidthPx.times(0.85f)) {
                                    //TODO: seekFwd()

                                    val press = PressInteraction.Press(Offset.Zero)
                                    seekRightInteraction.emit(press)
                                    delay(150)
                                    seekRightInteraction.emit(PressInteraction.Release(press))
                                }
                            }
                        } else null,
                        onTap = {
                            hudVisibility.value = !hudVisibility.value
                            if (!hudVisibility.value) {
                                /* Hide any popups */
                                controlcardvisible.value = false
                                addmediacardvisible.value = false
                            }
                        }
                    )
                }
            )

            /* This is the UI builder for the HUD.
         * In order to replace the limitation of not being able to use ConstraintLayout in KMM,
         * we can resort to using a combination of Boxes, Rows, and Columns. */
            if (hudVisibility.value) {
                Box(modifier = Modifier.fillMaxSize().padding(12.dp)) {

                    /* Top row (Message input box + Messages) */
                    Column(
                        modifier = Modifier.fillMaxWidth(0.32f).align(Alignment.TopStart),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            /** Message input box */
                            if (!isSoloMode() && !pipModeObserver) {
                                val onSend = fun() {
                                    val msgToSend = msg.value.also {
                                        it.replace("\\", "")
                                        if (it.length > 150) it.substring(0, 149)
                                    }
                                    if (msgToSend.isNotBlank()) {
                                        //TODO: sendMessage(msgToSend)
                                    }
                                    msg.value = ""
                                    msgCanSend.value = false

                                    focusManager.clearFocus()
                                }

                                OutlinedTextField(
                                    modifier = Modifier.alpha(0.75f).gradientOverlay().fillMaxWidth(),
                                    singleLine = true,
                                    keyboardActions = KeyboardActions(onDone = {
                                        if (keyboardOkFunction) {
                                            onSend()
                                        }
                                    }),
                                    label = { Text(text = "Type your message...", fontSize = 12.sp) },
                                    trailingIcon = {
                                        if (msgCanSend.value) {
                                            IconButton(onClick = { onSend() }) {
                                                Icon(imageVector = Icons.Filled.Send, "")
                                            }
                                        }
                                    },
                                    value = msg.value,
                                    onValueChange = { s ->
                                        msg.value = s
                                        msgCanSend.value = s.isNotBlank()
                                    }
                                )
                            }
                        }

                        /** Messages */
                        if (!isSoloMode) {
                            Card(
                                modifier = Modifier.focusable(false),
                                shape = RoundedCornerShape(4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (hasVideo.value || MaterialTheme.colorScheme.primary != Paletting.OLD_SP_YELLOW)
                                        Color(50, 50, 50, msgBoxOpacity.value) else Color.Transparent
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                            ) {

                                LazyColumn(
                                    contentPadding = PaddingValues(8.dp),
                                    userScrollEnabled = false,
                                    modifier = Modifier.fillMaxWidth().focusable(false)
                                ) {
                                    //TODO: Show errors in red
                                    val lastMessages = msgs.toList().takeLast(msgMaxCount)
                                    items(lastMessages) {
                                        LaunchedEffect(null) {
                                            it.seen = true /* Once seen, don't use it in fading message */
                                        }

                                        Box {
                                            val text = it.factorize(msgPalette)

                                            FlexibleFancyAnnotatedText(
                                                modifier = Modifier.fillMaxWidth()
                                                    .clickable(enabled = false) {}.focusable(false),
                                                text = text,
                                                size = if (pipModeObserver) 6f else (msgFontSize.value.toFloat()),
                                                font = MR.fonts.Inter.regular.asFont()!!,
                                                lineHeight = (msgFontSize.value + 4).sp,
                                                overflow = TextOverflow.Ellipsis,
                                                shadowSize = 1.5f,
                                                shadowColors = if (msgOutline.value) listOf(Color.Black) else listOf()
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    /* Top-Center info */
                    /** Overall info (PING + ROOMNAME + OSD Messages) */
                    if (!isSoloMode() && !pipModeObserver) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.wrapContentWidth().align(Alignment.TopCenter)
                        ) {
                            if (!isSoloMode) {
                                Row {
                                    Text(
                                        text = if (p.ping.doubleValue < 0) {
                                            "Disconnected"
                                        } else {
                                            "Connected - ${p.ping.doubleValue.toInt()}ms"
                                        },
                                        color = Paletting.OLD_SP_PINK
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    /*
                        Image(
                            when (p.ping.doubleValue) {
                                (-1.0) -> painterResource(MR.images.icon_unconnected)
                                in (0.0..100.0) -> painterResource(MR.images.ping_3)
                                in (100.0..199.0) -> painterResource(MR.images.ping_2)
                                else -> painterResource(MR.images.ping_1)
                            }, "", Modifier.size(16.dp)
                        )
                         */
                                }
                                Text(text = "Room: ${p.session.currentRoom}", fontSize = 11.sp, color = Paletting.OLD_SP_PINK)
                            }
                        }
                    }

                    /* Card tabs (Top-right row) and the Cards below them  */
                    Column(
                        modifier = Modifier.align(Alignment.TopEnd),
                        horizontalAlignment = Alignment.End
                    ) {
                        /* The tabs row */
                        Row(
                            modifier = Modifier.fillMaxWidth().height(50.dp)
                                .padding(top = 15.dp, end = 4.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            /** The tabs in the top-right corner */
                            /** In-room settings */
                            RoomTab(icon = Icons.Filled.AutoFixHigh, visibilityState = inroomprefsVisibility.value) {
                                sharedplaylistVisibility.value = false
                                userinfoVisibility.value = false
                                inroomprefsVisibility.value = !inroomprefsVisibility.value
                            }

                            Spacer(Modifier.width(12.dp))

                            /** Shared Playlist */
                            if (!isSoloMode()) {
                                RoomTab(icon = Icons.Filled.PlaylistPlay, visibilityState = sharedplaylistVisibility.value) {
                                    sharedplaylistVisibility.value = !sharedplaylistVisibility.value
                                    userinfoVisibility.value = false
                                    inroomprefsVisibility.value = false
                                }

                                Spacer(Modifier.width(12.dp))
                            }

                            /** User Info card tab */
                            if (!isSoloMode()) {
                                RoomTab(icon = Icons.Filled.Groups, visibilityState = userinfoVisibility.value) {
                                    userinfoVisibility.value = !userinfoVisibility.value
                                    sharedplaylistVisibility.value = false
                                    inroomprefsVisibility.value = false
                                }

                                Spacer(Modifier.width(12.dp))
                            }


                            /** Lock card */
                            RoomTab(icon = Icons.Filled.Lock, visibilityState = false) {
                                locked.value = true
                                hudVisibility.value = false
                            }

                            Spacer(Modifier.width(6.dp))

                            Box {
                                val overflowmenustate = remember { mutableStateOf(false) }
                                FancyIcon2(icon = Icons.Filled.MoreVert, size = ROOM_ICON_SIZE, shadowColor = Color.Black) {
                                    overflowmenustate.value = !overflowmenustate.value
                                }

                                DropdownMenu(
                                    modifier = Modifier.background(color = MaterialTheme.colorScheme.tertiaryContainer),
                                    expanded = overflowmenustate.value,
                                    properties = PopupProperties(
                                        dismissOnBackPress = true,
                                        focusable = true,
                                        dismissOnClickOutside = true
                                    ),
                                    onDismissRequest = { overflowmenustate.value = false }) {

                                    ComposeUtils.FancyText2(
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(horizontal = 2.dp),
                                        string = "More Options...",
                                        solid = Color.Black,
                                        size = 14f,
                                        font = MR.fonts.Directive4.regular.asFont()
                                    )

                                    /* Chat history item */
                                    if (!isSoloMode()) {
                                        DropdownMenuItem(
                                            text = {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        modifier = Modifier.padding(2.dp),
                                                        imageVector = Icons.Filled.Forum, contentDescription = "",
                                                        tint = Color.LightGray
                                                    )

                                                    Spacer(Modifier.width(8.dp))

                                                    Text(
                                                        color = Color.LightGray,
                                                        text = "Chat history"
                                                    )
                                                }
                                            },
                                            onClick = {
                                                overflowmenustate.value = false
                                                chathistorypopupstate.value = true
                                            }
                                        )
                                    }

                                    /* Toggle Dark mode */
                                    DropdownMenuItem(
                                        text = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    modifier = Modifier.padding(2.dp),
                                                    imageVector = Icons.Filled.DarkMode, contentDescription = "",
                                                    tint = Color.LightGray
                                                )

                                                Spacer(Modifier.width(8.dp))

                                                Text(
                                                    color = Color.LightGray,
                                                    text = "Toggle night-mode"
                                                )
                                            }
                                        },
                                        onClick = {
                                            overflowmenustate.value = false

                                            val newMode = runBlocking {
                                                !DATASTORE_MISC_PREFS.obtainBoolean(MISC_NIGHTMODE, true)
                                            }

                                            /*
                            FIXME: if (newMode) {
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                            } else {
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                            }

                             */

                                            generalScope.launch {
                                                DATASTORE_MISC_PREFS.writeBoolean(MISC_NIGHTMODE, newMode)
                                            }
                                        }
                                    )

                                    /* Leave room item */
                                    DropdownMenuItem(
                                        text = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    modifier = Modifier.padding(2.dp),
                                                    imageVector = Icons.Filled.Logout, contentDescription = "",
                                                    tint = Color.LightGray
                                                )

                                                Spacer(Modifier.width(8.dp))

                                                Text(
                                                    color = Color.LightGray,
                                                    text = "Leave room"
                                                )
                                            }
                                        },
                                        onClick = {
                                            //TODO: terminate()
                                        }
                                    )
                                }
                            }


                        }

                        Spacer(Modifier.height(20.dp))

                        /* The cards below */
                        val cardWidth = 0.36f
                        val cardHeight = 0.72f
                        Box {
                            /** User-info card (toggled on and off) */
                            if (!isSoloMode()) {
                                FreeAnimatedVisibility(
                                    modifier = Modifier
                                        .fillMaxWidth(cardWidth)
                                        .fillMaxHeight(cardHeight),
                                    enter = slideInHorizontally(initialOffsetX = { (screenWidthPx + screenWidthPx * 0.3).toInt() }),
                                    exit = slideOutHorizontally(targetOffsetX = { (screenWidthPx + screenWidthPx * 0.3).toInt() }),
                                    visible = !inroomprefsVisibility.value && userinfoVisibility.value && !sharedplaylistVisibility.value
                                ) {
                                    UserInfoCard()
                                }
                            }

                            /** Shared Playlist card (toggled on and off) */
                            if (!isSoloMode()) {
                                FreeAnimatedVisibility(
                                    modifier = Modifier
                                        .fillMaxWidth(cardWidth)
                                        .fillMaxHeight(cardHeight),
                                    enter = slideInHorizontally(initialOffsetX = { (screenWidthPx + screenWidthPx * 0.3).toInt() }),
                                    exit = slideOutHorizontally(targetOffsetX = { (screenWidthPx + screenWidthPx * 0.3).toInt() }),
                                    visible = !inroomprefsVisibility.value && !userinfoVisibility.value && sharedplaylistVisibility.value
                                ) {
                                    //TODO: SharedPlaylistCard()
                                }
                            }

                            /** In-room card (toggled on and off) */
                            FreeAnimatedVisibility(
                                modifier = Modifier
                                    .fillMaxWidth(cardWidth)
                                    .fillMaxHeight(cardHeight),
                                enter = slideInHorizontally(initialOffsetX = { (screenWidthPx + screenWidthPx * 0.3).toInt() }),
                                exit = slideOutHorizontally(targetOffsetX = { (screenWidthPx + screenWidthPx * 0.3).toInt() }),
                                visible = inroomprefsVisibility.value && !userinfoVisibility.value && !sharedplaylistVisibility.value
                            ) {
                                InRoomSettingsCard()
                            }
                        }
                    }

                    /* Bottom-left row (Ready button) */
                    if (!isSoloMode()) {
                        IconToggleButton(modifier = Modifier
                            .width(112.dp)
                            .padding(8.dp)
                            .align(Alignment.BottomStart),
                            checked = ready.value,
                            colors = IconButtonDefaults.iconToggleButtonColors(
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                contentColor = MaterialTheme.colorScheme.primary,
                                checkedContainerColor = MaterialTheme.colorScheme.primary,
                                checkedContentColor = Color.Black
                            ),
                            onCheckedChange = { b ->
                                ready.value = b
                                p.ready = b
                                p.sendPacket(JsonSender.sendReadiness(b, true))
                            }) {
                            when (ready.value) {
                                true -> Text("Ready")
                                false -> Text("Not Ready")
                            }
                        }
                    }

                    /* Bottom-mid row (Slider + timestamps) */
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.align(Alignment.BottomCenter).padding(4.dp).fillMaxWidth()
                    ) {
                        val slidervalue = remember { timeCurrent }
                        val slidermax = remember { timeFull }
                        val interactionSource = remember { MutableInteractionSource() }

                        Row(modifier = Modifier.fillMaxWidth(0.75f)) {

                            Column(
                                modifier = Modifier.fillMaxWidth(0.33f),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Bottom,
                            ) {
                                Text(
                                    text = timeStamper(remember { timeCurrent }.longValue),
                                    modifier = Modifier
                                        .alpha(0.85f)
                                        .gradientOverlay(),
                                )
                            }

                            if (!gestures.value) {
                                Column(
                                    Modifier.fillMaxWidth(0.5f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row(horizontalArrangement = Arrangement.Center) {
                                        FancyIcon2(icon = Icons.Filled.FastRewind, size = ROOM_ICON_SIZE + 6, shadowColor = Color.Black) {
                                            //seekBckwd()
                                        }
                                        Spacer(Modifier.width(24.dp))
                                        FancyIcon2(icon = Icons.Filled.FastForward, size = ROOM_ICON_SIZE + 6, shadowColor = Color.Black) {
                                            //seekFwd()
                                        }
                                    }
                                }
                            }

                            Column(
                                Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.End
                            ) {
                                val timeFullR = remember { timeFull }
                                Text(
                                    text = if (timeFullR.longValue >= Long.MAX_VALUE / 1000L) "???" else timeStamper(timeFullR.longValue),
                                    modifier = Modifier
                                        .alpha(0.85f)
                                        .gradientOverlay(),
                                )
                            }
                        }
                        Slider(
                            value = slidervalue.longValue.toFloat(),
                            valueRange = (0f..(slidermax.longValue.toFloat())),
                            onValueChange = { f ->
                                generalScope.launch {
                                    /* FIXME: Seeking
                                if (isSoloMode()) {
                                    if (player == null) return@launch
                                    seeks.add(Pair(player!!.getPositionMs(), f.toLong() * 1000))
                                }
                                player?.seekTo(f.toLong() * 1000L)
                                sendSeek(f.toLong() * 1000L)
                                 */
                                }
                                slidervalue.longValue = f.toLong()
                            },
                            modifier = Modifier
                                .alpha(0.82f)
                                .fillMaxWidth(0.75f)
                                .padding(horizontal = 12.dp),
                            interactionSource = interactionSource,
                            steps = 500,
                            thumb = remember(SliderDefaults.colors(), true) {
                                {
                                    SliderDefaults.Thumb(
                                        interactionSource = interactionSource,
                                        colors = SliderDefaults.colors(),
                                        enabled = true,
                                        modifier = Modifier.alpha(0.8f)
                                    )
                                }
                            }
                        )
                    }

                    /* Bottom-right row (Controls) */
                    Row(modifier = Modifier.align(Alignment.BottomEnd).padding(4.dp)) {
                        FancyIcon2(
                            modifier = Modifier,
                            icon = Icons.Filled.VideoSettings, size = ROOM_ICON_SIZE + 6, shadowColor = Color.Black,
                            onClick = {
                                controlcardvisible.value = !controlcardvisible.value
                                addmediacardvisible.value = false
                            }
                        )

                        /* The button of adding media */
                        AddVideoButton(
                            modifier = Modifier,
                            onClick = {
                                addmediacardvisible.value = !addmediacardvisible.value
                                controlcardvisible.value = false

                                //TODO: REMOVE
                                hasVideo.value = !hasVideo.value
                            }
                        )
                    }

                    /** PLAY BUTTON */
                    val playing = remember { isNowPlaying }
                    FancyIcon2(
                        icon = when (playing.value) {
                            true -> Icons.Filled.Pause
                            false -> Icons.Filled.PlayArrow
                        },
                        size = (ROOM_ICON_SIZE * 2.25).roundToInt(),
                        shadowColor = Color.Black,
                        modifier = Modifier.align(Alignment.Center)
                    ) { /* FIXME: OnClick
                    if (player?.isInPlayState() == true) {
                        pausePlayback()
                    } else {
                        playPlayback()
                    }*/
                    }
                }
            }
        }

        /** Fading Message overlay */
        if (!isSoloMode()) {
            fadingMessageLayout(
                hudVisibility = hudVisibility.value,
                pipModeObserver = pipModeObserver,
                msgPalette
            )
        }


        /** Popups */
        //AddUrlPopup(visibilityState = addurlpopupstate)
        //SeekToPositionPopup(visibilityState = seektopopupstate)
        //if (!isSoloMode()) ChatHistoryPopup(visibilityState = chathistorypopupstate)

    }
}