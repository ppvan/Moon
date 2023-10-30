package me.ppvan.moon.ui.player

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.ppvan.moon.data.model.Song
import javax.inject.Inject


@HiltViewModel
class PlayerViewModel @Inject constructor(): ViewModel() {

    val currentSong: Song = Song("MediaID", "Title", "url", "url2")
    val isPlaying = false
    val message = "Hello world"
}