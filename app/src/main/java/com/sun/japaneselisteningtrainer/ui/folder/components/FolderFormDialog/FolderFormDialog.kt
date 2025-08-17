package com.sun.japaneselisteningtrainer.ui.folder.components.FolderFormDialog

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sun.japaneselisteningtrainer.R
import com.sun.japaneselisteningtrainer.data.model.Folder
import com.sun.japaneselisteningtrainer.ui.AppViewModelProvider

/**
 * Selected used to populate the form when editing a folder.
 */
@Composable
fun FolderFormDialog(
    onCancel: () -> Unit,
    onConfirm: (Folder) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "",
    selected: Folder? = null,
    viewModel: FolderFormViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.folderFormUiState

    //
    LaunchedEffect(selected) {
        viewModel.loadFolder(selected)
    }

    AlertDialog(
        onDismissRequest = { onCancel() },
        containerColor = MaterialTheme.colorScheme.surface,
        title = { Text(title) },
        text = {
            FolderInputForm(
                uiState = uiState,
                onValueChange = viewModel::updateUiState
            )
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = {
                onCancel()
                viewModel.resetUiState()
            }) {
                Text(stringResource(R.string.cancel))
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.resetUiState()
                    onConfirm(uiState.toFolder())
                },
                enabled = uiState.isEntryValid
            ) {
                Text(stringResource(R.string.ok))
            }
        }
    )
}

@Composable
fun FolderInputForm(
    uiState: FolderFormUiState,
    onValueChange: (FolderFormUiState) -> Unit,
) {
    val isError = !uiState.isEntryValid && uiState.title.isNotBlank()
    Column {
        OutlinedTextField(
            value = uiState.title,
            onValueChange = { onValueChange(uiState.copy(title = it)) },
            label = {
                if (isError) {
                    Text(
                        text = stringResource(R.string.folder_title_exists),
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    Text(stringResource(R.string.title))
                }
            },
            singleLine = true,
            isError = isError,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.description,
            onValueChange = { onValueChange(uiState.copy(description = it)) },
            label = { Text(stringResource(R.string.description)) },
            placeholder = { Text(stringResource(R.string.description_placeholder)) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
