package me.ppvan.moon.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.ppvan.moon.data.model.Album
import me.ppvan.moon.data.repository.AlbumRepository
import me.ppvan.moon.services.PermissionEvents
import me.ppvan.moon.services.PermissionsManager
import javax.inject.Inject

@HiltViewModel

class AlbumViewModel @Inject constructor(
    private val repository: AlbumRepository,
    val player: MoonPlayer,
    permissionsManager: PermissionsManager,
): ViewModel() {

        val allAlbums: List<Album>
        get() = repository.findAll()

        init {
            permissionsManager.onUpdate.subscribe {
                when (it) {
                    PermissionEvents.MEDIA_PERMISSION_GRANTED -> repository.invalidateCache()
                }
            }
        }
}