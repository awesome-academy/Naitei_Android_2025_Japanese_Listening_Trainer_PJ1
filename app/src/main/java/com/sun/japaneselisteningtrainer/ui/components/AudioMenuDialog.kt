package com.sun.japaneselisteningtrainer.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sun.japaneselisteningtrainer.R
import com.sun.japaneselisteningtrainer.data.repository.AudioRepository
import com.sun.japaneselisteningtrainer.ui.AppViewModelProvider
import com.sun.japaneselisteningtrainer.ui.navigation.NavigationDestination
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
    modifier: Modifier = Modifier.width(300.dp),
    onEdit: (Int) -> Unit,
    viewModel: AudioMenuDialogViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onDismiss: () -> Unit,
) {
    val audioId = viewModel.audioId
    val coroutineScope = rememberCoroutineScope()
    val menuItems = listOf(
        MenuItem(
            title = stringResource(R.string.edit),
            onClick = { onEdit(audioId) }
        ),
        MenuItem(
            title = stringResource(R.string.move_to),
            onClick = { }
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
        modifier = modifier,
        menuItems = menuItems,
        onDismiss = onDismiss
    )
}

class AudioMenuDialogViewModel(
    savedStateHandle: SavedStateHandle,
    private val audioRepository: AudioRepository
) : ViewModel() {
    val audioId: Int = checkNotNull(savedStateHandle[AudioMenuDestination.audioIdArg])
    suspend fun deleteAudio(audioId: Int) = audioRepository.delete(audioId)
}
