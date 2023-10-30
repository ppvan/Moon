package me.ppvan.moon.data.model

import android.provider.MediaStore.Audio.Artists

data class Song(
    val mediaId: String = "",
    val title: String = "",
    val songUrl: String = "",
    val imageUrl: String = ""
)