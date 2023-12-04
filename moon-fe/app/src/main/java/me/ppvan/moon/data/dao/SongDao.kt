package me.ppvan.moon.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.ppvan.moon.data.model.Song


@Dao
interface SongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(songs: List<Song>)

    @Query("SELECT * FROM song")
    suspend fun findAll(): List<Song>

    @Query("SELECT * FROM song WHERE albumId = :albumId")
    suspend fun findAllByAlbum(albumId : Long): List<Song>

}
