package me.ppvan.moon.ui.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import me.ppvan.moon.data.model.Track
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class

MoonPlayer @Inject constructor(var player: Player) : ViewModel(), Player.Listener {


    private val scope = CoroutineScope(Dispatchers.Main + Job())

    /**
     * A state flow that emits the current playback state of the player.
     */
    private val _playerState = MutableStateFlow(PlayerState.STATE_IDLE)
    private val playerState = _playerState.asStateFlow()

    /**
     * Current player position and duration.
     */
    private var _playbackState = MutableStateFlow(PlaybackState.DEFAULT)
    val playbackState = _playbackState.asStateFlow()

    private var _currentTrack = MutableStateFlow(Track.DEFAULT)
    val currentTrack = playbackState.map { it.track }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), _currentTrack.value)

    private var _currentQueue = MutableStateFlow(emptyList<Track>())
    val currentQueue = playbackState.map { getShuffleQueue(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), _currentQueue.value)


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

    private fun getShuffleQueue(playbackState: PlaybackState) : List<Track> {
        val timeline = player.currentTimeline

        val shuffledOrder = mutableListOf<Int>()
        var index = timeline.getFirstWindowIndex(playbackState.shuffleMode)
        while (index != C.INDEX_UNSET) {
            shuffledOrder.add(index)
            index = timeline.getNextWindowIndex(index, Player.REPEAT_MODE_OFF, playbackState.shuffleMode)
        }

        return shuffledOrder.map { i ->
            Track.fromMediaItem(player.currentTimeline.getWindow(i, Timeline.Window()).mediaItem)
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
//        _currentQueue.update { tracks }

        if (player.playbackState == Player.STATE_IDLE) player.prepare()
        player.setMediaItems(tracks.map {
            MediaItem.fromUri(it.contentUri)

            val metadata = MediaMetadata.Builder()
                .setTitle(it.title)
                .setAlbumTitle(it.album)
                .setArtist(it.artist)
                .setArtworkUri(Uri.parse(it.thumbnailUri))
                .build()

            MediaItem.Builder()
                .setMediaId(it.contentUri)
                .setMediaMetadata(metadata)
                .setUri(it.contentUri)
                .build()
        })
    }

    fun addUrlItem(url: String) {
        player.addMediaItem(0, MediaItem.fromUri(url))
    }

    fun isPlaying(): Boolean {
        return _playerState.value == PlayerState.STATE_PLAYING
    }

    fun preparePlayAtIndex(index: Int, playWhenReady: Boolean) {
        if (index != -1) {
            player.seekTo(index, 0)
        } else {
            player.seekTo(0, 0)
        }


        player.playWhenReady = playWhenReady
    }

    fun preparePlay(track: Track, playWhenReady: Boolean = true) {
        if (player.playbackState == Player.STATE_IDLE) player.prepare()
        val tracks = getShuffleQueue(playbackState.value)

        val index = tracks.indexOf(track)
        preparePlayAtIndex(index, playWhenReady)
    }

    fun next() {
//        val allTracks = _tracks.value
//        val index = _currentIndex.value
//
//        if (index >= allTracks.size - 1 || index < 0) {
//            Log.i("INFO", "Invalid index $index")
//            return
//        }
////        emit signal to higher layer
//        _currentIndex.update { index + 1 }

//        val oldPlayBack = playbackState.value
//        _playbackState.tryEmit(oldPlayBack.copy(track = allTracks[index + 1]))
        player.seekToNextMediaItem()
    }

    fun previous() {
//        val allTracks = _tracks.value
//        val index = _currentIndex.value
//        if (index <= 0) {
//            return
//        }

//        _currentIndex.update { index - 1 }

//        val oldPlayBack = playbackState.value
//        _playbackState.tryEmit(oldPlayBack.copy(track = allTracks[index - 1]))
        player.seekToPreviousMediaItem()
    }

    fun seek(position: Long) {
        val playbackState = _playbackState.value
        val updated = playbackState.copy(
            position = position
        )

        _playbackState.update { updated }
        player.seekTo(position)
    }

    fun switchRepeatMode() {
        val oldPlayBack = _playbackState.value

        val newMode = when (oldPlayBack.repeatMode) {
            RepeatMode.OFF -> RepeatMode.ALL
            RepeatMode.ONE -> RepeatMode.OFF
            RepeatMode.ALL -> RepeatMode.ONE
        }

        _playbackState.update { oldPlayBack.copy(repeatMode = newMode) }
    }

    fun shuffle() {
        val oldPlayBack = _playbackState.value
        val newShuffle = !oldPlayBack.shuffleMode

        _playbackState.update { oldPlayBack.copy(shuffleMode = newShuffle) }
        player.shuffleModeEnabled = newShuffle
    }

    fun clear() {

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

    private fun updateState(state: PlayerState) {
        val updatedState = _playbackState.value.copy(state = state)
        _playbackState.update { updatedState }

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
        if (mediaItem?.mediaMetadata == null) {
            return
        }

        val track = Track.fromMediaItem(mediaItem)
        val oldPlayback = _playbackState.value
        _playbackState.tryEmit(oldPlayback.copy(track = track))
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
    val shuffleMode: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.OFF
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

enum class RepeatMode(val id: Int) {
    OFF(0),
    ONE(1),
    ALL(2);
}