package me.ppvan.moon.ui.player

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class MoonPlayer @Inject constructor(private val player: ExoPlayer) : Player.Listener {

    /**
    * A state flow that emits the current playback state of the player.
    */
    val playerState = MutableStateFlow(PlayerStates.STATE_IDLE)

    /**
     * The current playback position in milliseconds. If the player's position
     * is negative, this returns 0.
     */
    val currentPlaybackPosition: Long
        get() = if (player.currentPosition > 0) player.currentPosition else 0L

    /**
     * The duration of the current track in milliseconds. If the track's duration
     * is negative, this returns 0.
     */
    val currentTrackDuration: Long
        get() = if (player.duration > 0) player.duration else 0L

    fun initPlayer(trackList: MutableList<MediaItem>) {
        player.addListener(this)
        player.setMediaItems(trackList)
        player.prepare()
    }

    fun playPause() {
        if (player.playbackState == Player.STATE_IDLE) player.prepare()
        player.playWhenReady = !player.playWhenReady


        Log.i("INFO", player.currentPosition.toString() + "/" + player.duration.toString())
    }


    /**
     * Called when a player error occurs. This implementation emits the
     * STATE_ERROR state to the playerState flow.
     */
    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        playerState.tryEmit(PlayerStates.STATE_ERROR)
    }

    /**
     * Called when the player's playWhenReady state changes. This implementation
     * emits the STATE_PLAYING or STATE_PAUSE state to the playerState flow
     * depending on the new playWhenReady state and the current playback state.
     */
    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        if (player.playbackState == Player.STATE_READY) {
            if (playWhenReady) {
                playerState.tryEmit(PlayerStates.STATE_PLAYING)
            } else {
                playerState.tryEmit(PlayerStates.STATE_PAUSE)
            }
        }
    }

    /**
     * Called when the player transitions to a new media item. This implementation
     * emits the STATE_NEXT_TRACK and STATE_PLAYING states to the playerState flow
     * if the transition was automatic.
     */
    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO) {
            playerState.tryEmit(PlayerStates.STATE_NEXT_TRACK)
            playerState.tryEmit(PlayerStates.STATE_PLAYING)
        }
    }


    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            Player.STATE_IDLE -> {
                playerState.tryEmit(PlayerStates.STATE_IDLE)
            }

            Player.STATE_BUFFERING -> {
                playerState.tryEmit(PlayerStates.STATE_BUFFERING)
            }

            Player.STATE_READY -> {
                playerState.tryEmit(PlayerStates.STATE_READY)
                if (player.playWhenReady) {
                    playerState.tryEmit(PlayerStates.STATE_PLAYING)
                } else {
                    playerState.tryEmit(PlayerStates.STATE_PAUSE)
                }
            }

            Player.STATE_ENDED -> {
                playerState.tryEmit(PlayerStates.STATE_END)
            }
        }
    }

}

enum class PlayerStates {
    /**
     * State when the player is idle, not ready to play.
     */
    STATE_IDLE,

    /**
     * State when the player is ready to start playback.
     */
    STATE_READY,

    /**
     * State when the player is buffering content.
     */
    STATE_BUFFERING,

    /**
     * State when the player has encountered an error.
     */
    STATE_ERROR,

    /**
     * State when the playback has ended.
     */
    STATE_END,

    /**
     * State when the player is actively playing content.
     */
    STATE_PLAYING,

    /**
     * State when the player has paused the playback.
     */
    STATE_PAUSE,

    /**
     * State when the player has moved to the next track.
     */
    STATE_NEXT_TRACK
}