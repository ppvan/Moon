package me.ppvan.moon.data.model

import android.provider.MediaStore.Audio.Artists
import me.ppvan.moon.ui.player.PlayerStates

data class Song(
    val mediaId: String = "",
    val title: String = "",
    val songUrl: String = "",
    val imageUrl: String = "",
    var state: PlayerStates = PlayerStates.STATE_IDLE
)