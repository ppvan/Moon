package me.ppvan.moon.data.repository

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import me.ppvan.moon.data.model.Track
import javax.inject.Inject

class TrackRepository @Inject constructor(@ApplicationContext val context: Context) {

    private var _all = emptyList<Track>()

    init {
        _all = fetchTrackList()
    }

    fun findAll(): List<Track> {
        return fetchTrackList()
    }

    /**
     * Fetch track list from MediaStore API.
     *
     * Note: Check read external storage permission
     */
    private fun fetchTrackList(): List<Track> {
        val trackList = mutableListOf<Track>()
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Albums.ALBUM_ID,
            MediaStore.Audio.Media.DATA // Path to the audio file
        )
        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            MediaStore.Audio.Media.IS_MUSIC + " = 1",
            null,
            null
        )

        cursor?.use {
            val idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)
            val albumIdColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID)
            val dataColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)

            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn)
                val artist = cursor.getString(artistColumn)
                val album = cursor.getString(albumColumn)
                val data = cursor.getString(dataColumn)
                val albumId = cursor.getLong(albumIdColumn)

                val albumArt = ContentUris.withAppendedId(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    albumId
                )

                val track = Track(
                    id = id,
                    title = title,
                    artist = artist,
                    album = album,
                    thumbnailUri = albumArt.toString(),
                    contentUri = data
                )
                trackList.add(track)

                cursor.moveToNext()
            }
        }

        return trackList
    }
}