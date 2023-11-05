package me.ppvan.moon.data.model

/**
 * Track is a song in exoplayer.
 *
 * This is for consistency
 */
data class Track(
    val id: Long = 123,
    val title: String = "Faded",
    val artist: String = "Alan Walker",
    val album: String = "Faded Song",
    val thumbnailUri: String = "",
    val contentUri: String = ""
) {
    companion object {
        val DEFAULT = Track()
    }
}