package me.ppvan.moon.data.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import me.ppvan.moon.data.model.Playlist
import me.ppvan.moon.data.model.PlaylistSongRef
import me.ppvan.moon.data.model.Song

@Database(entities = [Playlist::class, Song::class, PlaylistSongRef::class], version = 2, exportSchema = false)
abstract class MoonDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
    abstract fun songDao(): SongDao
}
