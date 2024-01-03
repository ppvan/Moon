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

    @Query("SELECT * FROM playlist WHERE playlistId = :id")
    suspend fun findById(id: Long): Playlist

    @Transaction
    @Query("SELECT * FROM playlist WHERE playlistId = :playlistId")
    suspend fun getPlaylistsWithSongs(playlistId: Long): PlaylistWithSongs

}