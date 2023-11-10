package me.ppvan.moon.ui.view.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import me.ppvan.moon.ui.component.AlbumGrid
import me.ppvan.moon.ui.viewmodel.AlbumViewModel

@Composable
fun AlbumsPage(albumViewModel: AlbumViewModel) {
    val allAlbums by albumViewModel.allAlbums.collectAsState()
    Surface(
        modifier = Modifier
            .fillMaxSize(), color = MaterialTheme.colorScheme.surface
    ){
        AlbumGrid(allAlbums)
    }
}