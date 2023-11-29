package me.ppvan.moon.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Song(
    @PrimaryKey(autoGenerate = true)
    val songId: Long,
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