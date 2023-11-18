package me.ppvan.moon.ui.view.nowplaying

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import me.ppvan.moon.R
import me.ppvan.moon.data.model.Track
import me.ppvan.moon.ui.activity.ViewContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingQueue(
    context: ViewContext
) {

    val player = context.trackViewModel.player
    val currentQueue by player.currentQueue.collectAsState()
    val currentTrack by player.currentTrack.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Playing Queue",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),

                navigationIcon = {
                    IconButton(
                        onClick = {
                            context.navigator.popBackStack()
                        }
                    ) {
                        Icon(
                            Icons.Filled.ExpandMore,
                            "Back icon button",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { player.clear() }) {
                        Icon(
                            imageVector = Icons.Filled.ClearAll,
                            contentDescription = "ClearAll"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            NowPlayingQueueBody(tracks = currentQueue, currentTrack = currentTrack)
        }
    }
}

@Composable
fun NowPlayingQueueBody(
    tracks: List<Track> = List(10) { Track.DEFAULT },
    currentTrack: Track = Track.DEFAULT
) {

    LazyColumn() {
        itemsIndexed(tracks) {index, track ->
            NowPlayingQueueItem(index = index, item = track, (track == currentTrack))
        }
    }
}

@Composable
fun NowPlayingQueueItem(index: Int, item: Track, isPlaying: Boolean = false) {
    Column(
        modifier = Modifier
            .padding(8.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .background(color = MaterialTheme.colorScheme.surface),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp, 50.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "$index", style = MaterialTheme.typography.titleMedium)
            }

            Box(
                modifier = Modifier
                    .size(50.dp, 50.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.thumbnailUri)
                        .error(R.drawable.thumbnail)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.thumbnail),
                    contentDescription = "Music thumbnail",
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
//                verticalArrangement = Arrangement.SpaceBetween
            ) {
                if (isPlaying) {
                    Text(text = item.title, maxLines = 1, overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.primary)
                } else {
                    Text(text = item.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }

                Text(text = item.artist, style = MaterialTheme.typography.labelMedium)
            }

            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "More")
            }
        }
    }
}

@Preview
@Composable
fun PreviewQueue() {
    NowPlayingQueueBody()
}