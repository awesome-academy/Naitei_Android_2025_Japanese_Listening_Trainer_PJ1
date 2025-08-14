package com.sun.japaneselisteningtrainer.ui.audio.player

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.togetherWith
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.sun.japaneselisteningtrainer.TrainerTopAppBar
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sun.japaneselisteningtrainer.R
import com.sun.japaneselisteningtrainer.ui.home.HomeDestination
import com.sun.japaneselisteningtrainer.ui.navigation.NavigationDestination
import com.sun.japaneselisteningtrainer.ui.theme.JapaneseListeningTrainerTheme

/* ----------------------------- Rotating disc ------------------------------ */

@Composable
fun RotatingDisc(
    imageRes: Int,
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 200.dp,
    borderColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.outlineVariant
) {
    val infinite = rememberInfiniteTransition(label = "disc")
    val angle by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "angle"
    )

    Image(
        painter = painterResource(imageRes),
        contentDescription = "Album art",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .border(2.dp, borderColor, CircleShape)
            .rotate(if (isPlaying) angle else 0f)
    )
}

/* ------------------------------ Progress bar ------------------------------ */

@Composable
fun AudioProgressBar(
    progress: Float,                 // 0f..1f
    currentLabel: String,            // "mm:ss"
    totalLabel: String,              // "mm:ss"
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier,
    onSeekFinished: () -> Unit = {}
) {
    val cs = MaterialTheme.colorScheme
    Column(modifier = modifier.fillMaxWidth()) {
        Slider(
            value = progress.coerceIn(0f, 1f),
            onValueChange = onSeek,
            onValueChangeFinished = onSeekFinished,
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp),
            colors = SliderDefaults.colors(
                thumbColor = cs.primary,
                activeTrackColor = cs.primary,
                inactiveTrackColor = cs.onSurface.copy(alpha = 0.24f)
            )
        )
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(currentLabel, color = cs.onSurfaceVariant, fontSize = 12.sp)
            Text(totalLabel, color = cs.onSurfaceVariant, fontSize = 12.sp)
        }
    }
}

@Composable
fun AudioProgressBarMs(
    positionMs: Long,
    durationMs: Long,
    onSeekChange: (Long) -> Unit,
    onSeekFinished: () -> Unit,
    modifier: Modifier = Modifier,
    showTimeLabels: Boolean = true
) {
    val cs = MaterialTheme.colorScheme
    val progress = if (durationMs > 0) positionMs.toFloat() / durationMs else 0f
    Column(modifier = modifier.fillMaxWidth()) {
        Slider(
            value = progress.coerceIn(0f, 1f),
            onValueChange = { frac -> onSeekChange((frac * durationMs).toLong()) },
            onValueChangeFinished = onSeekFinished,
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp),
            colors = SliderDefaults.colors(
                thumbColor = cs.primary,
                activeTrackColor = cs.primary,
                inactiveTrackColor = cs.onSurface.copy(alpha = 0.24f)
            )
        )
        if (showTimeLabels) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(formatMs(positionMs), color = cs.onSurfaceVariant, fontSize = 12.sp)
                Text(formatMs(durationMs), color = cs.onSurfaceVariant, fontSize = 12.sp)
            }
        }
    }
}

private fun formatMs(ms: Long): String {
    val sec = (ms / 1000).toInt()
    val m = sec / 60
    val s = sec % 60
    return "%02d:%02d".format(m, s)
}

/* --------------------------------- Icons ---------------------------------- */

@Composable
fun RandomToggleButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            Icons.Default.Shuffle,
            contentDescription = "Shuffle",
            tint = if (enabled) cs.primary else cs.onSurface
        )
    }
}

@Composable
fun FavoriteToggleButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Favorite",
            tint = if (isFavorite) cs.error else cs.onSurface
        )
    }
}

@Composable
fun EditAudioButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            Icons.Default.Edit,
            contentDescription = "Edit audio",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

/* --------------------------- Playback controls core ----------------------- */

@Composable
fun PlaybackControls(
    isPlaying: Boolean,
    onPrevious: () -> Unit,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tint = MaterialTheme.colorScheme.onSurface
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IconButton(onClick = onPrevious) {
            Icon(Icons.Default.SkipPrevious, contentDescription = "Previous", tint = tint)
        }
        IconButton(onClick = onPlayPause) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Pause" else "Play",
                tint = tint,
                modifier = Modifier.size(44.dp)
            )
        }
        IconButton(onClick = onNext) {
            Icon(Icons.Default.SkipNext, contentDescription = "Next", tint = tint)
        }
    }
}

@Composable
fun TransportBar(
    isPlaying: Boolean,
    isShuffleOn: Boolean,
    isFavorite: Boolean,
    onToggleShuffle: () -> Unit,
    onPrevious: () -> Unit,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        RandomToggleButton(enabled = isShuffleOn, onClick = onToggleShuffle)
        PlaybackControls(
            isPlaying = isPlaying,
            onPrevious = onPrevious,
            onPlayPause = onPlayPause,
            onNext = onNext
        )
        FavoriteToggleButton(isFavorite = isFavorite, onClick = onToggleFavorite)
    }
}

@Deprecated("Use TransportBar instead")
@Composable
fun TransportControls(
    isPlaying: Boolean,
    isShuffleOn: Boolean,
    isFavorite: Boolean,
    onToggleShuffle: () -> Unit,
    onPrevious: () -> Unit,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    TransportBar(
        isPlaying = isPlaying,
        isShuffleOn = isShuffleOn,
        isFavorite = isFavorite,
        onToggleShuffle = onToggleShuffle,
        onPrevious = onPrevious,
        onPlayPause = onPlayPause,
        onNext = onNext,
        onToggleFavorite = onToggleFavorite,
        modifier = modifier
    )
}

