package com.sun.japaneselisteningtrainer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sun.japaneselisteningtrainer.ui.theme.JapaneseListeningTrainerTheme

class MainActivity : ComponentActivity() {
    
    private var currentIntent by mutableStateOf<Intent?>(null)
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        currentIntent = intent
        
        setContent {
            JapaneseListeningTrainerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TrainerApp(notificationIntent = currentIntent)
                }
            }
        }
    }
    
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        currentIntent = intent
    }
}

@Preview
@Composable
fun TrainerAppPreview() {
    JapaneseListeningTrainerTheme {
        TrainerApp(notificationIntent = null)
    }
}
