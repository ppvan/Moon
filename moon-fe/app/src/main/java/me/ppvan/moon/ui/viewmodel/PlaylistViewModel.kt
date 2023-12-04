package me.ppvan.moon.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.ppvan.moon.data.model.Playlist
import me.ppvan.moon.data.model.Track
import me.ppvan.moon.data.repository.PlaylistRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaylistViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository,
) : ViewModel() {

    val currentPlaylist = MutableStateFlow(Playlist.default())
    val playListSongs = mutableStateListOf(Track.default())
    val playlists = mutableStateListOf<Playlist>()


    init {
        playListSongs.clear()
        playListSongs.addAll(List(10) { Track.default() })
        updatePlaylists()
    }

    private fun updatePlaylists() {
        playlists.clear()
        viewModelScope.launch(Dispatchers.IO) {
            val dbPlaylists = playlistRepository.findAll()
            Log.d("PlaylistViewModel", dbPlaylists.joinToString { it.name })
            withContext(Dispatchers.Main) {
                playlists.addAll(dbPlaylists)
            }
        }
    }

    fun updatePlaylist() {

    }

    fun createNewPlaylist(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistRepository.createPlaylist(name)
        }
        updatePlaylists()
    }
}