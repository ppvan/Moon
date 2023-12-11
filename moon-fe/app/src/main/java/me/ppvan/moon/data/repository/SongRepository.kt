package me.ppvan.moon.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.ppvan.moon.data.dao.MoonDatabase
import me.ppvan.moon.data.model.Song
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SongRepository @Inject constructor(
    database: MoonDatabase
) {
    private val songDao = database.songDao()

    suspend fun insertAll(songs: List<Song>) {
        withContext(Dispatchers.IO) {
            songDao.insertAll(songs)
        }
    }

    suspend fun findAll(): List<Song> {
        return withContext(Dispatchers.IO) {
            songDao.findAll()
        }
    }

    suspend fun findRecentSongs(): List<Song> {
        val songIds = songDao.findRecentIds()

        return songDao.findAllByIds(songIds)
    }

}