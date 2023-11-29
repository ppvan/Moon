package me.ppvan.moon.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

@Entity(primaryKeys = ["playlistId", "songId"])
data class PlaylistSongRef(
    val playlistId: Long,
    val songId: Long
)

data class PlaylistWithSongs(
    @Embedded val playlist: Playlist,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "songId",
        associateBy = Junction(PlaylistSongRef::class)
    )
    val songs: List<Song>
)

