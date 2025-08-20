package com.sun.japaneselisteningtrainer.ui.folder.audiolists

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.sun.japaneselisteningtrainer.data.folder.FolderRepository
import com.sun.japaneselisteningtrainer.data.repository.AudioRepository

class FolderAudioListViewModel(
    savedStateHandle: SavedStateHandle,
    private val folderRepository: FolderRepository,
    private val audioRepository: AudioRepository,
) : ViewModel() {
    val folderId: Int = checkNotNull(savedStateHandle[FolderAudioListDestination.folderIdArg])
}
