package me.ppvan.moon.ui.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import me.ppvan.moon.data.model.Track
import me.ppvan.moon.ui.activity.Routes
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.component.TopAppBarMinimalTitle
import me.ppvan.moon.ui.view.home.BottomPlayer
import me.ppvan.moon.ui.view.home.SongList
import me.ppvan.moon.ui.viewmodel.AlbumViewModel
import me.ppvan.moon.ui.viewmodel.ArtistViewModel
import me.ppvan.moon.ui.viewmodel.MoonPlayer
import me.ppvan.moon.ui.viewmodel.TrackViewModel
import me.ppvan.moon.utils.SlideTransition


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistView(context: ViewContext, artistId: Long) {
    val artistViewModel: ArtistViewModel = context.artistViewModel
    val trackViewModel: TrackViewModel = context. trackViewModel
    val artist = artistViewModel.getArtistById(artistId)
    val allSongs = artistViewModel.getSongsByAlbumId(artistId)
    val allAlbums = artistViewModel.getAlbumById(artistId)
    val player: MoonPlayer = trackViewModel.player
    val playbackState by player.playbackState.collectAsState()
    val bottomPlayerVisible = playbackState.track != Track.DEFAULT

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = { context.navigator.popBackStack() }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                title = {
                    TopAppBarMinimalTitle {
                        Text(
                            "Artist" + (artist?.let { " - ${it.name}" } ?: ""),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
                actions = {
//                    IconButtonPlaceholder()
                },
            )
        },
        content = { contentPadding ->
            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                    SongList(
                        allSongs,
                        ){
                        player.load(allSongs)
                        player.preparePlay(it)
                    }
                }

        },
        bottomBar = {
            AnimatedContent(
                targetState = bottomPlayerVisible,
                label = "player",
                modifier = Modifier.navigationBarsPadding(),
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
        }
    )
}