package me.ppvan.moon.ui.Nav.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import me.ppvan.moon.ui.Nav.AlbumNavGraph
import me.ppvan.moon.ui.Nav.AppNavGraph
import me.ppvan.moon.ui.Nav.ArtistNavGraph
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.view.AlbumView
import me.ppvan.moon.ui.view.ArtistView

fun NavGraphBuilder.ArtistGraph(context: ViewContext) {
    navigation(
        route = AppNavGraph.Artist.route,
        startDestination = ArtistNavGraph.root.route
    ) {
        composable(
            route = ArtistNavGraph.artist.route,
            arguments = ArtistNavGraph.artist.arguments
        ) { backStackEntry ->
            val artistId = backStackEntry.arguments?.getString("artistId")
            if (artistId != null) {
                ArtistView(context = context, artistId = artistId.toLong())
            }
        }
    }
}