package me.ppvan.moon.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.ppvan.moon.data.model.Album
import me.ppvan.moon.data.model.Playlist
import me.ppvan.moon.ui.activity.Routes
import me.ppvan.moon.ui.activity.ViewContext

@Composable
fun PlayList(
    context: ViewContext,
    playlists: List<Playlist> = List(10) { Playlist.default() },
    maxVisibleItems: Int = 5
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 16.dp, top = 4.dp),
    ) {
        AddPlaylistItem(context){

        }

        playlists.take(maxVisibleItems).forEach { playlist ->
           PlaylistItem(playlist, context) {

            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ){
        Box(
            modifier = Modifier
                .border(1.dp, Color.LightGray.copy(0.7f), RoundedCornerShape(30.dp))
                .clickable {
                    context.navigator.navigate(Routes.Playlist.name)
                }
                .width(120.dp)
        ) {
            Text(
                "Xem thêm",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 2.dp)
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}
