package com.sun.japaneselisteningtrainer.ui.folder.components

import android.net.Uri
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sun.japaneselisteningtrainer.data.model.Audio

@Composable
fun AudioCard(
    modifier: Modifier = Modifier,
    AudioCardInfo: AudioCardInfo
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = MaterialTheme.shapes.large
    ) {

    }
}

data class AudioCardInfo(
    val id: Int = 0,
    val title: String = "",
    val isFavorite: Boolean = false,
    val isNew: Boolean = false,
    val isPlaying: Boolean = false,
    val duration: Int = 0
)

fun Audio.toAudioCardInfo(getDuration: (String) -> Int = { 0 }): AudioCardInfo {
    return AudioCardInfo(
        id = id,
        title = title,
        isFavorite = isFavorite,
        isNew = (listenTimes == 0),
        isPlaying = false,
        duration = getDuration(filePath)
    )
}
