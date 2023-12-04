package me.ppvan.moon.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.ppvan.moon.data.model.Track
import me.ppvan.moon.data.repository.TrackRepository
import me.ppvan.moon.services.PermissionEvents
import me.ppvan.moon.services.PermissionsManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackViewModel @Inject constructor(
    private val repository: TrackRepository,
    val player: MoonPlayer,
    permissionsManager: PermissionsManager,
) : ViewModel() {

    val allTracks = mutableStateListOf(Track.default())
//    var size = 10

    init {
//        observePlayerState()
        reloadTracks()

        // Load new track if we has storage permission
        permissionsManager.onUpdate.subscribe {
            when (it) {
                PermissionEvents.MEDIA_PERMISSION_GRANTED -> reloadTracks()
            }
        }
    }


    fun reloadTracks() {
        viewModelScope.launch(Dispatchers.IO) {
            val tracks = repository.findAll()
            allTracks.clear()
            allTracks.addAll(tracks)
        }
    }
}