package com.jayelmeynak.videoplayer.presentation.popularVideos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.jayelmeynak.videoplayer.data.local.models.VideoEntity
import com.jayelmeynak.videoplayer.data.toVideo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PopularVideosViewModel @Inject constructor(
    private val videosPager: Pager<Int, VideoEntity>,
) : ViewModel() {

    private val _refreshTrigger = MutableStateFlow(0)
    val videoPagingFlow = _refreshTrigger.flatMapLatest {
        videosPager.flow.map { pagingData ->
            pagingData.map { it.toVideo() }
        }
    }.cachedIn(viewModelScope)

    fun onEvent(event: PopularVideosUiEvent) {
        when (event) {
            is PopularVideosUiEvent.SwipeRefresh -> {
                viewModelScope.launch {
                    _refreshTrigger.value += 1
                }
            }
            is PopularVideosUiEvent.Retry -> {
                viewModelScope.launch {
                    _refreshTrigger.value += 1
                }
            }
            is PopularVideosUiEvent.OnVideoClick -> {
            }
        }
    }
}