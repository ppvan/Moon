package me.ppvan.moon.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecentSong(
    @PrimaryKey
    val songId: Long,
    val lastPlayed: Long
)
