package me.ppvan.moon.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

        private val _albums = MutableStateFlow(listOf<Album>())
        val allAlbums = _albums.asStateFlow()

        init {
            permissionsManager.onUpdate.subscribe {
                when (it) {
                    PermissionEvents.MEDIA_PERMISSION_GRANTED -> repository.invalidateCache()
                }
            }

            viewModelScope.launch(Dispatchers.IO) {
                _albums.update { repository.findAll() }
            }
        }
}