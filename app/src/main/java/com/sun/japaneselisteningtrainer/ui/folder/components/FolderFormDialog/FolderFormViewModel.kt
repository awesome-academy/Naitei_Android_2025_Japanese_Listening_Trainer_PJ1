package com.sun.japaneselisteningtrainer.ui.folder.components.FolderFormDialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sun.japaneselisteningtrainer.data.folder.FolderRepository
import com.sun.japaneselisteningtrainer.data.model.Folder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FolderFormViewModel (private val folderRepository: FolderRepository) : ViewModel() {
    var folderFormUiState by mutableStateOf(FolderFormUiState())

    fun updateUiState(uiState: FolderFormUiState) {
        folderFormUiState =
            FolderFormUiState(
                title = uiState.title,
                description = uiState.description,
            )
        validateTitleInput(uiState.title)
    }

    fun resetUiState() {
        folderFormUiState = FolderFormUiState()
    }
    private fun validateTitleInput(title: String = folderFormUiState.title) {
        if (title.isBlank()) {
            folderFormUiState = folderFormUiState.copy(isEntryValid = false)
            return
        }
        viewModelScope.launch {
            val exists = isFolderTitleExists(title)
            folderFormUiState = folderFormUiState.copy(isEntryValid = !exists)
        }
    }

    private suspend fun isFolderTitleExists(title: String): Boolean {
        val folder = folderRepository.getFolderStream(title).first()
        return folder != null
    }

    fun loadFolder(selected: Folder?) {
        if (selected != null) {
            folderFormUiState = FolderFormUiState(
                title = selected.name,
                description = selected.description
            )
        }

    }
}

data class FolderFormUiState(
    val title: String = "",
    val description: String = "",
    val isEntryValid: Boolean = false
)

fun FolderFormUiState.toFolder(): Folder = Folder(
    name = title,
    description = description
)


