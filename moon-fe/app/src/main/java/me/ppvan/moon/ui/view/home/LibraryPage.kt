package me.ppvan.moon.ui.view.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.component.AlbumList
import me.ppvan.moon.ui.component.LibraryRow
import me.ppvan.moon.ui.component.PlaylistAlbumTabRow
import me.ppvan.moon.ui.component.SongRow
import me.ppvan.moon.ui.viewmodel.MoonPlayer


@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun LibraryPage(context: ViewContext) {
    val trackViewModel = context.trackViewModel
    val allTracks = trackViewModel.allTracks.toList()
    val player: MoonPlayer = trackViewModel.player
    Surface(
        modifier = Modifier
            .fillMaxSize(), color = MaterialTheme.colorScheme.surface
    ){
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ){
            item {
                LibraryRow(context)
            }
            item{
                SideHeading {
                    Text("Song")
                }
            }
            item{
                SongRow(tracks = allTracks, context = context)
            }
            item {
                PlaylistAlbumTabRow(context = context)
            }


        }


    }
}

@Composable
private fun SideHeading(text: @Composable () -> Unit) {
    Box(modifier = Modifier.padding(20.dp, 0.dp)) {
        ProvideTextStyle(MaterialTheme.typography.titleMedium) {
            text()
        }
    }
}
