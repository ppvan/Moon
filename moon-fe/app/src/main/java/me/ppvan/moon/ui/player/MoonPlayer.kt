package me.ppvan.moon.ui.player

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import me.ppvan.moon.data.model.Track
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoonPlayer @Inject constructor(var player: Player) : ViewModel(), Player.Listener {


    private val scope = CoroutineScope(Dispatchers.Main + Job())

    /**
     * A state flow that emits the current playback state of the player.
     */
    private val _playerState = MutableStateFlow(PlayerState.STATE_IDLE)
    val playerState = _playerState.asStateFlow()

    /**
     * Current player position and duration.
     */
    private var _playbackState = MutableStateFlow(PlaybackState.DEFAULT)
    val playbackState = _playbackState.asStateFlow()

    private var currentIndex = 0
    private var _tracks = emptyList<Track>()


    /**
     * A thread to update playbackState async.
     */
    private var playbackJob: Job? = null

    init {
        player.addListener(this)
        player.prepare()

        scope.launch {
            playerState.collect {
                updateState(it)
                setupPlaybackJob()
            }
        }
    }

    /**
     * Set a custom player.
     * This method exists to set a MediaController to connect with MoonMediaService
     */
    fun setCustomPlayer(player: Player) {
        this.player = player

        player.addListener(this)
        player.prepare()
    }

    fun load(tracks: List<Track>) {
        _tracks = tracks.toList()
        if (player.playbackState == Player.STATE_IDLE) player.prepare()
        player.setMediaItems(tracks.map { MediaItem.fromUri(it.contentUri) })
    }

    fun preparePlay(track: Track, playWhenReady: Boolean = true) {
        if (player.playbackState == Player.STATE_IDLE) player.prepare()

        val index = _tracks.indexOf(track).let {
            if (it == -1) 0
            else it
        }
        currentIndex = index
        player.seekTo(index, 0)

        player.playWhenReady = playWhenReady

        val oldPlayBack = playbackState.value
        _playbackState.tryEmit(oldPlayBack.copy(position = 0, duration = 0, track = _tracks[index]))
    }

    fun next() {
        if (currentIndex >= _tracks.size - 1 || currentIndex < 0) {
            Log.i("INFO", "Invalid index $currentIndex")
            return
        }
//        emit signal to higher layer
        currentIndex += 1
        val oldPlayBack = playbackState.value
        _playbackState.tryEmit(oldPlayBack.copy(track = _tracks[currentIndex]))
        player.seekToNextMediaItem()
    }

    /**
     * A coroutine to update playback state for every second.
     */
    private fun setupPlaybackJob() {

        playbackJob?.cancel()

        playbackJob = scope.launch {
            do {
                val updatedState = _playbackState.value.copy(
                    position = player.currentPosition,
                    duration = player.duration
                )
                _playbackState.emit(updatedState)

                delay(1000)
            } while (_playerState.value == PlayerState.STATE_PLAYING && isActive)
        }
    }

    fun playPause() {
        if (player.playbackState == Player.STATE_IDLE) player.prepare()
        player.playWhenReady = !player.isPlaying
    }

    private suspend fun updateState(state: PlayerState) {
        val updatedState = _playbackState.value.copy(state = state)
        _playbackState.emit(updatedState)

        Log.i("INFO", state.name)
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


    /**
     * Just a layer to encapsulate our player from exoplayer types.
     */
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
    val track: Track,
    val state: PlayerState,
    val position: Long,
    val duration: Long,
) {
    val progress: Float
        get() = if (duration != 0L)
            position.toFloat() / duration
        else 0.0f

    companion object {
        val DEFAULT = PlaybackState(
            track = Track.DEFAULT,
            position = 0,
            duration = 0,
            state = PlayerState.STATE_IDLE
        )
    }

}