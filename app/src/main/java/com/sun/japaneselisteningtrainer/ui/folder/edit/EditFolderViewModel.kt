package com.sun.japaneselisteningtrainer.ui.folder.edit

import androidx.lifecycle.ViewModel
import com.sun.japaneselisteningtrainer.data.folder.FolderRepository
import com.sun.japaneselisteningtrainer.data.model.Folder

class EditFolderViewModel(val folderRepository: FolderRepository) : ViewModel()  {
    suspend fun editFolder(folder: Folder) {
        folderRepository.update(folder)
    }

    fun loadFolder(selected: Folder) {

    }
}
