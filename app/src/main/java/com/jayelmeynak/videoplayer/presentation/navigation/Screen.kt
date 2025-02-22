package com.jayelmeynak.videoplayer.presentation.navigation

sealed class Screen(
    val route: String
) {
    object Main : Screen(ROUTE_MAIN)
    object VideoPlayer : Screen(ROUTE_VIDEO_PLAYER)

    companion object{
        const val ROUTE_VIDEO_PLAYER = "video_player"
        const val ROUTE_MAIN = "main"
    }
}