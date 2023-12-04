package me.ppvan.moon.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Song(
    @PrimaryKey(autoGenerate = true)
    val songId: Long = 0,
    val albumId: Long = 0,
    val artistId: Long = 0,
    val title: String,
    val artist: String,
    val album: String,
    val thumbnail: String,
    val content: String,
    val trackNumber: Int,
    val discNumber: Int,
    val comment: String
) {
    companion object {
        fun default(): Song {
            return Song(
                songId = 0,
                albumId = 0,
                artistId = 0,
                title = "Default Title",
                artist = "Default Artist",
                album = "Default Album",
                trackNumber = 1,
                discNumber = 1,
                comment = "Default Comment",
                thumbnail = "",
                content = ""
            )
        }
    }
}