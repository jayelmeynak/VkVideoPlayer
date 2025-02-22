package com.jayelmeynak.videoplayer.presentation.popularVideos

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.jayelmeynak.videoplayer.R
import com.jayelmeynak.videoplayer.domain.models.Video
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopularVideosScreen(
    modifier: Modifier = Modifier,
    viewModel: PopularVideosViewModel = hiltViewModel(),
    onVideoClick: (String) -> Unit
) {
    val videos = viewModel.videoPagingFlow.collectAsLazyPagingItems()
    val context = LocalContext.current

    val refreshing = videos.loadState.refresh is LoadState.Loading
    val pullRefreshState = rememberPullRefreshState(refreshing, onRefresh = {
        viewModel.onEvent(PopularVideosUiEvent.SwipeRefresh)
    })
    LaunchedEffect(key1 = videos.loadState) {
        if (videos.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                "Error: " + (videos.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        if (videos.loadState.refresh is LoadState.Loading && videos.itemCount == 0) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(
                    count = videos.itemCount
                ) { index ->
                    val video = videos[index]
                    if (video != null) {
                        VideoListItem(
                            video = video,
                            onVideoClick = {
                                viewModel.onEvent(
                                    PopularVideosUiEvent.OnVideoClick(video)
                                )
                                onVideoClick(video.videoUrl)
                            }
                        )
                    }
                }
                videos.apply {
                    when (loadState.append) {
                        is LoadState.Loading -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }

                        is LoadState.Error -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Error loading more videos. Tap to retry.",
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .clickable { viewModel.onEvent(PopularVideosUiEvent.Retry) },
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }

                        else -> Unit
                    }
                }
            }
        }
        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun VideoListItem(
    video: Video,
    onVideoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onVideoClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
        ) {
            AsyncImage(
                model = video.imageUrl,
                contentDescription = "Video thumbnail",
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.fillMaxSize(),
                placeholder = painterResource(id = R.drawable.placeholder),
                error = painterResource(id = R.drawable.error_placeholder)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Author: ${video.userName}", style = MaterialTheme.typography.titleMedium)
    }
}

