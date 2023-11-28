package me.ppvan.moon.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PlaylistAdd
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class AppNavGraph(
    var route: String,
    var icon: ImageVector,
    var title: String
) {
    object Home : AppNavGraph("home", Icons.Rounded.Home, "Home")
    object Album : AppNavGraph("album", Icons.Rounded.Search, "Explore")
    object Artist : AppNavGraph("artist", Icons.Rounded.Person, "Profile")
    object Playlist: AppNavGraph("playlist", Icons.Rounded.PlaylistAdd,"Album")
}

data class NavGraphItem(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
)

object HomeNavGraph {
    val root = NavGraphItem("home_main")
    val playlist = NavGraphItem(
        "home_playlist/{playlistId}",
        listOf(navArgument("playlistId") { type = NavType.StringType })
    )
}

object AlbumNavGraph {
    val root = NavGraphItem("album_page")
    val search = NavGraphItem("album_search")
    val album = NavGraphItem(
        "album_page/{albumId}",
        listOf(navArgument("albumId") { type = NavType.StringType })
    )
}

object ArtistNavGraph {
    val root = NavGraphItem("artist_page")
    val artist = NavGraphItem(
        "artist_page/{artistId}",
        listOf(navArgument("artistId") { type = NavType.StringType })
    )
}

object PlaylistNavGraph {
    val root = NavGraphItem("playlist_page")
    val search = NavGraphItem("playlist_search")
    val playlist = NavGraphItem(
        "playlist_page/{playlistId}",
        listOf(navArgument("playlistId") { type = NavType.StringType })
    )
}