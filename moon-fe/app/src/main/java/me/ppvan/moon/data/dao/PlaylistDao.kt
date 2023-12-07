package me.ppvan.moon.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import me.ppvan.moon.data.model.Playlist
import me.ppvan.moon.data.model.PlaylistWithSongs

@Dao
interface PlaylistDao {
    @Insert
    suspend fun insert(playlist: Playlist)

    @Query("SELECT * FROM playlist")
    suspend fun findAll(): List<Playlist>

    @Transaction
    @Query("SELECT * FROM playlist")
    fun getPlaylistsWithSongs(): List<PlaylistWithSongs>

}