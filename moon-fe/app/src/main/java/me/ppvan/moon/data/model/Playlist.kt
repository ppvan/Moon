package me.ppvan.moon.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long = 0,
    @ColumnInfo(name = "name")
    val name: String
) {
    companion object {
        fun default() : Playlist {
            return Playlist(
                playlistId = 123,
                name = "My Default playlist"
            )
        }
    }

    val thumbnail: String
        get() = ""
//        get() {
//            return if (songs.isEmpty()) {
//                ""
//            } else {
//                songs.first().thumbnailUri
//            }
//        }
}

