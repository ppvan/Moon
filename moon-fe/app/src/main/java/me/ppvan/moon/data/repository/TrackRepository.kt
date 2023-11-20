package me.ppvan.moon.data.repository

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import me.ppvan.moon.data.model.Track
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackRepository @Inject constructor(@ApplicationContext val context: Context) {

    private var _all = emptyList<Track>()

    fun findAll(): List<Track> {
        return fetchTrackList()
    }

    fun findTrackById(mediaId: String): Track {

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Albums.ALBUM_ID,
            MediaStore.Audio.Media.DATA // Path to the audio file
        )
        val selection = "${MediaStore.Audio.Media._ID} = ?"
        val selectionArgs = arrayOf(mediaId)

        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
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
            if (!cursor.isAfterLast) {
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

                return Track(
                    id = id,
                    title = title,
                    artist = artist,
                    album = album,
                    thumbnailUri = albumArt.toString(),
                    contentUri = data
                )
            }
        }

        return Track.DEFAULT
    }


    fun save(track: Track) {
        // Step 1: Query the MediaStore to find the media item by its _ID
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val id = track.id
        val updateUri: Uri = ContentUris.withAppendedId(uri, id)

        val values = ContentValues().apply {
            put(MediaStore.Audio.Media.IS_PENDING, 1)
        }
        context.contentResolver.update(updateUri, values, null, null)

        values.run {
            clear()
            put(MediaStore.MediaColumns.TITLE, track.title)
            put(MediaStore.Audio.Media.IS_PENDING, 0)
        }
        val affected = context.contentResolver.update(updateUri, values, null, null)

        Log.d("INFO", "$affected")
    }
    fun invalidateCache() {
        _all = fetchTrackList()
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

                val songUri = getSongUri(id) // Gọi hàm để lấy Uri của bài hát

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

                cursor.moveToNext()
            }
        }

        return trackList
    }

    /**
     * Get the Uri of a song using its ID.
     */
    private fun getSongUri(songId: Long): Uri {
        return ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId)
    }
}
