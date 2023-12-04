package me.ppvan.moon.services

import android.content.ContentResolver
import android.content.ContentUris
import android.provider.MediaStore
import android.util.Log
import me.ppvan.moon.data.dao.MoonDatabase
import me.ppvan.moon.data.model.Album
import me.ppvan.moon.data.model.Artist
import me.ppvan.moon.data.model.Song
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MediaStoreSync @Inject constructor(
    database: MoonDatabase,
    private val contentResolver: ContentResolver
) {
    private val songDao = database.songDao()
    private val albumDao = database.albumDao()
    private val artistDao = database.artistDao()





    suspend fun syncWithMediaStore() {
        val songs = findAllMediaStoreSongs()
        val albums = findAllMediaAlbums()
        val artists = findAllMediaStoreArtists()

        songDao.insertAll(songs)
        albumDao.insertAll(albums)
        artistDao.insertAll(artists)
    }


    private fun findAllMediaStoreArtists(): List<Artist> {
        val artistList = mutableListOf<Artist>()

        val artistProjection = arrayOf(
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS
        )

        val artistCursor = contentResolver.query(
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
//                val songList = getSongsByArtistId(artistId)
//                val firstTrack: Track = songList[0]
                val artist = Artist(
                    id = artistId,
                    name = artistName,
                    numberOfAlbums = numberOfAlbums,
                    numberOfTracks = numberOfTracks,
                    thumbnailUri = ""
                )

                artistList.add(artist)

                Log.d("MediaStoreSync", "artist: ${artist.name}")

                it.moveToNext()
            }
        }

        return artistList
    }

    private fun findAllMediaAlbums(): List<Album> {
        val albumList = mutableListOf<Album>()
        val albumProjection = arrayOf(
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS
        )

        val albumCursor = contentResolver.query(
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

                Log.d("MediaStoreSync", "Album: ${album.name}")

                it.moveToNext()
            }
        }

        return albumList
    }
    private fun findAllMediaStoreSongs() : List<Song> {
        val songs = mutableListOf<Song>()
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.NUM_TRACKS,
            MediaStore.Audio.Albums.ALBUM_ID,
            MediaStore.Audio.Media.DATA // Path to the audio file
        )
        val cursor = contentResolver.query(
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
            val artistIdColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID)
            val albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)
            val albumIdColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID)
            val dataColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val trackNumberColumn = cursor.getColumnIndex(MediaStore.Audio.Media.NUM_TRACKS)
//            val discNumberColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DISC_NUMBER)

            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn)
                val artist = cursor.getString(artistColumn)
                val artistId = cursor.getLong(artistIdColumn)
                val album = cursor.getString(albumColumn)
                val data = cursor.getString(dataColumn)
                val albumId = cursor.getLong(albumIdColumn)
                val trackNumber = cursor.getInt(trackNumberColumn)
//                val discNumber = cursor.getInt(discNumberColumn)

                val albumArt = ContentUris.withAppendedId(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    albumId
                )

                val song = Song(
                    songId = id,
                    albumId = albumId,
                    artistId = artistId,
                    title = title,
                    artist = artist,
                    album = album,
                    content = data,
                    thumbnail = albumArt.toString(),
                    trackNumber = trackNumber,
                    discNumber = 0,
                    comment = ""
                )
                songs.add(song)

                cursor.moveToNext()
            }
        }

        songs.forEach { Log.d("INFO", it.title) }

        return songs
    }
}