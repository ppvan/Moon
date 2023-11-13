package me.ppvan.moon.ui.view.nowplaying

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.QueueMusic
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import me.ppvan.moon.R
import me.ppvan.moon.data.model.Track
import me.ppvan.moon.ui.activity.Routes
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.theme.MoonTheme
import me.ppvan.moon.ui.viewmodel.PlaybackState
import me.ppvan.moon.ui.viewmodel.PlayerState
import me.ppvan.moon.ui.viewmodel.RepeatMode
import me.ppvan.moon.utils.DurationFormatter


@Composable
fun NowPlayingView(
    context: ViewContext
) {

    val player = context.trackViewModel.player
    val playbackState by player.playbackState.collectAsState()


    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        NowPlayingAppBar(onBackPress = {
            context.navigator.popBackStack()
        })

        NowPlayingThumbnail(playbackState.track)
//        Text(text = playbackState.toString())

        NowPlayingBottomBar(
            playbackState,
            onSlide = { position -> player.seek(position) },
            onNextClick = { player.next() },
            onFavouriteClick = {  },
            onShuffleClick = { player.shuffle() },
            onPlayQueueClick = { context.navigator.navigate(Routes.NowPlayingQueue.name) },
            onPlayPauseClick = { player.playPause() },
            onPreviousClick = { player.previous() },
            onRepeatModeClick = { player.switchRepeatMode() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackTimeSlider(
    progress: Float,
    duration: Long,
    onSlide: (Long) -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                DurationFormatter.formatMs((progress * duration).toInt()),
                style = MaterialTheme.typography.labelLarge
            )

            Text(
                DurationFormatter.formatMs(duration),
                style = MaterialTheme.typography.labelLarge
            )
        }

        Slider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp, 0.dp, 0.dp, 0.dp),
            value = progress,
//            valueRange = 0f..duration.toFloat(),
            onValueChange = { onSlide((it * duration).toLong()) },
            interactionSource = interactionSource,
            thumb = {
                SliderDefaults.Thumb(
                    //androidx.compose.material3.SliderDefaults
                    interactionSource = interactionSource,
                    thumbSize = DpSize(12.dp, 12.dp),
                    // NOTE: pad top to fix stupid layout
                    modifier = Modifier.padding(top = 4.dp),
                )
            },
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NowPlayingBottomBar(
    playbackState: PlaybackState,
    onSlide: (Long) -> Unit,
    onPlayQueueClick: () -> Unit,
    onFavouriteClick: () -> Unit,
    onRepeatModeClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onShuffleClick: () -> Unit,
) {

    val track = playbackState.track
    val isPlaying = playbackState.state == PlayerState.STATE_PLAYING
    val repeatMode = playbackState.repeatMode
    val shuffle = playbackState.shuffleMode

    val maxWidth = Modifier.fillMaxWidth()

    Column(Modifier.padding(32.dp, 0.dp)) {
        Row(
            modifier = maxWidth.height(IntrinsicSize.Max),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            IconButton(onClick = { onPlayQueueClick() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.QueueMusic,
                    contentDescription = "Icons.Outlined.QueueMusic"
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = track.title, style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.basicMarquee(
                        animationMode = MarqueeAnimationMode.Immediately,
                        delayMillis = 1000,
                        iterations = Int.MAX_VALUE
                    )
                )
                Text(text = track.artist, style = MaterialTheme.typography.bodySmall)
            }

            IconButton(onClick = { onFavouriteClick() }) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Icons.Outlined.FavoriteBorder"
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        TrackTimeSlider(
            progress = playbackState.progress,
            duration = playbackState.duration,
            onSlide = { position -> onSlide(position) }
        )
        Spacer(modifier = Modifier.height(20.dp))

        NowPlayingControls(
            playing = isPlaying,
            repeatMode = repeatMode,
            shuffle = shuffle,
            onRepeatModeClick = onRepeatModeClick,
            onPreviousClick = onPreviousClick,
            onPlayPauseClick = onPlayPauseClick,
            onNextClick = onNextClick,
            onShuffleClick = onShuffleClick
        )
    }
}


@Composable
fun NowPlayingControls(
    repeatMode: RepeatMode,
    shuffle: Boolean,
    playing: Boolean,

    onRepeatModeClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onShuffleClick: () -> Unit,
) {

    val controlsButton = Modifier
        .clip(shape = CircleShape)
        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            modifier = controlsButton,
            onClick = { onRepeatModeClick() }
        ) {
            when (repeatMode) {
                RepeatMode.OFF -> {
                    Icon(imageVector = Icons.Filled.Repeat, contentDescription = "Repeat Button")
                }

                RepeatMode.ONE -> {
                    Icon(
                        imageVector = Icons.Filled.RepeatOne,
                        contentDescription = "Repeat Button",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                RepeatMode.ALL -> {
                    Icon(
                        imageVector = Icons.Filled.Repeat,
                        contentDescription = "Repeat Button",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        IconButton(modifier = controlsButton, onClick = { onPreviousClick() }) {
            Icon(
                imageVector = Icons.Filled.SkipPrevious,
                contentDescription = "Previous Button"
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(64.dp)
                .clip(shape = CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable { onPlayPauseClick() },
        ) {
            if (playing) {
                Icon(imageVector = Icons.Filled.Pause, contentDescription = "Pause Button")
            } else {
                Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "Play Button")
            }

        }

        IconButton(modifier = controlsButton, onClick = { onNextClick() }) {
            Icon(imageVector = Icons.Filled.SkipNext, contentDescription = "Next Button")
        }

        IconButton(modifier = controlsButton, onClick = { onShuffleClick() }) {
            if (shuffle) {
                Icon(
                    imageVector = Icons.Filled.Shuffle,
                    contentDescription = "Shuffle Button",
                    tint = MaterialTheme.colorScheme.primary
                )
            } else {
                Icon(imageVector = Icons.Filled.Shuffle, contentDescription = "Shuffle Button")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingAppBar(
    onBackPress: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Now Playing",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        ),

        navigationIcon = {
            IconButton(
                onClick = {
                    onBackPress()
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
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}

@Composable
fun NowPlayingThumbnail(
    currentTrack: Track
) {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(4.dp))
//            .background(Brush.horizontalGradient(listOf(Color.Blue, Color.Green)))
            .padding(32.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(shape = RoundedCornerShape(8.dp)),
            model = ImageRequest.Builder(LocalContext.current)
                .data(currentTrack.thumbnailUri)
                .error(R.drawable.thumbnail)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.thumbnail),
            contentDescription = "Music thumbnail",
            contentScale = ContentScale.Crop
        )

    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MoonTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
//            NowPlayingView()
        }
    }
}

