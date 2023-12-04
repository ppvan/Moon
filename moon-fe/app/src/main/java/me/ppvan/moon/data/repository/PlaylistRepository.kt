package me.ppvan.moon.data.repository

import me.ppvan.moon.data.dao.MoonDatabase
import me.ppvan.moon.data.model.Playlist
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


