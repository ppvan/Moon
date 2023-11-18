package me.ppvan.moon.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import coil.request.ImageRequest
import me.ppvan.moon.R
import me.ppvan.moon.data.model.Artist
import me.ppvan.moon.ui.activity.ViewContext

@Composable
fun ArtistTile(context: ViewContext, artist: Artist, onClick: () -> Unit) {
    SquareGrooveTile(
        image = ImageRequest.Builder(LocalContext.current)
            .data(data = artist.thumbnailUri)
            .error(R.drawable.thumbnail)
            .crossfade(true)
            .build(),
        options = { expanded, onDismissRequest ->
            ArtistDropdownMenu(
                context,
                artist,
                expanded = expanded,
                onDismissRequest = onDismissRequest,
            )
        },
        content = {
            Text(
                artist.name,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        },
        onPlay = {},
        onClick = {
            onClick();
        }
    )
}

@Composable
fun ArtistDropdownMenu(
    context: ViewContext,
    artist: Artist,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
) {
    var showAddToPlaylistDialog by remember { mutableStateOf(false) }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
    ) {
        DropdownMenuItem(
            leadingIcon = {
                Icon(Icons.AutoMirrored.Filled.PlaylistPlay, null)
            },
            text = {
                Text("ShufflePlay")
            },
            onClick = {
                onDismissRequest()
            }
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon(Icons.AutoMirrored.Filled.PlaylistPlay, null)
            },
            text = {
                Text("PlayNext")
            },
            onClick = {
                onDismissRequest()
            }
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon(Icons.AutoMirrored.Filled.PlaylistPlay, null)
            },
            text = {
                Text("Add To Queue")
            },
            onClick = {
                onDismissRequest()
            }
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon(Icons.AutoMirrored.Filled.PlaylistAdd, null)
            },
            text = {
                Text("Add To Playlist")
            },
            onClick = {
                onDismissRequest()
                showAddToPlaylistDialog = true
            }
        )
    }

//    if (showAddToPlaylistDialog) {
//        AddToPlaylistDialog(
//            context,
//            songIds = context.symphony.groove.artist.getSongIds(artist.name),
//            onDismissRequest = {
//                showAddToPlaylistDialog = false
//            }
//        )
//    }
}