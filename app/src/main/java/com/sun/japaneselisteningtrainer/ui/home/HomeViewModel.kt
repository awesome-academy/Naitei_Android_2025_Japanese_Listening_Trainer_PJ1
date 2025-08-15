package com.sun.japaneselisteningtrainer.ui.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sun.japaneselisteningtrainer.data.model.Audio
import com.sun.japaneselisteningtrainer.data.repository.AudioRepository
import com.sun.japaneselisteningtrainer.data.storage.AudioFileStorage

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.io.File

class HomeViewModel(
    val audioRepository: AudioRepository,
    val audioFileStorage: AudioFileStorage
) : ViewModel() {
    suspend fun saveAudioToStorage(filename: String, uri: Uri) : Uri {
       return  audioFileStorage.save(uri)
    }

    suspend fun getAudioFromStorage(filename: String): File? {
        return audioFileStorage.get(filename)
    }
    val uiState: StateFlow<HomeUiState> =
        audioRepository.getAllAudioStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

class HomeUiState(val audioList: List<Audio> = emptyList())
