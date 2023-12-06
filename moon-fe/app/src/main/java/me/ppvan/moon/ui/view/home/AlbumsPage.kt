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
import me.ppvan.moon.ui.component.AlbumGrid
import me.ppvan.moon.ui.component.CenterTopAppBar
import me.ppvan.moon.utils.SlideTransition

@Composable
fun AlbumScreen(context: ViewContext){
    val player = context.trackViewModel.player
    val playbackState by player.playbackState.collectAsState()
    val bottomPlayerVisible = playbackState.track != Track.default()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterTopAppBar(
                title = "Album",
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
                AlbumsPage(context = context)
            }
        },
        bottomBar = {
            Column {
                AnimatedContent(
                    targetState = bottomPlayerVisible,
                    label = "player",
//               modifier = Modifier.navigationBarsPadding(),
                    transitionSpec = {
                        SlideTransition.slideUp.enterTransition()
                            .togetherWith(SlideTransition.slideDown.exitTransition())
                    }
                ) { visible ->
                    if (visible) {
                        Column {
                            BottomPlayer(
                                playbackState = playbackState,
                                onPausePlayClick = { player.playPause() },
                                onNextClick = { player.next() },
                                onClick = { context.navigator.navigate(Routes.NowPlaying.name) }
                            )
                        }
                    }
                }
                Spacer(
                    Modifier.windowInsetsBottomHeight(
                        WindowInsets.systemBars
                    )
                )
            }

        }
    )
}
@Composable
fun AlbumsPage(context: ViewContext) {
    val allAlbums = context.albumViewModel.albums
    Surface(
        modifier = Modifier
            .fillMaxSize(), color = MaterialTheme.colorScheme.surface
    ){
        AlbumGrid(context, allAlbums)
    }
}
