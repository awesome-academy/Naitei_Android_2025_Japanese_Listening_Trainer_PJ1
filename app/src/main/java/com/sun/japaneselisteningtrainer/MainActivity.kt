package com.sun.japaneselisteningtrainer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sun.japaneselisteningtrainer.ui.audio.player.AudioPlayerWithTranscriptScreen
import com.sun.japaneselisteningtrainer.ui.theme.JapaneseListeningTrainerTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val lines = listOf(
                "会社で女の人と男の人が話しています。",
                "男：男の人はこれからまず何をしますか。",
                "女：今、注文があった商品を箱に入れてるところなんですけど、手伝ってくれる？",
                "男：箱に入れるんですか。",
                "女：それはもうすぐ終わるから、できたのから配送用の宛名シールを貼っていって。"
            )
            var showLyrics by rememberSaveable { mutableStateOf(false) }
            JapaneseListeningTrainerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
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
                        japaneseVisible = showLyrics,
                        onToggleTranscript = { showLyrics = !showLyrics },
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
        }
    }
}

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

//@Preview
//@Composable
//fun TrainerAppPreview() {
//    JapaneseListeningTrainerTheme {
//        TrainerApp()
//    }
//}
