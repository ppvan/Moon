package me.ppvan.moon.ui.player

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import me.ppvan.moon.R
import me.ppvan.moon.di.AppModule
import me.ppvan.moon.ui.theme.MoonTheme


@Composable
fun BottomPlayer(viewmodel: PlayerViewModel = hiltViewModel(), modifier: Modifier = Modifier) {

    val isPlaying by viewmodel.isPlaying.collectAsState(false)

    Column (
    ) {
        LinearProgressIndicator(
            progress = 0.5f,
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
        )
        Row(
            modifier = modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .fillMaxWidth()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Box (
                    modifier
                        .size(50.dp, 50.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .background(Color.Red)) {
                    Image(painter = painterResource(id = R.drawable.bocchi), contentDescription = "Song thumbnail")
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Row (
                modifier = modifier
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(text = "By My Side")
                    Text(text = "Alan Walker", style = typography.bodySmall)
                }


                IconButton(onClick = { viewmodel.flipPlayingState(); }) {
                    if (isPlaying) {
                        Icon(imageVector = Icons.Outlined.Pause, contentDescription = "Play button")
                    } else {
                        Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "Pause button")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MoonTheme {
        BottomPlayer()
    }
}