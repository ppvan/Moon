package me.ppvan.moon.ui.view.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import me.ppvan.moon.data.model.Track
import me.ppvan.moon.ui.activity.Routes
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.component.ArtistGrid
import me.ppvan.moon.ui.component.CenterTopAppBar
import me.ppvan.moon.ui.view.MoonPages
import me.ppvan.moon.utils.SlideTransition

@Composable
fun ArtistScreen(context: ViewContext){
    var selectedTab = context.selectedTab
    val player = context.trackViewModel.player
    val playbackState by player.playbackState.collectAsState()
    val bottomPlayerVisible = playbackState.track != Track.default()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterTopAppBar(
                title = "Artist",
                navigationIcon = {
                    IconButton(
                        onClick = { context.navigator.popBackStack()  }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                menuItems = {
                    DropdownMenuItem(
                        text = { Text("ReScan") },
                        onClick = { context.trackViewModel.reloadTracks() },
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
        },
        content = { contentPadding ->
            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                ArtistsPage(context = context)
            }
        },
        bottomBar = {
            Column {

                AnimatedContent(
                    targetState = bottomPlayerVisible,
                    label = "player",
                    transitionSpec = {
                        SlideTransition.slideUp.enterTransition()
                            .togetherWith(SlideTransition.slideDown.exitTransition())
                    }
                ) { visible ->
                    if (visible) {
                        BottomPlayer(
                            playbackState = playbackState,
                            onPausePlayClick = { player.playPause() },
                            onNextClick = { player.next() },
                            onClick = { context.navigator.navigate(Routes.NowPlaying.name) }
                        )
                    }
                }
                NavigationBar {
                    for (tab in MoonPages.values()) {
                        NavigationBarItem(
                            icon = { Icon(tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label) },
                            selected = selectedTab.value == tab,
                            onClick = {
                                context.updateSelectedTab(tab)
                                context.navigator.navigate(Routes.Home.name)
                            }
                        )
                    }
                }
            }

        }
    )
}
@Composable
fun ArtistsPage(context: ViewContext) {

    val artistViewModel = context.artistViewModel
    val allArtist = artistViewModel.allArtist
    Surface(
        modifier = Modifier
            .fillMaxSize(), color = MaterialTheme.colorScheme.surface
    ){
        ArtistGrid(context, allArtist)
    }
}