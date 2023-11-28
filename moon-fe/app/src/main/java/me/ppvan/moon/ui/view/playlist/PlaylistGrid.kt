package me.ppvan.moon.ui.view.playlist

import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import me.ppvan.moon.data.model.Playlist
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.component.IconTextBody
import me.ppvan.moon.ui.component.ResponsiveGrid


@Composable
fun PlaylistGrid(
    context: ViewContext,
    playlists: List<Playlist> = List(10) { Playlist.default() },
) {
    when {
        playlists.isEmpty() -> IconTextBody(
            icon = { modifier ->
                Icon(
                    Icons.AutoMirrored.Filled.List,
                    null,
                    modifier = modifier,
                )
            },
            content = { Text("Empty") }
        )

        else -> ResponsiveGrid {
            itemsIndexed(playlists) { _,
                                      playlist ->
                PlaylistTile(context = context, playlist = playlist) {
                    context.navigator.navigate("playlist_page/${playlist.id}")
                }
            }
        }
    }
}