package me.ppvan.moon.ui.view.home

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import me.ppvan.moon.R
import me.ppvan.moon.data.model.Track
import me.ppvan.moon.ui.activity.Routes
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.viewmodel.MoonPlayer


@Composable
fun SongsPage(context: ViewContext) {

    val trackViewModel = context.trackViewModel
    val allTracks = trackViewModel.allTracks.toList()
    val player: MoonPlayer = trackViewModel.player

    Surface(
        modifier = Modifier
            .fillMaxSize(), color = MaterialTheme.colorScheme.surface
    ) {
//        Column {
//            for (track in allTracks) {
//                Text(text = track.title)
//            }
//        }
        SongList(songs = allTracks, navigator = context.navigator) {
            player.load(allTracks)
            player.preparePlay(it)
        }
    }
}


@Composable
fun SongList(
    navigator : NavHostController,
    songs: List<Track> = List(10) { Track.DEFAULT },
    onItemClick: (item: Track) -> Unit = {},
) {
    Column {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(songs, key = { track ->
                track.contentUri
            }) {song ->
                SongListItem(
                    Modifier.fillMaxWidth(),
                    track = song,
                    onClick = { onItemClick(song) },
                    navigator = navigator
                )
            }
        }
    }
}

@Composable
fun SongListItem(
    modifier: Modifier = Modifier,
    track: Track = Track.DEFAULT,
    onClick: () -> Unit = {},
    navigator: NavHostController
) {
    Column(
        modifier = modifier.clickable {
            onClick()
        },
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .background(color = MaterialTheme.colorScheme.surface),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(50.dp, 50.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(track.thumbnailUri)
                        .error(R.drawable.thumbnail)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.thumbnail),
                    contentDescription = "Music thumbnail",
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = track.title)
                Text(text = track.artist, style = MaterialTheme.typography.labelMedium)
            }
            var showOptionsMenu by remember { mutableStateOf(false) }
            IconButton(onClick = { showOptionsMenu = !showOptionsMenu }) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "More")
                SongDropdownMenu(
                    expanded = showOptionsMenu,
                    onDismissRequest = {
                        showOptionsMenu = false
                    },
                    track,
                    navigator
                )
            }

        }
    }
}
@Composable
fun SongDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    track: Track,
    navigator: NavHostController
) {

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        val context = LocalContext.current
        DropdownMenuItem(
            leadingIcon = {
                Icon(Icons.Filled.Share, null)
            },
            text = {
                Text("Share")
            },
            onClick = {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "audio/*"
                intent.putExtra(Intent.EXTRA_STREAM, track.songUri)
                context.startActivity(Intent.createChooser(intent, "Chia sẻ âm nhạc bằng"))
            }
        )
        DropdownMenuItem(
            text = { Text("Edit tags") },
            onClick = { navigator.navigate("${Routes.TagEdit.name}/${track.id}") },
            leadingIcon = {
                Icon(
                    Icons.Outlined.EditNote,
                    contentDescription = null
                )
            })
    }
}

@Preview(showBackground = true)
@Composable
fun SongsPagePreview() {
//    SongList()
}