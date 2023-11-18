package me.ppvan.moon.data.model

import androidx.media3.common.MediaItem

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

        fun fromMediaItem(mediaItem: MediaItem): Track {
            val metadata = mediaItem.mediaMetadata

            return Track(
                id = 0,
                title = metadata.title.toString(),
                artist = metadata.artist.toString(),
                contentUri = mediaItem.mediaId,
                album = metadata.albumTitle.toString(),
                thumbnailUri = metadata.artworkUri.toString()
            )
        }
    }

    // Override equals and hashCode based on contentUri
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Track

        return contentUri == other.contentUri
    }

    override fun hashCode(): Int {
        return contentUri.hashCode()
    }
}