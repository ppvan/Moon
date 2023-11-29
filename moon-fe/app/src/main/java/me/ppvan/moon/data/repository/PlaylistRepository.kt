package me.ppvan.moon.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import me.ppvan.moon.data.model.Playlist
import me.ppvan.moon.data.model.PlaylistWithSongs
import javax.inject.Inject

class PlaylistRepository @Inject constructor(database: MoonDatabase) {


    private val playlistDao = database.playlistDao()


    suspend fun findAll(): List<Playlist> {
        return playlistDao.findAll()
    }

    suspend fun createPlaylist(name: String) {
        playlistDao.insert(Playlist(name = name))
    }
}


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

