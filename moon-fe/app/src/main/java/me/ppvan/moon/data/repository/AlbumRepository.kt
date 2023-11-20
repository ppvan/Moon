package me.ppvan.moon.data.repository

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import me.ppvan.moon.data.model.Album
import me.ppvan.moon.data.model.Track
import me.ppvan.moon.ui.activity.ViewContext
import javax.inject.Inject

class AlbumRepository @Inject constructor(@ApplicationContext val context: Context){
    private var _all = emptyList<Album>()


    fun findAll(): List<Album> {
        return fetchAlbumList()
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
                val albumArt = ContentUris.withAppendedId(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    albumId
                )

                val album = Album(albumId, albumName, artist, numberOfSongs, albumArt.toString())

                albumList.add(album)

                it.moveToNext()
            }
        }

        return albumList
    }
    fun getSongsByAlbumId(albumId: Long): List<Track> {
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Albums.ALBUM_ID,
            MediaStore.Audio.Media.DATA
        )

        val selection = "${MediaStore.Audio.Media.ALBUM_ID} = ?"
        val selectionArgs = arrayOf(albumId.toString())

        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )

        val trackList = mutableListOf<Track>()

        cursor?.use {
            while (it.moveToNext()) {
                val idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
                val titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                val artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
                val albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)
                val albumIdColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID)
                val dataColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)

                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn)
                val artist = cursor.getString(artistColumn)
                val album = cursor.getString(albumColumn)
                val data = cursor.getString(dataColumn)
                val currentAlbumId = cursor.getLong(albumIdColumn)

                val albumArt = ContentUris.withAppendedId(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    currentAlbumId
                )
                val songUri = getSongUri(id)
                val track = Track(
                    id = id,
                    title = title,
                    artist = artist,
                    album = album,
                    thumbnailUri = albumArt.toString(),
                    contentUri = data,
                    songUri = songUri
                )
                trackList.add(track)
            }
        }

        return trackList
    }

    fun getAlbumById(albumId: Long): Album? {
        val albumProjection = arrayOf(
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS
        )

        val selection = "${MediaStore.Audio.Albums._ID} = ?"
        val selectionArgs = arrayOf(albumId.toString())

        val albumCursor = context.contentResolver.query(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            albumProjection,
            selection,
            selectionArgs,
            null
        )

        return albumCursor?.use {
            val albumIdColumn = it.getColumnIndex(MediaStore.Audio.Albums._ID)
            val albumNameColumn = it.getColumnIndex(MediaStore.Audio.Albums.ALBUM)
            val artistColumn = it.getColumnIndex(MediaStore.Audio.Albums.ARTIST)
            val numberOfSongsColumn = it.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS)


            if (it.moveToFirst()) {
                val fetchedAlbumId = it.getLong(albumIdColumn)
                val albumName = it.getString(albumNameColumn)
                val artist = it.getString(artistColumn)
                val numberOfSongs = it.getInt(numberOfSongsColumn)
                val albumArt = ContentUris.withAppendedId(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    albumId
                )

                Album(fetchedAlbumId, albumName, artist, numberOfSongs, albumArt.toString())
            } else {
                null
            }
        }
    }
    private fun getSongUri(songId: Long): Uri {
        return ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId)
    }

}