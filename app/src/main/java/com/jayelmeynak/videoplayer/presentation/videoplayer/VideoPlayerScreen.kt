package com.jayelmeynak.videoplayer.presentation.videoplayer

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@SuppressLint("ContextCastToActivity")
@OptIn(UnstableApi::class)
@Composable
fun VideoPlayerScreen(
    videoUrl: String,
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = LocalContext.current as? Activity
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val systemUiController = rememberSystemUiController()
    val exoPlayer by viewModel.player.collectAsState()

    LaunchedEffect(isLandscape) {
        systemUiController.isNavigationBarVisible = !isLandscape
        systemUiController.isStatusBarVisible = !isLandscape
    }

    LaunchedEffect(videoUrl) {
        viewModel.setVideoUrl(videoUrl)
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        exoPlayer?.let { exoPlayer ->
            AndroidView(
                factory = {
                    PlayerView(context).apply {
                        this.player = exoPlayer
                        useController = true
                        controllerShowTimeoutMs = 2000
                        setFullscreenButtonClickListener {
                            if (it) {
                                activity?.requestedOrientation =
                                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                            } else {
                                activity?.requestedOrientation =
                                    ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                            }
                        }
                        setFullscreenButtonState(isLandscape)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}