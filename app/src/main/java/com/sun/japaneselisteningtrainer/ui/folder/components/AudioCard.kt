package com.sun.japaneselisteningtrainer.ui.folder.components

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sun.japaneselisteningtrainer.R
import com.sun.japaneselisteningtrainer.data.model.Audio
import com.sun.japaneselisteningtrainer.ui.theme.JapaneseListeningTrainerTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AudioItem(
    modifier: Modifier = Modifier,
    info: AudioCardInfo,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onFavorite: () -> Unit,
) {
    Card(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .requiredHeightIn(max = 72.dp)
            .combinedClickable(
                onLongClick = onLongClick,
                onClick = onClick,
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlayPauseButton(
                isPlaying = info.isPlaying,
                onClick = onClick,
            )
            Spacer(Modifier.width(6.dp))
            Column {
                Text(
                    text = info.title,
                    style = MaterialTheme.typography.displayMedium,
                )
                if (info.isNew) {
                    Text(
                        text = stringResource(R.string.New),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            FavoriteIconButton(
                isFavorite = info.isFavorite,
                onClick = onFavorite
            )
        }
    }
}

@Composable
fun PlayPauseButton(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(45.dp),
            imageVector = if (isPlaying) Icons.Filled.PlayCircle else Icons.Filled.PauseCircle,
            contentDescription = null,
            tint = if (isPlaying) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
@Composable
fun FavoriteIconButton(
    isFavorite: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = null,
            tint = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview
@Composable
fun AudioItemPreview() {
    JapaneseListeningTrainerTheme {
        AudioItem(
            modifier = Modifier.fillMaxWidth(),
            info = AudioCardInfo(
                id = 1,
                title = "Title",
                isFavorite = true,
                isNew = true,
                isPlaying = true,
                duration = "4:22"
            ),
            onClick = { },
            onLongClick = { },
            onFavorite = { }
        )
    }
}

data class AudioCardInfo(
    val id: Int = 0,
    val title: String = "",
    val isFavorite: Boolean = false,
    val isNew: Boolean = true,
    val isPlaying: Boolean = false,
    val duration: String = "0:00"
)

fun Audio.toAudioCardInfo(): AudioCardInfo {
    return AudioCardInfo(
        id = id,
        title = title,
        isFavorite = isFavorite,
        isNew = (listenTimes == 0),
        isPlaying = false,
        duration = formatDuration,
    )
}
