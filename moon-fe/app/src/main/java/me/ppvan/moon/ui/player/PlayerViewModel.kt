package me.ppvan.moon.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.ppvan.moon.data.model.Track
import me.ppvan.moon.data.repository.TrackRepository
import me.ppvan.moon.services.PermissionEvents
import me.ppvan.moon.services.PermissionsManager
import me.ppvan.moon.utils.collectPlayerState
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val player: MoonPlayer,
    private val repository: TrackRepository,
    permissionsManager: PermissionsManager,
) : ViewModel() {

    private val _currentTrack = MutableStateFlow(Track.DEFAULT)

    val currentPlaybackState = player.playbackState
    val currentPlayingTrack = _currentTrack.asStateFlow()

    init {
        fetchSystemTracks()
        observePlayerState()

        // Load new track if we has storage permission
        permissionsManager.onUpdate.subscribe {
            when (it) {
                PermissionEvents.MEDIA_PERMISSION_GRANTED -> fetchSystemTracks()
            }
        }
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

    private fun fetchSystemTracks() {
        val tracks = repository.findAll()
        player.initPlayer(
            tracks.map {
                MediaItem.fromUri(it.contentUri)
            }.toMutableList()
        )

        return

        if (tracks.isNotEmpty()) {
            player.setUpTrack(1, true)
            _currentTrack.tryEmit(tracks[1])
        }
    }
}