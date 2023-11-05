package me.ppvan.moon.ui.player

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import me.ppvan.moon.data.model.Track
import me.ppvan.moon.data.repository.TrackRepository
import me.ppvan.moon.services.PermissionEvents
import me.ppvan.moon.services.PermissionsManager
import javax.inject.Inject

@HiltViewModel
class TrackViewModel @Inject constructor(
    private val repository: TrackRepository,
    val player: MoonPlayer,
    permissionsManager: PermissionsManager,
) : ViewModel() {

    val currentTrack = MutableStateFlow(Track.DEFAULT)
    val allTracks: List<Track>
        get() = repository.findAll()

    init {
//        observePlayerState()

        // Load new track if we has storage permission
        permissionsManager.onUpdate.subscribe {
            when (it) {
                PermissionEvents.MEDIA_PERMISSION_GRANTED -> repository.invalidateCache()
            }
        }
    }
}