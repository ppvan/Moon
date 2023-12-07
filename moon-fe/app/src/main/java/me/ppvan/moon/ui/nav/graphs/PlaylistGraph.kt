package me.ppvan.moon.ui.nav.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.nav.AppNavGraph
import me.ppvan.moon.ui.nav.PlaylistNavGraph
import me.ppvan.moon.ui.view.PlaylistView
import me.ppvan.moon.ui.view.home.PlaylistPage


fun NavGraphBuilder.PlaylistGraph(context: ViewContext) {
    navigation(
        route = AppNavGraph.Playlist.route,
        startDestination = PlaylistNavGraph.root.route
    ) {
        composable(route = PlaylistNavGraph.root.route) {
            PlaylistPage(context)
        }
        composable(
            route = PlaylistNavGraph.playlist.route,
            arguments = PlaylistNavGraph.playlist.arguments
        ) { backStackEntry ->
            // Lấy giá trị của tham số albumId từ URL
            val playlistId = backStackEntry.arguments?.getString("playlistId")

            // Sử dụng albumId để truy cập danh sách bài hát từ AlbumViewModel
            if (playlistId != null) {
                PlaylistView(context = context, playlistId = playlistId.toLong())
            }
        }
    }
}