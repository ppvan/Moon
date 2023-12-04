package me.ppvan.moon.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.ppvan.moon.data.model.Album


@Dao
interface AlbumDao {

    @Query("SELECT * FROM album")
    fun findAll(): List<Album>

    @Query("SELECT * FROM album WHERE id = :id")
    fun findById(id: Long): Album

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(albums: List<Album>)
}