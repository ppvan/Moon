package me.ppvan.moon.ui.player

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import me.ppvan.moon.R
import javax.inject.Inject


@HiltViewModel
class PlayerViewModel @Inject constructor(context: Context, val player: MoonPlayer): ViewModel() {

    val isPlaying = player.playerState.map { it ==  PlayerStates.STATE_PLAYING }.distinctUntilChanged()


    init {
        val uri = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.demo)
        val mediaItem = MediaItem.fromUri(uri)
        player.initPlayer(mutableListOf(mediaItem))
    }

    fun flipPlayingState() {
        player.playPause()
    }
}