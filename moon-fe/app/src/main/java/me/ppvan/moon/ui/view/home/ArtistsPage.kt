package me.ppvan.moon.ui.view.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.component.AlbumGrid
import me.ppvan.moon.ui.component.ArtistGrid
import me.ppvan.moon.ui.viewmodel.ArtistViewModel

@Composable
fun ArtistsPage(context: ViewContext, artistViewModel: ArtistViewModel) {
    val allArtist = artistViewModel.allArtist
    Surface(
        modifier = Modifier
            .fillMaxSize(), color = MaterialTheme.colorScheme.surface
    ){
        ArtistGrid(context, allArtist)
    }
}