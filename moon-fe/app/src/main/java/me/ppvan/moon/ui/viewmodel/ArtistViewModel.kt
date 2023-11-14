package me.ppvan.moon.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.ppvan.moon.data.model.Album
import me.ppvan.moon.data.model.Artist
import me.ppvan.moon.data.model.Track
import me.ppvan.moon.data.repository.ArtistRepository
import me.ppvan.moon.services.PermissionEvents
import me.ppvan.moon.services.PermissionsManager
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val repository: ArtistRepository,
    val player: MoonPlayer,
    permissionsManager: PermissionsManager,
) : ViewModel() {
    private val _albums = MutableStateFlow(listOf<Artist>())

    val allArtist: List<Artist>
        get() = repository.findAllArtists()

    init {
        permissionsManager.onUpdate.subscribe {
            when (it) {
                PermissionEvents.MEDIA_PERMISSION_GRANTED -> repository.invalidateArtistCache()
            }

            viewModelScope.launch(Dispatchers.IO) {
                _albums.update { repository.findAllArtists() }
            }
        }
    }
    fun getSongsByAlbumId(artistId: Long): List<Track> {
        return repository.getSongsByArtistId(artistId)
    }

    fun getAlbumById(artistId: Long): List<Album> {
        return repository.getAlbumsByArtistId(artistId);
    }
    fun getArtistById(artistId: Long): Artist? {
        return repository.getArtistById(artistId)
    }

}