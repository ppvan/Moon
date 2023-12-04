package me.ppvan.moon.data.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import me.ppvan.moon.data.model.Album
import me.ppvan.moon.data.model.Artist
import me.ppvan.moon.data.model.Playlist
import me.ppvan.moon.data.model.PlaylistSongRef
import me.ppvan.moon.data.model.Song

@Database(entities = [Playlist::class, Song::class, Album::class, Artist::class, PlaylistSongRef::class], version = 5, exportSchema = false)
abstract class MoonDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
    abstract fun songDao(): SongDao

    abstract fun artistDao(): ArtistDao
    abstract fun albumDao(): AlbumDao
}
