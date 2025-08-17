package com.sun.japaneselisteningtrainer.ui.folder.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sun.japaneselisteningtrainer.R
import com.sun.japaneselisteningtrainer.ui.AppViewModelProvider
import com.sun.japaneselisteningtrainer.ui.folder.components.FolderFormDialog.FolderFormDialog


@Composable
fun CreateFolderDialog(
    onCreateConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateFolderViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    FolderFormDialog(
        title = stringResource(R.string.create_folder_title),
        onCancel = onCancel,
        onConfirm = {folder ->
            viewModel.createFolder(folder)
            onCreateConfirm()
        },
        modifier = modifier,
    )
}

