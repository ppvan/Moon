package me.ppvan.moon.ui.screens.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import me.ppvan.moon.services.PermissionsManager
import me.ppvan.moon.ui.player.BottomPlayer
import me.ppvan.moon.ui.player.PlayerViewModel
import me.ppvan.moon.ui.theme.MoonTheme
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var permissionsManager: PermissionsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("PlayerViewModel", System.identityHashCode(permissionsManager).toString())
        permissionsManager.handle(this)

        setContent {
            MoonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MoonApp()
                }
            }
        }
    }
}


@Composable
fun MoonApp(
    mainViewModel: MainViewModel = hiltViewModel(),
    playerViewModel: PlayerViewModel = hiltViewModel(),
) {

    PlayerScreen(playerViewModel)
}

@Composable
fun HomeScreen() {

}

@Composable
fun PlayerScreen(viewModel: PlayerViewModel) {
    val playbackState by viewModel.currentPlaybackState.collectAsState()
    val currentTrack by viewModel.currentPlayingTrack.collectAsState()

    Column () {
        Spacer(modifier = Modifier.weight(1f))
        Text(text = playbackState.toString())
        Text(text = currentTrack.toString(), color = Color.Green)
        BottomPlayer(
            playbackState = playbackState,
            selectedTrack = currentTrack,
            onPausePlayClick = {viewModel.flipPlayingState()}
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MoonTheme {
        MoonApp()
    }
}