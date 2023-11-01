package me.ppvan.moon.ui.player

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import me.ppvan.moon.R
import me.ppvan.moon.data.model.Track
import me.ppvan.moon.ui.theme.MoonTheme


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomPlayer(
    playbackState: PlaybackState,
    modifier: Modifier = Modifier,
    selectedTrack: Track = Track.DEFAULT,
    onPausePlayClick: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val isPlaying = selectedTrack.state == PlayerState.STATE_PLAYING
    val progress = playbackState.progress

    Column (
    ) {
        LinearProgressIndicator(
            progress = progress,
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
        )
        Row(
            modifier = modifier
                .clickable { onClick() }
                .background(MaterialTheme.colorScheme.primaryContainer)
                .fillMaxWidth()
                .padding(6.dp, 4.dp, 6.dp, 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row (
                modifier = modifier
                    .weight(1f),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box (
                    modifier
                        .size(50.dp, 50.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .background(Color.Red)) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(selectedTrack.thumbnailUri)
                            .error(R.drawable.thumbnail)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.thumbnail),
                        contentDescription = "Dmoe"
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = selectedTrack.title, modifier = Modifier.basicMarquee(
                        animationMode = MarqueeAnimationMode.Immediately,
                        delayMillis = 1000,
                        iterations = Int.MAX_VALUE

                    ))
                    Text(text = selectedTrack.artist, style = typography.bodySmall)
                }
            }

            IconButton(modifier = Modifier.padding(4.dp), onClick = { onPausePlayClick() }) {
                if (isPlaying) {
                    Icon(imageVector = Icons.Outlined.Pause, contentDescription = "Play button")
                } else {
                    Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "Pause button")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MoonTheme {
        BottomPlayer(PlaybackState(0L, 0L))
    }
}