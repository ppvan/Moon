package me.ppvan.moon.ui.Nav.graph

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import me.ppvan.moon.ui.Nav.AppNavGraph
import me.ppvan.moon.ui.Nav.HomeNavGraph
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.view.home.AlbumsPage


// Extension
fun NavGraphBuilder.AlbumGraph(context: ViewContext){
    navigation(
        route = AppNavGraph.Home.route,
        startDestination = HomeNavGraph.root.route
    ) {
        composable(route = HomeNavGraph.root.route) {
            val parentEntry = remember(it) {
                context.navigator.getBackStackEntry(HomeNavGraph.root.route)
            }

            AlbumsPage(context = context)
        }

        composable(
            route = HomeNavGraph.playlist.route,
            arguments = HomeNavGraph.playlist.arguments
        ) {}

    }
}