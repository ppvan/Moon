package me.ppvan.moon.ui.view.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.view.playlist.PlaylistGrid

@Composable
fun PlaylistPage(context: ViewContext) {
    Surface(
        modifier = Modifier
            .fillMaxSize(), color = MaterialTheme.colorScheme.surface
    ){
        PlaylistGrid(context = context)
    }
}