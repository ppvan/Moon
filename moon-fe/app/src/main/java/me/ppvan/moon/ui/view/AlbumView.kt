package me.ppvan.moon.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.ppvan.moon.data.model.Album
import me.ppvan.moon.data.model.Track
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.view.home.SongList
import me.ppvan.moon.ui.view.nowplaying.NowPlayingBottomBar
import me.ppvan.moon.ui.viewmodel.AlbumViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumView(context: ViewContext,albumId: Long, albumViewModel: AlbumViewModel = hiltViewModel()) {
    val allTracks = albumViewModel.getSongsByAlbumId(albumId);
    val album = albumViewModel.getAlbumById(albumId);
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
                    )

            }
        },
        bottomBar = {
//            NowPlayingBottomBar()
        }
    )
}