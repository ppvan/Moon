package me.ppvan.moon.ui.component

import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import me.ppvan.moon.data.model.Artist
import me.ppvan.moon.ui.activity.ViewContext

@Composable
fun ArtistGrid(
    context: ViewContext,
    artistList: List<Artist>,
) {
    when {
        artistList.isEmpty() -> IconTextBody(
            icon = { modifier ->
                Icon(
                    Icons.Filled.Person,
                    null,
                    modifier = modifier,
                )
            },
            content = { Text("Damn This Is So Empty") }
        )

        else -> ResponsiveGrid {
            itemsIndexed(
                artistList,
                key = { i, x -> "$i-$x" },
            ) { _, artist ->
                    ArtistTile(context, artist){ context.navigator.navigate("artist_page/${artist.id}") }

            }
        }
    }
}