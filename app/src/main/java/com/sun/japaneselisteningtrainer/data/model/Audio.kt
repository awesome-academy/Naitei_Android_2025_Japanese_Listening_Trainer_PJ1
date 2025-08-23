package com.sun.japaneselisteningtrainer.data.model

data class Audio(
    val id: Int = 0,
    val title: String = "",
    val folderId: Int = 0,
    val filePath: String = "",
    val duration: Long = 0,
    val script: String = "",
    val translate: String = "",
    val isSuspended: Boolean = false,
    val isFavorite: Boolean = false,
    val listenTimes: Int = 0,
    val createdAt: Long = 0
) {
    val formatCreatedAt: String
        get() = formatDateTime(createdAt)

    val formatDuration: String
        get() = formatMs(duration)
}

fun formatDateTime(millis: Long): String {
    val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(millis))
}

private fun formatMs(ms: Long): String {
    val sec = (ms / 1000).toInt()
    val m = sec / 60
    val s = sec % 60
    return "%02d:%02d".format(m, s)
}
