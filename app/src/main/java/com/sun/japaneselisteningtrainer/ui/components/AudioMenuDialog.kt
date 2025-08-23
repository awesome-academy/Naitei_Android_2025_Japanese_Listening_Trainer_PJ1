package com.sun.japaneselisteningtrainer.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sun.japaneselisteningtrainer.R
import com.sun.japaneselisteningtrainer.data.repository.AudioRepository
import com.sun.japaneselisteningtrainer.ui.AppViewModelProvider
import com.sun.japaneselisteningtrainer.ui.folder.components.FolderPicker
import com.sun.japaneselisteningtrainer.ui.navigation.NavigationDestination
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

object AudioMenuDestination : NavigationDestination {
    override val route: String = "audio_menu"
    override val titleRes: Int = R.string.audio_menu_title
    const val audioIdArg = "audioId"
    val routeWithArgs = "$route/{$audioIdArg}"
    fun createRoute(audioId: Int) = "$route/$audioId"
}

@Composable
fun AudioMenuDialog(
    modifier: Modifier = Modifier,
    onEdit: (Int) -> Unit,
    viewModel: AudioMenuDialogViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onDismiss: () -> Unit,
) {
    val audioId = viewModel.audioId
    var openFolderPickerDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val menuItems = listOf(
        MenuItem(
            title = stringResource(R.string.edit),
            onClick = { onEdit(audioId) }
        ),
        MenuItem(
            title = stringResource(R.string.move_to),
            onClick = { openFolderPickerDialog = true }
        ),
        MenuItem(
            title = stringResource(R.string.delete),
            onClick = {
                coroutineScope.launch {
                    viewModel.deleteAudio(audioId)
                    onDismiss()
                }
            }
        ),
    )
    MenuDialog(
        modifier = modifier.width(300.dp),
        menuItems = menuItems,
        onDismiss = onDismiss
    )
    if (openFolderPickerDialog) {
        FolderPicker(
            onFolderSelected = {
                coroutineScope.launch {
                    viewModel.moveAudio(audioId, it.id)
                    openFolderPickerDialog = false
                    onDismiss()
                }
            },
            onDismiss = {
                openFolderPickerDialog = false
                onDismiss()
            }
        )
    }
}

class AudioMenuDialogViewModel(
    savedStateHandle: SavedStateHandle,
    private val audioRepository: AudioRepository
) : ViewModel() {
    val audioId: Int = checkNotNull(savedStateHandle[AudioMenuDestination.audioIdArg])
    suspend fun deleteAudio(audioId: Int) = audioRepository.delete(audioId)
    suspend fun moveAudio(audioId: Int, folderId: Int) {
        val audio = audioRepository.getAudioStream(audioId).first()
        audio?.let {
            audioRepository.update(it.copy(folderId = folderId))
        }
    }
}
