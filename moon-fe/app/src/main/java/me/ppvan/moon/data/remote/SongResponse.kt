package me.ppvan.moon.data.remote

import me.ppvan.moon.data.model.Track
import me.ppvan.moon.ui.viewmodel.ResultItem

data class SongResponse (
    val id: Long,
    val title: String,
    val artist: String,
    val genre: String,
    val album: String
) {
    companion object {

    }

    fun toTrack(): Track {
        return Track(
            id = id,
            title = title,
            artist = artist,
            album = "album",
            thumbnailUri = "",
            contentUri = "",
        )
    }

    fun toResultItem(): ResultItem {
        return ResultItem(
            id = id.toString(),
            title = title,
            uploader = artist,
            duration = 0,
            thumbnailUrl = "",
            playbackUrl = "http://139.59.227.169:8080/api/v1/songs/$id",
        )
    }
}
