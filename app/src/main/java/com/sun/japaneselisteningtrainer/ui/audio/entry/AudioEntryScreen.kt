package com.sun.japaneselisteningtrainer.ui.audio.entry

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sun.japaneselisteningtrainer.R
import com.sun.japaneselisteningtrainer.ui.AppViewModelProvider
import com.sun.japaneselisteningtrainer.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object AudioEntryDestination : NavigationDestination {
    override val route = "audio_entry"
    override val titleRes = R.string.audio_entry_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioEntryScreen(
    navigateBack: () -> Unit,
    onNavigationUp: () -> Unit,
    audioEntryViewModel: AudioEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val uiState = audioEntryViewModel.uiState
    val audioForm = uiState.audioForm

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            audioEntryViewModel.updateForm(audioForm.copy(selectedFileUri = uri))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Audio") },
                navigationIcon = {
                    IconButton(onClick = { onNavigationUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTextField(
                label = "Audio Title",
                value = audioForm.title,
                onValueChange = { audioEntryViewModel.updateForm(audioForm.copy(title = it)) }
            )

            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(
                label = "Audio Script",
                value = audioForm.script,
                onValueChange = { audioEntryViewModel.updateForm(audioForm.copy(script = it)) }
            )

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = audioForm.translate,
                onValueChange = { audioEntryViewModel.updateForm(audioForm.copy(translate = it)) },
                label = { Text("Translation") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            // TODO: gọi hàm dịch tự động bằng AI
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.logoai),
                            contentDescription = "Translate automatically",
                            tint = Color.Unspecified,
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
            UploadField(
                fileUri = audioForm.selectedFileUri,
                onClickUpload = { filePickerLauncher.launch("audio/*") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        audioEntryViewModel.addAudio()
                        navigateBack()
                    }
                },
                enabled = uiState.isValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Audio")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun UploadField(
    fileUri: Uri,
    onClickUpload: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (fileUri != Uri.EMPTY) {
            Text(
                text = "Selected file: ${fileUri.lastPathSegment ?: "Unknown"}",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            Text("No file selected", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onClickUpload) {
            Text("Upload Audio")
        }
    }
}

