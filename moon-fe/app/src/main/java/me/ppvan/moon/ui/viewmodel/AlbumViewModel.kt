package me.ppvan.moon.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.ppvan.moon.data.model.Album
import me.ppvan.moon.data.repository.AlbumRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlbumViewModel @Inject constructor(
    private val repository: AlbumRepository,
    val player: MoonPlayer
) : ViewModel() {

    val albums = mutableStateListOf<Album>()
    private val _currentAlbum = MutableStateFlow(Album.default())
    val currentAlbum = _currentAlbum.asStateFlow()
    val currentSongs = currentAlbum.map { repository.getSongsByAlbumId(it.id) }

    init {

        viewModelScope.launch(Dispatchers.IO) {
            val localAlbums = repository.findAll()

            withContext(Dispatchers.Main) {
                albums.addAll(localAlbums)
            }
        }
    }

    fun onCurrentAlbumChanged(albumId: Long) {
        viewModelScope.launch (Dispatchers.IO) {
            val localAlbums = repository.getAlbumById(albumId)

            withContext(Dispatchers.Main) {
                _currentAlbum.update { localAlbums }
            }
        }
    }
}