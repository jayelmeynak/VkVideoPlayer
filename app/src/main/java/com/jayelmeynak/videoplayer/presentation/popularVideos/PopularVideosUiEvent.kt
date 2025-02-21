package com.jayelmeynak.videoplayer.presentation.popularVideos

import com.jayelmeynak.videoplayer.domain.models.Video

sealed class PopularVideosUiEvent {
    object SwipeRefresh : PopularVideosUiEvent()
    data class OnVideoClick(val video: Video) : PopularVideosUiEvent()
    object Retry : PopularVideosUiEvent()
}
