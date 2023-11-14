package me.ppvan.moon.data.model

data class Artist(
    val id: Long = 123,
    val name: String,
    var numberOfAlbums: Int,
    var numberOfTracks: Int,
    val thumbnailUri: String = "",
    )