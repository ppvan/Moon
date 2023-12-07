package me.ppvan.moon.data.repository

import me.ppvan.moon.data.dao.MoonDatabase
import me.ppvan.moon.data.model.Album
import me.ppvan.moon.data.model.Track
import javax.inject.Inject

class AlbumRepository @Inject constructor(database: MoonDatabase){
    private val albumDao = database.albumDao()
    private val songDao = database.songDao()


    suspend fun findAll(): List<Album> {
        return fetchAlbumList()
    }

    private suspend fun fetchAlbumList(): List<Album> {
        return albumDao.findAll()
    }
    suspend fun getSongsByAlbumId(albumId: Long): List<Track> {
        val albumSongs = songDao.findAllByAlbum(albumId)
        return albumSongs.map { it.toTrack() }
    }

    suspend fun getAlbumById(albumId: Long): Album {
        return albumDao.findById(albumId)
    }
}