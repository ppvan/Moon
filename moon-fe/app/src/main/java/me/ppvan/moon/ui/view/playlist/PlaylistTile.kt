package me.ppvan.moon.ui.view.playlist

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import coil.request.ImageRequest
import me.ppvan.moon.R
import me.ppvan.moon.data.model.Playlist
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.component.SquareGrooveTile

@Composable
fun PlaylistTile(context: ViewContext, playlist: Playlist, onClick: () -> Unit) {
    SquareGrooveTile(
        image = ImageRequest.Builder(LocalContext.current)
            .data(data = playlist.thumbnail)
            .error(R.drawable.thumbnail)
            .crossfade(true)
            .build(),
        options = { _, _ ->
            {}
        },
        content = {
            Text(
                playlist.name,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
//            album.artist?.let { artistName ->
//                Text(
//                    artistName,
//                    style = MaterialTheme.typography.bodySmall,
//                    textAlign = TextAlign.Center,
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis,
//                )
//            }
        },
        onPlay = {
//            context.symphony.radio.shorty.playQueue(
//                context.symphony.groove.album.getSongIds(album.id)
//            )
        },
        onClick = {
            onClick();
        }
    )
}
