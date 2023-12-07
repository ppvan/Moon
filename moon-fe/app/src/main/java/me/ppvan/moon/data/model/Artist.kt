package me.ppvan.moon.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Artist(
    @PrimaryKey
    val id: Long = 0,
    val name: String,
    val numberOfAlbums: Int,
    val numberOfTracks: Int,
    val thumbnailUri: String
) {
    companion object {
        fun default(): Artist {
            return Artist(
                id = 0,
                name = "Default Artist",
                numberOfTracks = 0,
                numberOfAlbums = 0,
                thumbnailUri = ""
            )
        }
    }
}