package me.ppvan.moon.data.remote

import me.ppvan.moon.data.model.Track
import me.ppvan.moon.ui.viewmodel.ResultItem

data class SongResponse (
    val id: Long,
    val title: String,
    val artist: String,
    val thumbnail: String,
    val filePath: String,
    val album: String
) {
    companion object {

    }

    fun toTrack(): Track {
        return Track(
            id = id,
            title = title,
            artist = artist,
            album = album,
            thumbnailUri = thumbnail,
            contentUri = filePath,
        )
    }

    fun toResultItem(): ResultItem {
        return ResultItem(
            id = id.toString(),
            title = title,
            uploader = artist,
            duration = 0,
            thumbnailUrl = thumbnail,
            playbackUrl = filePath,
        )
    }
}
