package com.sun.japaneselisteningtrainer.ui.folder

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sun.japaneselisteningtrainer.data.folder.FolderRepository
import com.sun.japaneselisteningtrainer.data.model.Folder
import com.sun.japaneselisteningtrainer.ui.folder.create.CreateFolderUiState
import com.sun.japaneselisteningtrainer.ui.home.HomeUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FolderListViewModel(private val folderRepository: FolderRepository) : ViewModel() {
    val folderListUiState: StateFlow<FolderListUiState> =
        folderRepository.getAllFolderStream().map { FolderListUiState(it) }
            .onEach { state ->
                Log.d("MyTag", "New FolderListUiState: $state")
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = FolderListUiState()
            )

    var createFolderUiState by mutableStateOf(CreateFolderUiState())
        private set




    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class FolderListUiState(
    val folderList: List<Folder> = listOf(),
)


