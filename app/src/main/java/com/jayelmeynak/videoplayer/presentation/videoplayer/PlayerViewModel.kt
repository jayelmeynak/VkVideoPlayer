package com.jayelmeynak.videoplayer.presentation.videoplayer

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_PLAYER_POSITION = "player_position"
    }

    private val _player = MutableStateFlow<ExoPlayer?>(null)
    val player: StateFlow<ExoPlayer?> = _player.asStateFlow()
    private val _videoUrl = MutableStateFlow<String?>(null)
    private val _isPlaying = MutableStateFlow(false)

    init {
        _player.value = ExoPlayer.Builder(context).build().also { player ->
            savedStateHandle.get<Long>(KEY_PLAYER_POSITION)?.let { position ->
                player.seekTo(position)
            }
        }
    }

    fun setVideoUrl(url: String) {
        if(_videoUrl.value == url) return
        _videoUrl.value = url
        preparePlayer(url)
    }

    private fun preparePlayer(videoUrl: String) {
        viewModelScope.launch {
            _player.value?.let { player ->
                val mediaItem = MediaItem.fromUri(videoUrl)
                player.setMediaItem(mediaItem)
                val savedPosition = savedStateHandle.get<Long>(KEY_PLAYER_POSITION) ?: 0L
                player.prepare()
                player.seekTo(savedPosition)
                _isPlaying.value = true
                player.play()
            }
        }
    }

    fun savePlayerState() {
        _player.value?.let { player ->
            savedStateHandle[KEY_PLAYER_POSITION] = player.currentPosition
        }
    }

    override fun onCleared() {
        // Save playback position before releasing the player.
        _player.value?.let { player ->
            savedStateHandle[KEY_PLAYER_POSITION] = player.currentPosition
        }
        releasePlayer()
        super.onCleared()
    }

    private fun releasePlayer() {
        _player.value?.release()
        _player.value = null
    }
}