package me.ppvan.moon.ui.player

import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoonPlayer @Inject constructor(private val player: ExoPlayer) : Player.Listener {


    private val scope = CoroutineScope(Dispatchers.Main + Job())

    /**
    * A state flow that emits the current playback state of the player.
    */
    private val _playerState = MutableStateFlow(PlayerState.STATE_IDLE)
    val playerState = _playerState.asStateFlow()

    private var _playbackState = MutableStateFlow(PlaybackState.DEFAULT)
    val playbackState = _playbackState.asStateFlow()

    fun initPlayer(trackList: MutableList<MediaItem>) {
        player.addListener(this)
        player.setMediaItems(trackList)
        player.prepare()
        updatePlaybackJob()
    }

    /**
     * Sets up the player to start playback of the track at the specified index.
     *
     * @param index The index of the track in the playlist.
     * @param isTrackPlay If true, playback will start immediately.
     */
    fun setUpTrack(index: Int, isTrackPlay: Boolean) {
        if (player.playbackState == Player.STATE_IDLE) player.prepare()
        player.seekTo(index, 0)
        if (isTrackPlay) player.playWhenReady = true
    }

    /**
     * Seeks to the specified position in the current track.
     *
     * @param position The position to seek to, in milliseconds.
     */
    fun seekToPosition(position: Long) {
        player.seekTo(position)
    }

    /**
     * A coroutine to update playback state for every second.
     */
    private fun updatePlaybackJob() {
        scope.launch {
            do {
                _playbackState.tryEmit(PlaybackState(player.currentPosition, player.duration))
                delay(1000)
            } while (_playerState.value == PlayerState.STATE_PLAYING && isActive)
        }
    }

    fun playPause() {
        if (player.playbackState == Player.STATE_IDLE) player.prepare()
        if (player.isPlaying) {
            player.playWhenReady = false
        } else {
            if (player.playbackState == Player.STATE_READY) updatePlaybackJob()
            player.playWhenReady = true
        }
    }


    // Here is the override functions.

    /**
     * Called when a player error occurs. This implementation emits the
     * STATE_ERROR state to the playerState flow.
     */
    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        _playerState.tryEmit(PlayerState.STATE_ERROR)
    }

    /**
     * Called when the player's playWhenReady state changes. This implementation
     * emits the STATE_PLAYING or STATE_PAUSE state to the playerState flow
     * depending on the new playWhenReady state and the current playback state.
     */
    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        if (player.playbackState == Player.STATE_READY) {
            if (playWhenReady) {
                _playerState.tryEmit(PlayerState.STATE_PLAYING)
            } else {
                _playerState.tryEmit(PlayerState.STATE_PAUSE)
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
            _playerState.tryEmit(PlayerState.STATE_NEXT_TRACK)
            _playerState.tryEmit(PlayerState.STATE_PLAYING)
        }
    }


    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            Player.STATE_IDLE -> {
                _playerState.tryEmit(PlayerState.STATE_IDLE)
            }

            Player.STATE_BUFFERING -> {
                _playerState.tryEmit(PlayerState.STATE_BUFFERING)
            }

            Player.STATE_READY -> {
                _playerState.tryEmit(PlayerState.STATE_READY)
                if (player.playWhenReady) {
                    _playerState.tryEmit(PlayerState.STATE_PLAYING)
                } else {
                    _playerState.tryEmit(PlayerState.STATE_PAUSE)
                }
            }

            Player.STATE_ENDED -> {
                _playerState.tryEmit(PlayerState.STATE_END)
            }
        }
    }

}

enum class PlayerState {
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


data class PlaybackState(
    val currentPosition: Long,
    val currentTrackDuration: Long,
) {
    val progress: Float
        get() = if (currentTrackDuration != 0L)
            currentPosition.toFloat() / currentTrackDuration
        else 0.0f

    companion object {
        val DEFAULT = PlaybackState(0L, 0L)
    }

}