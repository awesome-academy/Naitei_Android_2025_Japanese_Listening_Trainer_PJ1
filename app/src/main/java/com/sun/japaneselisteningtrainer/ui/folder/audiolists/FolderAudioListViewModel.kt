package com.sun.japaneselisteningtrainer.ui.folder.audiolists

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sun.japaneselisteningtrainer.data.folder.FolderRepository
import com.sun.japaneselisteningtrainer.data.model.Folder
import com.sun.japaneselisteningtrainer.data.repository.AudioRepository
import kotlinx.coroutines.launch

class FolderAudioListViewModel(
    savedStateHandle: SavedStateHandle,
    private val folderRepository: FolderRepository,
    private val audioRepository: AudioRepository,
) : ViewModel() {
    val folderId: Int = checkNotNull(savedStateHandle[FolderAudioListDestination.folderIdArg])

    val uiState = mutableStateOf(FolderAudioListUiState())

    init {
        viewModelScope.launch {
            folderRepository.getFolderStream(folderId).collect {
                uiState.value = uiState.value.copy(folder = it)
            }
        }
    }

}

data class FolderAudioListUiState(
    val folder: Folder? = null,
)
