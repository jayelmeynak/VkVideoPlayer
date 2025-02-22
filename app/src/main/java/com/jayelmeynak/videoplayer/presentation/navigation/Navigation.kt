package com.jayelmeynak.videoplayer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jayelmeynak.videoplayer.presentation.navigation.Screen.Companion.ROUTE_MAIN
import com.jayelmeynak.videoplayer.presentation.popularVideos.PopularVideosScreen
import com.jayelmeynak.videoplayer.presentation.videoplayer.VideoPlayerScreen
import java.net.URLDecoder
import java.net.URLEncoder

@Composable
fun Navigation(
    navController: NavHostController,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = ROUTE_MAIN,
        modifier = modifier
    ) {
        composable(Screen.Main.route) {
            PopularVideosScreen(){ videoUrl ->
                val encodedUrl = URLEncoder.encode(videoUrl, "UTF-8")
                navController.navigate("${Screen.VideoPlayer.route}/$encodedUrl")
            }
        }

        composable(
            route = "${Screen.VideoPlayer.route}/{videoUrl}",
            arguments = listOf(navArgument("videoUrl") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString("videoUrl") ?: ""
            val decodedUrl = URLDecoder.decode(encodedUrl, "UTF-8")
            VideoPlayerScreen(videoUrl = decodedUrl)
        }
    }
}