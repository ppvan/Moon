package me.ppvan.moon.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    //    val currentTrack = MutableStateFlow(Track.DEFAULT)
    private val _tracks = MutableStateFlow(listOf<Track>())
    val allTracks = _tracks.asStateFlow()


    init {
//        observePlayerState()

        reloadTracks()

        // Load new track if we has storage permission
        permissionsManager.onUpdate.subscribe {
            viewModelScope.launch (Dispatchers.IO) {
                when (it) {
                    PermissionEvents.MEDIA_PERMISSION_GRANTED ->  _tracks.update { repository.findAll() }
                }
            }
        }
    }

    fun reloadTracks() {
        viewModelScope.launch(Dispatchers.IO) {
            _tracks.update { repository.findAll() }
            Log.d("INFO", "Tracks Reloaded")
        }
    }
}