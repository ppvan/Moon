package me.ppvan.moon.ui.component

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.ppvan.moon.data.model.Playlist
import me.ppvan.moon.ui.activity.Routes
import me.ppvan.moon.ui.activity.ViewContext

@Composable
fun PlayList(
    context: ViewContext,
    playlists: List<Playlist> = List(5) { Playlist.default() },
    maxVisibleItems: Int = 5
) {

    var showNewPlaylistDialog by remember {
        mutableStateOf(false)
    }

    val currentContext = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 16.dp, top = 4.dp),
    ) {
        AddPlaylistItem(context) {
            showNewPlaylistDialog = true
        }

        if (showNewPlaylistDialog) {
            NewPlaylistDialog(onDismissRequest = {
                showNewPlaylistDialog = false
            }) {
                Log.d("INFO", "Playlist \"$it\" created")
                Toast.makeText(currentContext, "Playlist \"$it\" created", Toast.LENGTH_SHORT).show()
                context.playlistViewModel.createNewPlaylist(it)
            }
        }

        playlists.take(maxVisibleItems).forEach { playlist ->
            PlaylistItem(playlist, context) {
                context.navigator.navigate("playlist_page/${playlist.playlistId}")
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
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
