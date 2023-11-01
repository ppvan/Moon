package me.ppvan.moon.ui.player

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import me.ppvan.moon.R
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(context: Context, val player: MoonPlayer): ViewModel() {

    val isPlaying = player.playerState.map { it ==  PlayerStates.STATE_PLAYING }.distinctUntilChanged()
    val progress = player.currentPosition.combine(player.currentSongDuration) {
        position, duration -> position.toFloat() / duration.toFloat()
    }



    init {
        val uri = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.demo)
        val mediaItem = MediaItem.fromUri(uri)
        player.initPlayer(mutableListOf(mediaItem))
    }

    fun flipPlayingState() {
        player.playPause()
    }
}