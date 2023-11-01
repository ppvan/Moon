package me.ppvan.moon.ui.player

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import me.ppvan.moon.R
import me.ppvan.moon.data.model.Track
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(context: Context, val player: MoonPlayer): ViewModel() {

    val currentPlaybackState = player.playbackState
    val currentPlayerState = player.playerState
    val currentPlayingTrack = MutableStateFlow(Track.DEFAULT)

    init {
        val uri1 = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.demo)
        val mediaItem1 = MediaItem.fromUri(uri1)
        val uri2 = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.sample)
        val mediaItem2 = MediaItem.fromUri(uri2)


        player.initPlayer(mutableListOf(mediaItem1, mediaItem2))
    }

    fun flipPlayingState() {
        player.playPause()
    }
}