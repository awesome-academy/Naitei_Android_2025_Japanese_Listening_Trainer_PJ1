package com.sun.japaneselisteningtrainer.ui.home

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sun.japaneselisteningtrainer.TrainerTopAppBar
import com.sun.japaneselisteningtrainer.ui.AppViewModelProvider
import com.sun.japaneselisteningtrainer.R
import com.sun.japaneselisteningtrainer.ui.AudioFilePicker
import com.sun.japaneselisteningtrainer.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch
import kotlin.io.path.fileVisitor


object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.home_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigationBar: @Composable () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val homeUiState by homeViewModel.uiState.collectAsState()
    val couroutineScope = rememberCoroutineScope()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TrainerTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            navigationBar()
        }
    ) { innerPadding ->
       Column(
           modifier = Modifier
               .padding(innerPadding)
               .fillMaxSize()
       ) {
           var savedFileUri by remember { mutableStateOf<Uri?>(null) }
           AudioFilePicker(
               onFileSelected = { uri ->
                   couroutineScope.launch {
                       savedFileUri = homeViewModel.saveAudioToStorage("filename", uri)
                   }
               }
           )
           if (savedFileUri != null) {
               Text(text = savedFileUri.toString())
           }
       }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        navigationBar = {}
    )
}
