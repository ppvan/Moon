package me.ppvan.moon.data.repository

import android.content.Context
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext

import me.ppvan.moon.data.model.Album
import me.ppvan.moon.data.model.Track
import javax.inject.Inject

class AlbumRepository @Inject constructor(@ApplicationContext val context: Context){
    private var _all = emptyList<Album>()

    init {
        _all = fetchAlbumList()
    }

    fun findAll(): List<Album> {
        return _all
    }

    fun invalidateCache() {
        _all = fetchAlbumList()
    }

    private fun fetchAlbumList(): List<Album> {
        val albumList = mutableListOf<Album>()
        val albumProjection = arrayOf(
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS
        )

        val albumCursor = context.contentResolver.query(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            albumProjection,
            null,
            null,
            null
        )

        albumCursor?.use {
            val albumIdColumn = it.getColumnIndex(MediaStore.Audio.Albums._ID)
            val albumNameColumn = it.getColumnIndex(MediaStore.Audio.Albums.ALBUM)
            val artistColumn = it.getColumnIndex(MediaStore.Audio.Albums.ARTIST)
            val numberOfSongsColumn = it.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS)

            it.moveToFirst()
            while (!it.isAfterLast) {
                val albumId = it.getLong(albumIdColumn)
                val albumName = it.getString(albumNameColumn)
                val artist = it.getString(artistColumn)
                val numberOfSongs = it.getInt(numberOfSongsColumn)

                val album = Album(albumId, albumName, artist, numberOfSongs)
                albumList.add(album)

                it.moveToNext()
            }
        }

        return albumList
    }


}