package me.ppvan.moon.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.ppvan.moon.data.model.Artist

@Dao
interface ArtistDao {

    @Query("SELECT * FROM artist")
    fun findAll(): List<Artist>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(artist: List<Artist>)

}