package me.ppvan.moon.ui.nav.graphs


import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.nav.AlbumNavGraph
import me.ppvan.moon.ui.nav.AppNavGraph
import me.ppvan.moon.ui.view.AlbumView
import me.ppvan.moon.ui.view.home.AlbumsPage

fun NavGraphBuilder.AlbumGraph(context: ViewContext) {
    navigation(
        route = AppNavGraph.Album.route,
        startDestination = AlbumNavGraph.root.route
    ) {
        composable(route = AlbumNavGraph.root.route) {
            AlbumsPage(context)
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