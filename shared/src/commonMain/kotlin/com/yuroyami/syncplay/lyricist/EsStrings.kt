package com.yuroyami.syncplay.lyricist

import com.yuroyami.syncplay.utils.format

val EsStrings = object : Strings {
    override val appName: String
        get() = TODO("Not yet implemented")

    override val yes = "Sí"

    override val no = "No"

    override val okay = "Aceptar"

    override val cancel = "Cancelar"

    override val dontShowAgain = "No mostrar de nuevo"

    override val play = "Reproducir"

    override val pause = "Pausar"

    override val delete = "Eliminar"

    override val confirm = "Confirmar"

    override val done = "Hecho"

    override val close = "Cerrar"

    override val off = "Apagado"

    override val on = "Encendido"

    override val tabConnect = "Conectar"

    override val tabSettings = "Configuraciones"

    override val tabAbout = "Acerca"

    override val connectUsernameA = "Escribe tu nombre de usuario:"

    override val connectUsernameB = "Nombre de usuario"

    override val connectUsernameC = "Escribe lo que quieras"

    override val connectRoomnameA = "Escribe el nombre de la sala:"

    override val connectRoomnameB = "Nombre de la sala"

    override val connectRoomnameC = "Sala donde tú y tus amigos miran"

    override val connectServerA = "Selecciona el servidor Syncplay:"

    override val connectServerB = "Dirección del servidor"

    override val connectServerC = "Asegúrate de que tú y tus amigos se unan al mismo servidor."

    override val connectButtonJoin = "Unirse/crear sala"

    override val connectButton = "Unirse/crear sala"

    override val connectButtonSaveshortcut = "Agregar acceso directo a Inicio con la configuración actual"
    override val connectButtonCurrentEngine: (String) -> String
        get() = TODO("Not yet implemented")

    override val connectButtonSwitchplayerExo = "Cambiar a reproductor ExoPlayer (Estable)"

    override val connectButtonSwitchplayerMpv = "Cambiar a reproductor MPV (Experimental)"

    override val connectFootnote = "Cliente Android no oficial de Syncplay"

    override val connectUsernameEmptyError = "El nombre de usuario no debe estar vacío"

    override val connectRoomnameEmptyError = "El nombre de la sala no debe estar vacío"

    override val connectAddressEmptyError = "La dirección del servidor no debe estar vacía"

    override val connectPortEmptyError = "¡Ingresa el puerto!"

    override val connectEnterCustomServer = "Ingresa al servidor personalizado"

    override val connectCustomServerPassword = "Contraseña (vacía si no es necesaria)"

    override val connectPort = "Puerto"

    override val connectNightmodeswitch = "Cambia el modo día/noche."

    override val connectSolomode = "Ingresa al modo solo (solo reproductor)"

    override val roomStartupHint = "\"Inserta un nuevo video desde el almacenamiento haciendo clic en el botón con el ícono a continuación \""

    override val roomSelectedVid = { p0: String -> 
        "Archivo de vídeo seleccionado: %s"
            .format(p0)
    }

    override val roomSelectedSub = { p0: String -> 
        "Archivo de subtítulos seleccionado: %s"
            .format(p0)
    }

    override val roomSelectedSubError = "Archivo de subtítulos no válido. Los formatos admitidos son: 'SRT', 'TTML', 'ASS', 'SSA', 'VTT'"

    override val roomSubErrorLoadVidFirst = "Cargar video primero"

    override val roomTypeMessage = "Escribe tu mensaje…"

    override val roomSendButton = "ENVIAR"

    override val roomReady = "Listo"

    override val roomDetails = "Mostrar detalles"

    override val roomPingConnected = { p0: String -> 
        "Conectado - PING: %s ms"
            .format(p0)
    }

    override val roomPingDisconnected = "DESCONECTADO"

    override val roomOverflowSub = "Cargar archivo de subtítulo…"

    override val roomOverflowCutout = "Modo de corte (Notch)"

    override val roomOverflowFf = "Botones de búsqueda rápida"

    override val roomOverflowMsghistory = "Historial de mensajes"

    override val roomOverflowSettings = "Configuraciones"

    override val roomEmptyMessageError = "¡Escribe algo!"

    override val roomAttemptingConnect = "Intentando conectarse a %1s:%2s"

    override val roomConnectedToServer = "Conectado al servidor."

    override val roomConnectionFailed = "La conexión falló."

    override val roomAttemptingReconnection = "Se perdió la conexión con el servidor."

    override val roomGuyPlayed = { p0: String -> 
        "%s reanudó la reproducción"
            .format(p0)
    }

    override val roomGuyPaused = "%1s pausó en %2s"

    override val roomSeeked = "%1s saltó desde %2s a %3s"

    override val roomRewinded = { p0: String -> 
        "Rebobinado por desincronización con %s"
            .format(p0)
    }

    override val roomGuyLeft = { p0: String -> 
        "%s dejó la sala"
            .format(p0)
    }

    override val roomGuyJoined = { p0: String -> 
        "%s se unió a la sala."
            .format(p0)
    }

    override val roomIsplayingfile = "%1s está reproduciendo '%2s' (%3s)"

    override val roomYouJoinedRoom = { p0: String -> 
        "Te has unido a la sala: %s"
            .format(p0)
    }

    override val roomScalingFitScreen = "Modo de redimenionamiento: AJUSTAR A LA PANTALLA"

    override val roomScalingFixedWidth = "Modo de redimenionamiento: ANCHO FIJO"

    override val roomScalingFixedHeight = "Modo de redimenionamiento: ALTURA FIJA"

    override val roomScalingFillScreen = "Modo de redimenionamiento: LLENAR PANTALLA"

    override val roomScalingZoom = "Modo de redimenionamiento: Zoom"

    override val roomSubTrackChanged = { p0: String -> 
        "Pista de subtítulos cambaida a: %s"
            .format(p0)
    }

    override val roomAudioTrackChanged = { p0: String -> 
        "Pista de audio cambiada a: %s"
            .format(p0)
    }

    override val roomAudioTrackNotFound = "¡No se encontró audio!"

    override val roomSubTrackDisable = "Desactivar subtítulos"

    override val roomTrackTrack = "Pista"

    override val roomSubTrackNotfound = "¡No se encontraron subtítulos!"

    override val roomDetailsCurrentRoom = { p0: String -> 
        "Sala actual: %s"
            .format(p0)
    }

    override val roomDetailsNofileplayed = "(Ningún archivo en reproducción)"

    override val roomDetailsFileProperties = "Duración: %1s - Tamaño: %2s MB"

    override val disconnectedNoticeHeader = "DESCONECTADO"

    override val disconnectedNoticeA = "Te has desconectado del servidor.Esto puede deberse a un problema con tu conexión a Internet o un problema con el servidor al que te estás conectando. Si el problema persiste, cambia de servidor."

    override val disconnectedNoticeB = "Intentando reconectar..."

    override val roomFileMismatchWarningCore = { p0: String -> 
        "\"Tu archivo difiere del archivo de %s en los siguientes aspectos: \""
            .format(p0)
    }

    override val roomFileMismatchWarningName = "\"Nombre. \""

    override val roomFileMismatchWarningDuration = "\"Duración. \""

    override val roomFileMismatchWarningSize = "\"Tamaño. \""

    override val roomSharedPlaylist = "Lista de reproducción compartida de la sala"

    override val roomSharedPlaylistBrief = "Importa un archivo o un directorio para incluir el nombre del archivo en la lista de reproducción. Haz clic en una línea de archivo para que todos los usuarios lo reproduzcan."

    override val roomSharedPlaylistUpdated = { p0: String -> 
        "%s actualizó la lista de reproducción"
            .format(p0)
    }

    override val roomSharedPlaylistChanged = { p0: String -> 
        "%s cambió la selección actual de la lista de reproducción"
            .format(p0)
    }

    override val roomSharedPlaylistNoDirectories = "No has especificado ningún directorio de medios para listas de reproducción compartidas. Añade manualmente los archivos."

    override val roomSharedPlaylistNotFound = "Syncplay no pudo encontrar el archivo que se está reproduciendo actualmente en la lista de reproducción compartida en tus directorios de medios."

    override val roomSharedPlaylistNotFeatured = "Esta sala o servidor no tiene la función de Listas de reproducción compartidas activada."

    override val roomSharedPlaylistButtonAddFile = "Agregar archivo(s) al final de la lista de reproducción"

    override val roomSharedPlaylistButtonAddFolder = "Agregar carpeta a la lista de reproducción (y directorios de medios)"

    override val roomSharedPlaylistButtonAddUrl = "Agregar URL(s) al final de la lista de reproducción"

    override val roomSharedPlaylistButtonShuffle = "Mezclar toda la lista de reproducción"

    override val roomSharedPlaylistButtonShuffleRest = "Mezclar lista de reproducción restante"

    override val roomSharedPlaylistButtonOverflow = "Más configuraciones de lista de reproducción compartida"

    override val roomSharedPlaylistButtonPlaylistImport = "Cargar lista de reproducción desde archivo"

    override val roomSharedPlaylistButtonPlaylistImportNShuffle = "Cargar y mezclar lista de reproducción desde archivo"

    override val roomSharedPlaylistButtonPlaylistExport = "Guardar lista de reproducción en archivo"

    override val roomSharedPlaylistButtonSetMediaDirectories = "Establecer directorios de medios"

    override val roomSharedPlaylistButtonSetTrustedDomains = "Establecer dominios de confianza"

    override val roomSharedPlaylistButtonUndo = "Deshacer último cambio"

    override val roomButtonDescAspectRatio = "Relación de aspecto"

    override val roomButtonDescSharedPlaylist = "Lista de reproducción compartida"

    override val roomButtonDescAudioTracks = "Pistas de audio"

    override val roomButtonDescSubtitleTracks = "Pistas de subtítulos"

    override val roomButtonDescRewind = "Retroceder"

    override val roomButtonDescToggle = "."

    override val roomButtonDescPlay = "Reproducir"

    override val roomButtonDescPause = "Pausar"

    override val roomButtonDescFfwd = "Avance rápido"

    override val roomButtonDescAdd = "Agregar archivo multimedia"

    override val roomButtonDescLock = "Bloqueo táctil"

    override val roomButtonDescMore = "Más configuraciones"

    override val roomAddmediaOffline = "Desde el almacenamiento del teléfono"

    override val roomAddmediaOnline = "Desde una URL de red"

    override val roomAddmediaOnlineUrl = "Dirección URL"

    override val mediaDirectories = "Directorios de medios para Lista de reproducción compartida"

    override val mediaDirectoriesBrief = "Syncplay buscará en los directorios de medios que especifiques aquí para encontrar el nombre que está reproduciendo una lista de reproducción compartida. Es mucho mejor si especificas directorios pequeños, ya que la operación de búsqueda puede limitarse y ser muy lenta."

    override val mediaDirectoriesSettingSummary = "Syncplay buscará en los directorios de medios que especifiques aquí para encontrar el nombre que está reproduciendo una lista de reproducción compartida."

    override val mediaDirectoriesSave = "Guardar y salir"

    override val mediaDirectoriesClearAll = "Limpiar todo"

    override val mediaDirectoriesClearAllConfirm = "¿Estás seguro de que quieres borrar la lista ?"

    override val mediaDirectoriesAddFolder = "Agregar carpeta"

    override val mediaDirectoriesDelete = "Eliminar de la lista"

    override val mediaDirectoriesShowFullPath = "Mostrar ruta completa"

    override val settingsCategGeneral = "Configuración general"

    override val settingsCategPlayer = "Configuración del reproductor"

    override val settingsCategRoom = "Configuración de la sala"

    override val settingsCategVideo = "Configuración de video"

    override val settingsCategMisc = "Misceláneo"

    override val settingNightModeTitle = "Modo nocturno"

    override val settingNightModeSummary = "Selecciona el comportamiento del modo nocturno."

    override val settingRememberJoinInfoTitle = "Recordar información de entrada"

    override val settingRememberJoinInfoSummary = "Activar por defecto. Esto permitirá que SyncPlay guarde tu último nombre de usuario, nombre de sala y qué servidor oficial utilizaste por última vez."

    override val settingDisplayLanguageTitle = "Idioma de visualización"

    override val settingDisplayLanguageSummry = "Selecciona el idioma con el que SyncPlay se mostrará."

    override val settingDisplayLanguageToast = "Idioma cambiado. Reinicia la aplicación para que la configuración tenga efecto completo."

    override val settingAudioDefaultLanguageTitle = "Idioma preferido de la pista de audio"

    override val settingAudioDefaultLanguageSummry = "Carga automáticamente una pista de audio con el código de idioma que establezcas aquí.nPor ejemplo, el código en inglés es 'en-US', en japonés es 'ja-JP'.nConsulta 'Códigos BCP 47 de la IETF' para obtener más información."

    override val settingCcDefaultLanguageTitle = "Idioma preferido de subtítulos"

    override val settingCcDefaultLanguageSummry = "Carga automáticamente una pista de subtítulos con el código de idioma que establezcas aquí.nPor ejemplo, el código en inglés es 'en-US', en japonés es 'ja-JP'.nConsulta 'Códigos BCP 47 de la IETF' para obtener más información."

    override val settingUseBufferTitle = "Usar tamaños de búfer personalizados"

    override val settingUseBufferSummary = "Si no estás satisfecho con los tiempos de carga de video del reproductor antes y durante la reproducción, puedes usar tamaños de búfer personalizados (úsalo bajo tu propio riesgo)."

    override val settingMaxBufferTitle = "Tamaño máximo de búfer personalizado"

    override val settingMaxBufferSummary = "El valor predeterminado es 30 (30000 milisegundos). Esto determina el tamaño máximo del búfer antes de comenzar a reproducir el video. Si no sabes qué es esto, no lo cambies."

    override val settingMinBufferSummary = "El valor predeterminado es 15 (15000 milisegundos). Disminuye este valor para reproducir el video más rápido, pero existe la posibilidad de que el reproductor falle o incluso se cierre. Cámbialo bajo tu propio riesgo."

    override val settingMinBufferTitle = "Tamaño mínimo de búfer personalizado"

    override val settingPlaybackBufferSummary = "El valor predeterminado es 2500 milisegundos. Esto representa el tamaño del búfer al REALIZAR BÚSQUEDA o REANUDAR el video. Cambia esto si no estás satisfecho con el pequeño retraso al buscar el video."

    override val settingPlaybackBufferTitle = "Tamaño de búfer de reproducción personalizado (ms)"

    override val settingReadyFirsthandSummary = "Activa esto si quieres que se te establezca automáticamente como listo una vez que ingreses a la sala."

    override val settingReadyFirsthandTitle = "Establecer como listo de inmediato"

    override val settingRewindThresholdSummary = "Si alguien está rezagado con el valor que seleccionas aquí, tu video retrocederá para coincidir con el de la persona rezagada."

    override val settingRewindThresholdTitle = "Umbral de retroceso"

    override val settingTlsSummary = "Si un servidor admite una conexión segura TLS, Syncplay Android intentará conectarse a través de TLS. (No disponible aún)"

    override val settingTlsTitle = "Usar conexión segura (TLSv1.3) [Próximamente]"

    override val settingResetdefaultTitle = "Restablecer configuración predeterminada"

    override val settingResetdefaultSummary = "Restablece todo a su valor predeterminado (Recomendado)"

    override val settingResetdefaultDialog = "¿Estás seguro de borrar la configuración de esta pantalla?"

    override val settingPauseIfSomeoneLeftTitle = "Pausar si alguien se va"

    override val settingPauseIfSomeoneLeftSummary = "Activa esto si quieres que la reproducción se pause/detenga si alguien abandona la sala mientras estás viendo."

    override val settingWarnFileMismatchTitle = "Advertencia de discrepancia de archivos"

    override val settingWarnFileMismatchSummary = "Activado por defecto. Esto te advertirá en caso de que cargues un archivo que sea diferente de los usuarios del grupo (en términos de nombre, duración o tamaño, no todos)."

    override val settingFileinfoBehaviourNameTitle = "Envío de información del nombre del archivo"

    override val settingFileinfoBehaviourNameSummary = "Elige el método con el que mostrarás el nombre de tu archivo multimedia agregado a otros usuarios."

    override val settingFileinfoBehaviourSizeTitle = "Envío de información del tamaño del archivo"

    override val settingFileinfoBehaviourSizeSummary = "Elige el método con el que mostrarás el tamaño de tu archivo multimedia agregado a otros usuarios."

    override val uisettingApply = "APLICAR"

    override val uisettingTimestampSummary = "Desactiva esto para ocultar las marcas de tiempo al principio de los mensajes de chat."

    override val uisettingTimestampTitle = "Marcas de tiempo en el chat"

    override val uisettingMsgoutlineSummary = "Activa esto para darle a los mensajes de chat un contorno y reducir su fusión con el fondo del video."

    override val uisettingMsgoutlineTitle = "Contorno de mensajes de chat"
    override val uisettingMsgshadowSummary: String
        get() = TODO("Not yet implemented")
    override val uisettingMsgshadowTitle: String
        get() = TODO("Not yet implemented")

    override val uisettingMsgboxactionSummary = "Cuando está habilitado, hacer clic en el botón 'ACEPTAR' de tu teclado enviará el mensaje. Cuando está deshabilitado, simplemente cerrará el teclado sin hacer nada."

    override val uisettingMsgboxactionTitle = "Función del botón ACEPTAR del teclado"

    override val uisettingOverviewAlphaSummary = "El valor predeterminado es 40 (casi transparente), cámbialo si quieres hacer más legible el panel de detalles de la sala aumentando la opacidad."

    override val uisettingOverviewAlphaTitle = "Opacidad del fondo de detalles de la sala"

    override val uisettingMessageryAlphaSummary = "El valor predeterminado es 0 (transparente). El máximo es 255 (100% opaco). Aumenta la legibilidad de los mensajes haciendo el fondo más opaco."

    override val uisettingMessageryAlphaTitle = "Opacidad del fondo de mensajes"

    override val uisettingMsgsizeSummary = "Cambia el tamaño del texto de los mensajes. El valor predeterminado es 10."

    override val uisettingMsgsizeTitle = "Tamaño de fuente de mensajes"

    override val uisettingMsgcountSummary = "El valor predeterminado es 10. Limita el número máximo de mensajes a este valor."

    override val uisettingMsgcountTitle = "Número máximo de mensajes"

    override val uisettingMsglifeSummary = "Cuando recibes un mensaje de chat o mensaje de sala, comenzará a desvanecerse durante el tiempo establecido a continuación."

    override val uisettingMsglifeTitle = "Duración de visualización de mensajes de chat"

    override val uisettingTimestampColorSummary = "Personaliza el color del texto de las marcas de tiempo de los mensajes (Predeterminado es Rosa)."

    override val uisettingTimestampColorTitle = "Color del texto de marcas de tiempo"

    override val uisettingSelfColorSummary = "Personaliza el color del texto de tu etiqueta de nombre (Predeterminado es Rojo Oscuro)."

    override val uisettingSelfColorTitle = "Color de la etiqueta propia"

    override val uisettingFriendColorSummary = "Personaliza el color del texto de las etiquetas de nombres de amigos (Predeterminado es Azul)."

    override val uisettingFriendColorTitle = "Color del texto de la etiqueta de amigos"

    override val uisettingSystemColorSummary = "Personaliza el color del texto de los mensajes del sistema de la sala (Predeterminado es Blanco)."

    override val uisettingSystemColorTitle = "Color del texto de mensajes del sistema"

    override val uisettingHumanColorSummary = "Personaliza el color del texto de los mensajes de usuario (Predeterminado es Blanco)."

    override val uisettingHumanColorTitle = "Color del texto de mensajes de usuario"

    override val uisettingErrorColorSummary = "Personaliza el color del texto de los mensajes de error (Predeterminado es Rojo)."

    override val uisettingErrorColorTitle = "Color del texto de mensajes de error"

    override val uisettingSubtitleSizeSummary = "Esto cambia el tamaño de los subtítulos para los subtítulos cargados externamente (cuando los cargas desde un archivo). El valor predeterminado es 16. El tamaño de los subtítulos built-in no se puede cambiar."

    override val uisettingSubtitleSizeTitle = "Tamaño de subtítulos"

    override val uisettingSubtitleDelaySummary = "El valor predeterminado es 0. Esto retrasará o adelantará la pista de subtítulos. Usa valores negativos para adelantarlo."

    override val uisettingSubtitleDelayTitle = "Retardo de subtítulos (milisegundos)"

    override val uisettingAudioDelaySummary = "El valor predeterminado es 0. Esto retrasará o adelantará la pista de audio. Usa valores negativos para adelantarlo."

    override val uisettingAudioDelayTitle = "Retardo de pista de audio (milisegundos)"

    override val uisettingSeekForwardJumpSummary = "Especifica cuántos segundos debería saltar una búsqueda hacia adelante. El valor predeterminado es 10 segundos."

    override val uisettingSeekForwardJumpTitle = "Cantidad de salto hacia adelante en la búsqueda (segundos)"

    override val uisettingSeekBackwardJumpSummary = "Especifica cuántos segundos debería rebobinar una búsqueda hacia atrás. El valor predeterminado es 10 segundos."

    override val uisettingSeekBackwardJumpTitle = "Cantidad de rebobinado hacia atrás en la búsqueda (segundos)"

    override val uisettingPipSummary = "Si el reproductor debería entrar en modo de imagen en imagen con ventana al minimizar la aplicación. El valor predeterminado es verdadero."

    override val uisettingPipTitle = "Modo de imagen en imagen"

    override val uisettingReconnectIntervalSummary = "Cuántos segundos esperar para la reconexión en cada fallo de conexión o desconexión. El valor predeterminado es 2 segundos."

    override val uisettingReconnectIntervalTitle = "Intervalo de reconexión"

    override val uisettingResetdefaultSummary = "Restablecer todas las configuraciones anteriores a los valores predeterminados."

    override val uisettingResetdefaultTitle = "Restablecer configuración predeterminada"

    override val settingFileinfoBehaviorA = "Enviar sin procesar"

    override val settingFileinfoBehaviorB = "Enviar codificado"

    override val settingFileinfoBehaviorC = "No enviar"

    override val settingNightModeA = "APAGADO"

    override val settingNightModeB = "AUTO (Sistema)"

    override val settingNightModeC = "ENCENDIDO"

    override val en = "Inglés"

    override val ar = "Árabe"

    override val zh = "Chino"

    override val fr = "Francés"

    override val es = "Español"
}