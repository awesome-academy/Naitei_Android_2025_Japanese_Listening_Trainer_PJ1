package com.sun.japaneselisteningtrainer.ui.folder.audiolists

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sun.japaneselisteningtrainer.R
import com.sun.japaneselisteningtrainer.ui.AppViewModelProvider
import com.sun.japaneselisteningtrainer.ui.navigation.NavigationDestination

object FolderAudioListDestination : NavigationDestination {
    override val route: String = "folders"
    override val titleRes: Int = R.string.null_string
    const val folderIdArg = "folderId"
    val routeWithArgs = "$route/{$folderIdArg}"
}

@Composable
fun FolderAudioListScreen(
    modifier: Modifier = Modifier,
    viewModel: FolderAudioListViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateBar: @Composable () -> Unit
) {
    Text(text = "Folder ${viewModel.folderId}")
}
