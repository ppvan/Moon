package me.ppvan.moon.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import me.ppvan.moon.data.model.Playlist
import me.ppvan.moon.data.model.Track
import me.ppvan.moon.data.repository.TrackRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaylistViewModel @Inject constructor(
    private val trackRepository: TrackRepository,
) : ViewModel() {

    val currentPlaylist = MutableStateFlow(Playlist.default())
    val playListSongs = mutableStateListOf(Track.DEFAULT)


    init {
        playListSongs.clear()
        playListSongs.addAll(List(10) { Track.DEFAULT })
    }

    fun updatePlaylist() {

    }
}