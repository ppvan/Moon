package me.ppvan.moon.ui.player

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.ppvan.moon.data.model.Track
import me.ppvan.moon.data.repository.TrackRepository
import me.ppvan.moon.utils.collectPlayerState
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(context: Context, val player: MoonPlayer, private val repository: TrackRepository): ViewModel() {

    private val _currentTrack = MutableStateFlow(Track.DEFAULT)

    val currentPlaybackState = player.playbackState
    val currentPlayingTrack = _currentTrack.asStateFlow()

    init {
        val tracks = repository.findAll()
        if (tracks.isNotEmpty()) {
            player.setUpTrack(1, true)
            _currentTrack.tryEmit(tracks.get(1))
        }

        player.initPlayer(
            tracks.map {
                MediaItem.fromUri(it.contentUri)
            }.toMutableList()
        )

        observePlayerState()
    }

    fun flipPlayingState() {
        player.playPause()
    }

    private fun updateState(state: PlayerState) {
        val currentTrack = _currentTrack.value
        _currentTrack.tryEmit(currentTrack.copy(state = state))
    }

    private fun observePlayerState() {
        viewModelScope.collectPlayerState(player, ::updateState)
    }
}