package me.ppvan.moon.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import me.ppvan.moon.data.model.PlaylistSongRef
import me.ppvan.moon.data.model.Song

@Dao
interface PlaylistSongCrossRefDao {
    @Insert
    suspend fun insertCrossRef(crossRef: PlaylistSongRef)

    @Query("SELECT * FROM song INNER JOIN PlaylistSongRef ON song.songId = PlaylistSongRef.songId WHERE PlaylistSongRef.playlistId = :playlistId")
    suspend fun getSongsForPlaylist(playlistId: Long): List<Song>
}