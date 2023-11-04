package me.ppvan.moon.ui.screens.nowplaying

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.QueueMusic
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import me.ppvan.moon.R
import me.ppvan.moon.ui.player.PlayerViewModel
import me.ppvan.moon.ui.theme.MoonTheme
import me.ppvan.moon.utils.DurationFormatter


@Composable
fun NowPlayingScreen(
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel? = null,
    onBackPressed: () -> Unit = {}
) {

//    val selectedTrack by viewModel?.currentPlayingTrack.collectAsState()


    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        NowPlayingAppBar()

        NowPlayingThumbnail()

        NowPlayingBottomBar()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackTimeSlider() {

    var pos by remember {
        mutableFloatStateOf(0.5f)
    }

    var duration by remember {
        mutableIntStateOf(240)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                DurationFormatter.formatMs(pos.toInt()),
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
            value = pos,
            valueRange = 0f..duration.toFloat(),
            onValueChange = { pos = it },
            thumb = {
                SliderDefaults.Thumb(
                    //androidx.compose.material3.SliderDefaults
                    interactionSource = MutableInteractionSource(),
                    thumbSize = DpSize(12.dp, 12.dp),
                    // NOTE: pad top to fix stupid layout
                    modifier = Modifier.padding(top = 4.dp),
                )
            },
        )
    }
}


@Composable
fun NowPlayingBottomBar() {

    val maxWidth = Modifier.fillMaxWidth()
    val controlsButton = Modifier
        .clip(shape = CircleShape)
        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))

    Column(Modifier.padding(32.dp, 0.dp)) {
        Row(
            modifier = maxWidth,
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Outlined.QueueMusic,
                    contentDescription = "Icons.Outlined.QueueMusic"
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Lily", style = MaterialTheme.typography.titleMedium)
                Text(text = "Alan Walker", style = MaterialTheme.typography.bodySmall)
            }

            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Icons.Outlined.FavoriteBorder"
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        TrackTimeSlider()
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = maxWidth.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                modifier = controlsButton,
                onClick = { /*TODO*/ }
            ) {
                Icon(imageVector = Icons.Filled.Repeat, contentDescription = "Repeat Button")
            }

            IconButton(modifier = controlsButton, onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.SkipPrevious,
                    contentDescription = "Previous Button"
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(64.dp)
                    .clip(shape = CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable { /*TODO*/ }
                ,
            ) {
                Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "Play Button")
            }

            IconButton(modifier = controlsButton, onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.SkipNext, contentDescription = "Next Button")
            }

            IconButton(modifier = controlsButton, onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.Shuffle, contentDescription = "Shuffle Button")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingAppBar() {
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
fun NowPlayingThumbnail() {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(4.dp))
//            .background(Brush.horizontalGradient(listOf(Color.Blue, Color.Green)))
            .padding(32.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(8.dp)),
            painter = painterResource(id = R.drawable.bocchi),
            contentScale = ContentScale.Crop,
            contentDescription = null
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
            NowPlayingScreen()
        }
    }
}

