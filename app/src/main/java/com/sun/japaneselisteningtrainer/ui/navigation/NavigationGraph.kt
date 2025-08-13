/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sun.japaneselisteningtrainer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sun.japaneselisteningtrainer.ui.audio.entry.AudioEntryDestination
import com.sun.japaneselisteningtrainer.ui.audio.entry.AudioEntryScreen
import com.sun.japaneselisteningtrainer.ui.home.HomeDestination
import com.sun.japaneselisteningtrainer.ui.home.HomeScreen
import com.sun.japaneselisteningtrainer.ui.audio.player.LyricsScreen
import com.sun.japaneselisteningtrainer.ui.audio.player.MusicPlayerScreen


/**
 * Provides Navigation graph for the application.
 */
@Composable
fun TrainerNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToAudioEntry = {
                    navController.navigate(AudioEntryDestination.route)
                }
            )
        }
        composable(route = AudioEntryDestination.route) {
            AudioEntryScreen(
                navigateBack = {
                    navController.popBackStack()
                },
                onNavigationUp = {
                    navController.navigateUp()
                }
            )
        }
        composable(route = MusicDestination.route) {
            // TODO: thay mock bằng state thực từ ViewModel
            MusicPlayerScreen(
                title = "耳から覚える日本語",
                isPlaying = false,
                progress = 0.12f,
                currentTimeLabel = "00:25",
                totalTimeLabel = "03:44",
                isShuffleOn = false,
                isFavorite = false,
                onBack = { navController.popBackStack() },
                onOpenPicker = { /* mở picker audio */ },
                onEditAudio = { /* điều hướng sang màn Edit nếu có */ },
                onSeek = { /* seek theo fraction */ },
                onPlayPause = { /* toggle play/pause */ },
                onNext = { /* next track */ },
                onPrevious = { /* previous track */ },
                onToggleShuffle = { /* toggle random */ },
                onToggleFavorite = { /* toggle favorite */ },
                onGoToLyrics = { navController.navigate(LyricsDestination.route) }
            )
        }
        composable(route = LyricsDestination.route) {
            // TODO: thay mock bằng state thực từ ViewModel
            LyricsScreen(
                title = "耳から覚える日本語",
                lines = listOf(
                    "会社で女の人と男の人が話しています。",
                    "男：男の人はこれからまず何をしますか。",
                    "女：今、注文があった商品を箱に入れてるところなんですけど、手伝ってくれる？"
                ),
                isPlaying = false,
                progress = 0.12f,
                currentTimeLabel = "00:25",
                totalTimeLabel = "03:44",
                isShuffleOn = false,
                isFavorite = false,
                currentLineIndex = 0,
                currentLineProgress = 0f,
                japaneseVisible = true,
                onBack = { navController.popBackStack() },
                onOpenPicker = { /* ... */ },
                onEditAudio = { /* ... */ },
                onSeek = { /* seek fraction */ },
                onSeekFinished = { /* commit seek */ },
                onPlayPause = { /* ... */ },
                onNext = { /* ... */ },
                onPrevious = { /* ... */ },
                onToggleShuffle = { /* ... */ },
                onToggleFavorite = { /* ... */ },
                onSeekToLine = { /* jump to line -> seek ms */ },
                onToggleTranscript = { /* toggle visible in VM */ }
            )
        }
    }
}