object PlayMusicDestination : NavigationDestination {
    override val route = "Music"
    override val titleRes = R.string.track_id
}
/* --------------------------------- Screen --------------------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioPlayerWithTranscriptScreen(
    modifier: Modifier = Modifier,
    // common state
    isPlaying: Boolean,
    progress: Float,
    currentTimeLabel: String,
    totalTimeLabel: String,
    isShuffleOn: Boolean,
    isFavorite: Boolean,

    // lyrics state
    lines: List<String>,
    currentLineIndex: Int,
    currentLineProgress: Float,

    // transcript visibility
    japaneseVisible: Boolean,
    onToggleTranscript: () -> Unit,

    // callbacks
    onBack: () -> Unit,
    onOpenPicker: () -> Unit,
    onEditAudio: () -> Unit,
    onSeek: (Float) -> Unit,
    onSeekFinished: () -> Unit = {},
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onToggleShuffle: () -> Unit,
    onToggleFavorite: () -> Unit,

    // style

    albumRes: Int = R.drawable.son_tung
) {
    val cs = MaterialTheme.colorScheme
    val typo = MaterialTheme.typography

    Scaffold(
        topBar = {
            TrainerTopAppBar(
                title = stringResource(PlayMusicDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onBack,
                actions = {
                    EditAudioButton(onClick = onEditAudio)

                }
            )
        },
        containerColor = cs.background
    ) { inner ->
        // Container nhận swipe để toggle transcript
        TranscriptContainer(
            visible = true,
            onToggle = onToggleTranscript,
            modifier = modifier
                .fillMaxSize()
                .padding(inner)
        ) {
            AnimatedContent(
                targetState = japaneseVisible,
                transitionSpec = {
                    slideInHorizontally { it / 2 } + fadeIn() togetherWith
                            slideOutHorizontally { -it / 2 } + fadeOut()
                },
                label = "player-lyrics"
            ) { showLyrics ->
                if (!showLyrics) {
                    // --------------------------- PLAYER VIEW ---------------------------
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp),
                    ) {
                        // Vùng dành cho đĩa: ~55% chiều cao, canh đáy để đĩa “nhích lên”
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.40f),          // <- chỉnh tỉ lệ để đĩa cao/thấp hơn
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            RotatingDisc(
                                imageRes = albumRes,
                                isPlaying = isPlaying,
                                size = 260.dp            // có thể 260–280dp cho đẹp
                            )
                        }

                        // Vùng nội dung dưới: ~45% chiều cao
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.45f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = stringResource(PlayMusicDestination.titleRes),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.displayMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(Modifier.height(12.dp))
                            AudioProgressBar(
                                progress = progress,
                                currentLabel = currentTimeLabel,
                                totalLabel = totalTimeLabel,
                                onSeek = onSeek,
                                onSeekFinished = onSeekFinished
                            )

                            Spacer(Modifier.height(8.dp))
                            TransportBar(
                                isPlaying = isPlaying,
                                isShuffleOn = isShuffleOn,
                                isFavorite = isFavorite,
                                onToggleShuffle = onToggleShuffle,
                                onPrevious = onPrevious,
                                onPlayPause = onPlayPause,
                                onNext = onNext,
                                onToggleFavorite = onToggleFavorite
                            )
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                } else {
                    // --------------------------- LYRICS VIEW ---------------------------
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(HomeDestination.titleRes),
                            color = cs.onBackground,
                            style = typo.displayMedium,
                            maxLines = 1,
                            modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
                        )
                        LyricsBox(
                            lines = lines,
                            currentLineIndex = currentLineIndex,
                            currentLineProgress = currentLineProgress,
                            onSeekToLine = { /* map line -> time tại VM rồi call onSeek */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.58f)
                        )
                        Spacer(Modifier.height(12.dp))
                        AudioProgressBar(
                            progress = progress,
                            currentLabel = currentTimeLabel,
                            totalLabel = totalTimeLabel,
                            onSeek = onSeek,
                            onSeekFinished = onSeekFinished,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        TransportBar(
                            isPlaying = isPlaying,
                            isShuffleOn = isShuffleOn,
                            isFavorite = isFavorite,
                            onToggleShuffle = onToggleShuffle,
                            onPrevious = onPrevious,
                            onPlayPause = onPlayPause,
                            onNext = onNext,
                            onToggleFavorite = onToggleFavorite,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }
                }
            }
        }
    }
}


/* -------------------------------- Preview --------------------------------- */

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun AudioPlayerPreview() {
    // mock data
    val lines = listOf(
        "会社で女の人と男の人が話しています。",
        "男：男の人はこれからまず何をしますか。",
        "女：今、注文があった商品を箱に入れてるところなんですけど、手伝ってくれる？",
        "男：箱に入れるんですか。",
        "女：それはもうすぐ終わるから、できたのから配送用の宛名シールを貼っていって。"
    )
    JapaneseListeningTrainerTheme {
        AudioPlayerWithTranscriptScreen(
            isPlaying = true,
            progress = 0.1f,
            currentTimeLabel = "00:10",
            totalTimeLabel = "03:44",
            isShuffleOn = true,
            isFavorite = false,
            lines = lines,
            currentLineIndex = 1,
            currentLineProgress = 0.5f,
            japaneseVisible = false,
            onToggleTranscript = {},
            onBack = {},
            onOpenPicker = {},
            onEditAudio = {},
            onSeek = {},
            onSeekFinished = {},
            onPlayPause = {},
            onNext = {},
            onPrevious = {},
            onToggleShuffle = {},
            onToggleFavorite = {}
        )
    }
}
