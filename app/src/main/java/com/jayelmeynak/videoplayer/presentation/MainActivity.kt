package com.jayelmeynak.videoplayer.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.jayelmeynak.videoplayer.presentation.videoplayer.VideoPlayerScreen
import com.jayelmeynak.videoplayer.ui.theme.VideoPlayerTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VideoPlayerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    VideoPlayerScreen(
                        "https://videos.pexels.com/video-files/1526909/1526909-hd_1920_1080_24fps.mp4",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
