package me.ppvan.moon.ui.view.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.component.AlbumGrid

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
