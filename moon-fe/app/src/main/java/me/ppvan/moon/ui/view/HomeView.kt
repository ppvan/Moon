package me.ppvan.moon.ui.view

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import me.ppvan.moon.ui.activity.Routes
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.component.CenterTopAppBar
import me.ppvan.moon.ui.view.home.AlbumsPage
import me.ppvan.moon.ui.view.home.ArtistsPage
import me.ppvan.moon.ui.view.home.BottomPlayer
import me.ppvan.moon.ui.view.home.PlaylistPage
import me.ppvan.moon.ui.view.home.SearchPage
import me.ppvan.moon.ui.view.home.SongsPage
import me.ppvan.moon.ui.viewmodel.AlbumViewModel
import me.ppvan.moon.ui.viewmodel.TrackViewModel
import me.ppvan.moon.ui.viewmodel.YTViewModel
import me.ppvan.moon.utils.ScaleTransition
import me.ppvan.moon.utils.SlideTransition


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    context: ViewContext,
    trackViewModel: TrackViewModel = hiltViewModel(),
    albumViewModel: AlbumViewModel = hiltViewModel(),
    ytViewModel: YTViewModel = hiltViewModel()

) {

    val player = trackViewModel.player
    var selectedTab by remember { mutableStateOf(MoonPages.Song) }
    val playbackState by player.playbackState.collectAsState()



    Scaffold(
        topBar = {
            Crossfade(targetState = selectedTab, label = "top-bar-page") { page ->
                CenterTopAppBar(
                    title = page.label,
                    navigationIcon = {

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
            Column {
                BottomPlayer(
                    playbackState = playbackState,
                    onPausePlayClick = { player.playPause() },
                    onNextClick = { player.next() }
                )
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
                MoonPages.Song -> SongsPage(trackViewModel)
                MoonPages.Album -> AlbumsPage(albumViewModel)
                MoonPages.Search -> SearchPage(ytViewModel)
                MoonPages.Artist -> ArtistsPage()
                MoonPages.Playlist -> PlaylistPage()

                else -> {
                    Text(text = "Not implemented")
                }
            }
        }
    }
}

enum class MoonPages constructor(val label: String, val icon: ImageVector) {
    Song("Song", Icons.Filled.MusicNote),
    Album("Album", Icons.Filled.Album),
    Search("Search", Icons.Filled.Search),
    Artist("Artist", Icons.Filled.People),
    Playlist("Playlist", Icons.AutoMirrored.Filled.QueueMusic)
    ;
}