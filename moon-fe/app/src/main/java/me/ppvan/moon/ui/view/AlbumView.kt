package me.ppvan.moon.ui.view

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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import me.ppvan.moon.data.model.Track
import me.ppvan.moon.ui.activity.Routes
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.view.home.BottomPlayer
import me.ppvan.moon.ui.view.home.SongList
import me.ppvan.moon.ui.viewmodel.AlbumViewModel
import me.ppvan.moon.utils.SlideTransition


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumView(context: ViewContext, albumId: Long) {

    val albumViewModel: AlbumViewModel = context.albumViewModel
    val allTracks = albumViewModel.getSongsByAlbumId(albumId)
    val album = albumViewModel.getAlbumById(albumId)
    val player = context.trackViewModel.player
    val playbackState by player.playbackState.collectAsState()
    val bottomPlayerVisible = playbackState.track != Track.DEFAULT


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = { context.navigator.popBackStack()  }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                title = {
                     Text("Album" + (album?.let { " - ${it.name}" } ?: ""),)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
//                actions = {
////                    IconButtonPlaceholder()
//                },
            )
        },
        content = { contentPadding ->
            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                    SongList(
                        allTracks,
                        onItemClick = {
                            player.load(allTracks)
                            player.preparePlay(it)
                        }
                    )
            }
        },
        bottomBar = {
            AnimatedContent(
                targetState = bottomPlayerVisible,
                label = "player",
//                modifier = Modifier.,
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
                        Spacer(
                            Modifier.windowInsetsBottomHeight(
                                WindowInsets.systemBars
                            )
                        )
                    }
                }
            }
        }
    )
}