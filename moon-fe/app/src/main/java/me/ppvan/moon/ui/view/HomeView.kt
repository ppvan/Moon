package me.ppvan.moon.ui.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import me.ppvan.moon.ui.activity.Routes
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.component.CenterTopAppBar
import me.ppvan.moon.ui.view.home.AlbumsPage
import me.ppvan.moon.ui.view.home.ArtistsPage
import me.ppvan.moon.ui.view.home.PlaylistPage
import me.ppvan.moon.ui.view.home.SongsPage
import me.ppvan.moon.utils.ScaleTransition
import me.ppvan.moon.utils.SlideTransition


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(context: ViewContext) {
    var presses by remember { mutableIntStateOf(0) }
    var selectedTab by remember { mutableStateOf(MoonPages.Song) }

    Scaffold(
        topBar = {
            Crossfade(targetState = selectedTab, label = "top bar-page") { page ->
                CenterTopAppBar(
                    title = page.label,
                    navigationIcon = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = "Search"
                            )
                        }
                    },
                    menuItems = {
                        DropdownMenuItem(
                            text = { Text("ReScan") },
                            onClick = { /* Handle edit! */ },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Refresh,
                                    contentDescription = "ReScan"
                                )
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Setting") },
                            onClick = { context.navigator.navigate(route = Routes.Settings.name) },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Settings,
                                    contentDescription = "Settings"
                                )
                            }
                        )
                    }
                )
            }
        },
        bottomBar = {
            NavigationBar {
                for (tab in MoonPages.values()) {
                    NavigationBarItem(
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) },
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab }
                    )
                }
            }
        }
    ) { innerPadding ->
        AnimatedContent(
            targetState = selectedTab,
            label = "home-page",
            modifier = Modifier.padding(innerPadding),
            transitionSpec = {
                SlideTransition.slideUp.enterTransition()
                    .togetherWith(ScaleTransition.scaleDown.exitTransition())
            }
        ) { page ->
            when (page) {
                MoonPages.Song -> SongsPage()
                MoonPages.Album -> AlbumsPage()
                MoonPages.Artist -> ArtistsPage()
                MoonPages.Playlist -> PlaylistPage()
            }
        }
    }
}

enum class MoonPages constructor(val label: String, val icon: ImageVector) {
    Song("Song", Icons.Filled.MusicNote),
    Album("Album", Icons.Filled.Album),
    Artist("Artist", Icons.Filled.People),
    Playlist("Playlist", Icons.Filled.QueueMusic)
    ;
}