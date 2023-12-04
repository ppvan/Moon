package me.ppvan.moon.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import me.ppvan.moon.data.model.Playlist
import me.ppvan.moon.data.model.PlaylistSongRef
import me.ppvan.moon.data.model.PlaylistWithSongs
import me.ppvan.moon.data.model.Song
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


@Dao
interface PlaylistDao {
    @Insert
    suspend fun insert(playlist: Playlist)

    @Query("SELECT * FROM playlist")
    suspend fun findAll(): List<Playlist>

    @Transaction
    @Query("SELECT * FROM playlist")
    fun getPlaylistsWithSongs(): List<PlaylistWithSongs>

}

@Dao
interface PlaylistSongCrossRefDao {
    @Insert
    suspend fun insertCrossRef(crossRef: PlaylistSongRef)

    @Query("SELECT * FROM song INNER JOIN PlaylistSongRef ON song.songId = PlaylistSongRef.songId WHERE PlaylistSongRef.playlistId = :playlistId")
    suspend fun getSongsForPlaylist(playlistId: Long): List<Song>
}
@Dao
interface SongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(songs: List<Song>)

    @Query("SELECT * FROM song")
    suspend fun findAll(): List<Song>

    @Query("SELECT * FROM song WHERE albumId = :albumId")
    suspend fun findAllByAlbum(albumId : Long): List<Song>

}

