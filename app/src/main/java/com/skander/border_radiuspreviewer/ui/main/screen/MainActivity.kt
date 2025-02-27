package com.skander.border_radiuspreviewer.ui.main.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.skander.border_radiuspreviewer.ui.theme.BorderradiusPreviewerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BorderradiusPreviewerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CornerRadiusPreviewer(
                        modifier = Modifier.padding(innerPadding)
                    )

                }
            }
        }
    }
}
