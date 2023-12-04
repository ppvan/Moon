package me.ppvan.moon.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Album(
    @PrimaryKey
    val id: Long,
    val name: String,
    val artist: String,
    val numberOfTracks: Int,
    val thumbnailUri: String,
) {
    companion object {
        fun default(): Album {
            return Album(
                id = 0,
                name = "Default album",
                artist = "Default artist",
                numberOfTracks = 0,
                thumbnailUri = ""
            )
        }
    }
}
