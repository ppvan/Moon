package me.ppvan.moon.data.repository

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import me.ppvan.moon.data.model.Album
import me.ppvan.moon.data.model.Artist
import me.ppvan.moon.data.model.Track
import javax.inject.Inject

class ArtistRepository @Inject constructor(@ApplicationContext val context: Context) {
    private var _allArtists = emptyList<Artist>()

    init {
        _allArtists = fetchArtistList()
    }

    fun findAllArtists(): List<Artist> {
        return _allArtists
    }

    fun invalidateArtistCache() {
        _allArtists = fetchArtistList()
    }

    private fun fetchArtistList(): List<Artist> {
        val artistList = mutableListOf<Artist>()

        val artistProjection = arrayOf(
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS
        )

        val artistCursor = context.contentResolver.query(
            MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
            artistProjection,
            null,
            null,
            null
        )

        artistCursor?.use {
            val artistIdColumn = it.getColumnIndex(MediaStore.Audio.Artists._ID)
            val artistNameColumn = it.getColumnIndex(MediaStore.Audio.Artists.ARTIST)
            val numberOfAlbumsColumn = it.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS)
            val numberOfTracksColumn = it.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS)

            it.moveToFirst()
            while (!it.isAfterLast) {
                val artistId = it.getLong(artistIdColumn)
                val artistName = it.getString(artistNameColumn)
                val numberOfAlbums = it.getInt(numberOfAlbumsColumn)
                val numberOfTracks = it.getInt(numberOfTracksColumn)

                val artistArt = ContentUris.withAppendedId(
                    MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                    artistId
                )

                val artist = Artist(
                    id = artistId,
                    name = artistName,
                    numberOfAlbums = numberOfAlbums,
                    numberOfTracks = numberOfTracks,
                    thumbnailUri = artistArt.toString()
                )

                artistList.add(artist)

                it.moveToNext()
            }
        }

        return artistList
    }
    fun getSongsByArtistId(artistId: Long): List<Track> {
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Albums.ALBUM_ID,
            MediaStore.Audio.Media.DATA
        )

        val selection = "${MediaStore.Audio.Media.ARTIST_ID} = ?"
        val selectionArgs = arrayOf(artistId.toString())

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

    fun getAlbumsByArtistId(artistId: Long): List<Album> {
        val albumList = mutableListOf<Album>()

        val albumProjection = arrayOf(
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS
        )

        val selection = "${MediaStore.Audio.Albums.ARTIST_ID} = ?"
        val selectionArgs = arrayOf(artistId.toString())

        val albumCursor = context.contentResolver.query(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            albumProjection,
            selection,
            selectionArgs,
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
    fun getArtistById(artistId: Long): Artist? {
        val artistProjection = arrayOf(
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS
        )

        val selection = "${MediaStore.Audio.Artists._ID} = ?"
        val selectionArgs = arrayOf(artistId.toString())

        val artistCursor = context.contentResolver.query(
            MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
            artistProjection,
            selection,
            selectionArgs,
            null
        )

        return artistCursor?.use {
            val artistIdColumn = it.getColumnIndex(MediaStore.Audio.Artists._ID)
            val artistNameColumn = it.getColumnIndex(MediaStore.Audio.Artists.ARTIST)
            val numberOfAlbumsColumn = it.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS)
            val numberOfTracksColumn = it.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS)

            if (it.moveToFirst()) {
                val fetchedArtistId = it.getLong(artistIdColumn)
                val artistName = it.getString(artistNameColumn)
                val numberOfAlbums = it.getInt(numberOfAlbumsColumn)
                val numberOfTracks = it.getInt(numberOfTracksColumn)

                val artistArt = ContentUris.withAppendedId(
                    MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                    fetchedArtistId
                )

                Artist(
                    id = fetchedArtistId,
                    name = artistName,
                    numberOfAlbums = numberOfAlbums,
                    numberOfTracks = numberOfTracks,
                    thumbnailUri = artistArt.toString()
                )
            } else {
                null
            }
        }
    }
    private fun getSongUri(songId: Long): Uri {
        return ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId)
    }

}