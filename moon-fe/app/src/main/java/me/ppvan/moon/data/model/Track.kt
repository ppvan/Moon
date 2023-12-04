package me.ppvan.moon.data.model

import android.net.Uri
import androidx.media3.common.MediaItem

/**
 * Track is a song in exoplayer.
 *
 * This is for consistency
 */
data class Track(
    val id: Long = 0,
    val title: String,
    val artist: String,
    val album: String,
    val thumbnailUri: String,
    val contentUri: String,
    val songUri: Uri = Uri.EMPTY,
    val trackNumber: Int = 0,
    val discNumber: Int = 0,
    val comment: String = ""
) {
    companion object {

        fun default(): Track {
            return Track(
                id = 0,
                title = "Default Track",
                artist = "Default Artist",
                album = "Default Album",
                thumbnailUri = "Default thumb",
                contentUri = "Default content",
                songUri = Uri.EMPTY,
                trackNumber = 0,
                discNumber = 0,
                comment = "Default comment"
            )
        }

        fun fromMediaItem(mediaItem: MediaItem): Track {
            val metadata = mediaItem.mediaMetadata

            return Track(
                id = 0,
                title = metadata.title.toString(),
                artist = metadata.artist.toString(),
                contentUri = mediaItem.mediaId,
                album = metadata.albumTitle.toString(),
                thumbnailUri = metadata.artworkUri.toString(),
                trackNumber = metadata.trackNumber ?: 0,
                discNumber = metadata.discNumber ?: 0,
                comment = metadata.description.toString()
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