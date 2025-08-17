package com.sun.japaneselisteningtrainer.ui.folder.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sun.japaneselisteningtrainer.data.folder.FolderRepository
import com.sun.japaneselisteningtrainer.data.model.Folder
import com.sun.japaneselisteningtrainer.ui.folder.components.FolderFormDialog.FolderFormUiState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CreateFolderViewModel(val folderRepository: FolderRepository) : ViewModel() {

    fun createFolder(folder: Folder) {
        viewModelScope.launch {
            folderRepository.add(folder)
        }
    }

}


