package me.ppvan.moon.data.repository

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import me.ppvan.moon.data.dao.MoonDatabase
import me.ppvan.moon.data.model.Album
import me.ppvan.moon.data.model.Track
import javax.inject.Inject

class AlbumRepository @Inject constructor(database: MoonDatabase){
    private var _all = emptyList<Album>()

    private val albumDao = database.albumDao()
    private val songDao = database.songDao()


    fun findAll(): List<Album> {
        return fetchAlbumList()
    }

    fun invalidateCache() {
        _all = fetchAlbumList()
    }

    private fun fetchAlbumList(): List<Album> {
        return albumDao.findAll()
    }
    suspend fun getSongsByAlbumId(albumId: Long): List<Track> {
        val albumSongs = songDao.findAllByAlbum(albumId)
        return albumSongs.map { it.toTrack() }
    }

    suspend fun getAlbumById(albumId: Long): Album {
        return albumDao.findById(albumId)
    }
    private fun getSongUri(songId: Long): Uri {
        return ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId)
    }

}