package me.ppvan.moon.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.ppvan.moon.data.model.RecentSong
import me.ppvan.moon.data.model.Song


@Dao
abstract class SongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(songs: List<Song>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertRecent(recentSong: RecentSong)

    suspend fun insertRecentAutoNow(songId: Long) {
        val now = System.currentTimeMillis()
        val recentSong = RecentSong(songId, now)
        insertRecent(recentSong)
    }

    @Query("SELECT * FROM song")
    abstract suspend fun findAll(): List<Song>

    @Query("SELECT songId FROM recentsong ORDER BY lastPlayed DESC")
    abstract suspend fun findRecentIds(): List<Long>

    @Query("SELECT * FROM song WHERE songId IN (:songIds)")
    abstract suspend fun findAllByIds(songIds: List<Long>): List<Song>

    @Query("SELECT * FROM song WHERE albumId = :albumId")
    abstract suspend fun findAllByAlbum(albumId : Long): List<Song>

    @Query("SELECT * FROM song WHERE content = :content LIMIT 1")
    abstract suspend fun findByContentUri(content: String): Song

}
