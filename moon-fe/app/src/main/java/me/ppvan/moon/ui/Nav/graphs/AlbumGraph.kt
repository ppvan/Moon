package me.ppvan.moon.ui.Nav.graphs


import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import me.ppvan.moon.ui.Nav.AlbumNavGraph
import me.ppvan.moon.ui.Nav.AppNavGraph
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.view.AlbumView
import me.ppvan.moon.ui.view.home.AlbumsPage
import me.ppvan.moon.ui.viewmodel.AlbumViewModel

fun NavGraphBuilder.AlbumGraph(context: ViewContext) {
    navigation(
        route = AppNavGraph.Album.route,
        startDestination = AlbumNavGraph.root.route
    ) {
        composable(route = AlbumNavGraph.root.route) {
            AlbumsPage(context, hiltViewModel())
        }
        composable(
            route = AlbumNavGraph.album.route,
            arguments = AlbumNavGraph.album.arguments
        ) { backStackEntry ->
            // Lấy giá trị của tham số albumId từ URL
            val albumId = backStackEntry.arguments?.getString("albumId")


            // Sử dụng albumId để truy cập danh sách bài hát từ AlbumViewModel
            if (albumId != null) {
                AlbumView(context = context, albumId = albumId.toLong())
            }
        }
    }
}