package me.ppvan.moon.ui.component

import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import me.ppvan.moon.data.model.Album
import me.ppvan.moon.data.model.Track
import me.ppvan.moon.ui.activity.ViewContext


@Composable
fun AlbumGrid(
    context: ViewContext,
    albumList: List<Album> = List(10) { Album.DEFAULT },
){
    when {
        albumList.isEmpty() -> IconTextBody(
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
            itemsIndexed(
                albumList,
                key = { i, x -> "$i-$x" },
                //contentType = { _, _ -> GrooveKinds.ALBUM }

            ) { _,
                //albumId ->
//                context.symphony.groove.album.get(albumId)?.let { album ->
//                    AlbumTile(context, album)
//                }

                album-> AlbumTile(album, context) { context.navigator.navigate("album_page/${album.id}") }
            }
        }
    }
}