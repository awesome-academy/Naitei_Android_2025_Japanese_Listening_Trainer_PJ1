package com.sun.japaneselisteningtrainer.ui.folder.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sun.japaneselisteningtrainer.data.folder.FolderRepository
import com.sun.japaneselisteningtrainer.data.model.Folder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CreateFolderViewModel(val folderRepository: FolderRepository) : ViewModel() {
    var createFolderUiState by mutableStateOf(CreateFolderUiState())

    fun updateUiState(title: String = createFolderUiState.title, description: String = createFolderUiState.description) {
        createFolderUiState =
            CreateFolderUiState(
                title = title,
                description = description,
            )
        validateTitleInput(title)
    }

    private fun validateTitleInput(title: String = createFolderUiState.title) {
        if (title.isBlank()) {
            createFolderUiState = createFolderUiState.copy(isEntryValid = false)
            return
        }
        viewModelScope.launch {
            val exists = isFolderTitleExists(title)
            createFolderUiState = createFolderUiState.copy(isEntryValid = !exists)
        }
    }

    private suspend fun isFolderTitleExists(title: String): Boolean {
        val folder = folderRepository.getFolderStream(title).first()
        return folder != null
    }

    fun createFolder() {
        viewModelScope.launch {
            if (createFolderUiState.isEntryValid) {
                val folder = Folder(
                    name = createFolderUiState.title,
                    description = createFolderUiState.description
                )
                folderRepository.add(folder)
            }
        }
    }
}

data class CreateFolderUiState(
    val title: String = "",
    val description: String = "",
    val isEntryValid: Boolean = false
)
