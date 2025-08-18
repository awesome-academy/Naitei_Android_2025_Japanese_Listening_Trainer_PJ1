package com.sun.japaneselisteningtrainer.ui.folder.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sun.japaneselisteningtrainer.data.folder.FolderRepository
import com.sun.japaneselisteningtrainer.data.model.Folder
import com.sun.japaneselisteningtrainer.ui.folder.components.FolderFormDialog.DESCRIPTION_MAX_LENGTH
import com.sun.japaneselisteningtrainer.ui.folder.components.FolderFormDialog.FolderFormUiState
import com.sun.japaneselisteningtrainer.ui.folder.components.FolderFormDialog.toFolder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CreateFolderViewModel(val folderRepository: FolderRepository) : ViewModel() {
    var uiState by mutableStateOf(FolderFormUiState())

    fun updateUiState(newUiState: FolderFormUiState) {
        viewModelScope.launch {
            uiState = newUiState.copy(
                isEntryValid = validateInput(newUiState),
                isTitleError = isTitleError(newUiState.title)
            )
        }
    }

    fun resetUiState() {
        uiState = FolderFormUiState()
    }

    private suspend fun validateInput(uiState: FolderFormUiState): Boolean {
        return with(uiState) {
            this.validateTitleInput()
        }
    }

    private suspend fun FolderFormUiState.validateTitleInput(): Boolean {
        val title = this.title
        val des = this.description

        return when {
            title.isBlank() -> false
            des.length > DESCRIPTION_MAX_LENGTH -> false
            title.exists() -> false
            else -> true
        }
    }

    private suspend fun String.exists() = (folderRepository.getFolderStream(this).first() != null)

    private suspend fun isTitleError(title: String): Boolean =
        title.isBlank() || title.exists()

    fun createFolder(folder: Folder = uiState.toFolder()) {
        viewModelScope.launch {
            folderRepository.add(folder)
        }
    }

}


