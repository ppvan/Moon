package me.ppvan.moon.ui.view.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.component.AlbumGrid
import me.ppvan.moon.ui.viewmodel.AlbumViewModel

@Composable
fun AlbumsPage(context: ViewContext, albumViewModel: AlbumViewModel) {
    val allAlbums = albumViewModel.allAlbums
    Surface(
        modifier = Modifier
            .fillMaxSize(), color = MaterialTheme.colorScheme.surface
    ){
        AlbumGrid(context, allAlbums)
    }
}
