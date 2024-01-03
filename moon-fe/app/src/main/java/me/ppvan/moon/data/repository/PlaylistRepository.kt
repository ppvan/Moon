package me.ppvan.moon.data.repository

import me.ppvan.moon.data.dao.MoonDatabase
import me.ppvan.moon.data.model.Playlist
import me.ppvan.moon.data.model.PlaylistSongRef
import me.ppvan.moon.data.model.PlaylistWithSongs
import javax.inject.Inject

class PlaylistRepository @Inject constructor(database: MoonDatabase) {


    private val playlistDao = database.playlistDao()
    private val playlistSongCrossRefDao = database.playlistSongCrossRefDao()


    suspend fun findAll(): List<Playlist> {
        return playlistDao.findAll()
    }

    suspend fun getPlaylistWithSongs(playlistId: Long): PlaylistWithSongs {
        return playlistDao.getPlaylistsWithSongs(playlistId)
    }

    suspend fun findById(playlistId: Long): Playlist {
        return playlistDao.findById(playlistId)
    }

    suspend fun createPlaylist(name: String) {
        playlistDao.insert(Playlist(name = name))
    }

    suspend fun addSongToPlaylist(playlistId: Long, songId: Long) {
        playlistSongCrossRefDao.insertCrossRef(PlaylistSongRef(playlistId = playlistId, songId = songId))
    }
}


