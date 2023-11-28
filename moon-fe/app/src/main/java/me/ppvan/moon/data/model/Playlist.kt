package me.ppvan.moon.data.model

data class Playlist(
    val id: Int,
    val name: String,
    val songs: List<Track>
) {
    companion object {
        fun default() : Playlist {
            return Playlist(
                id = 123,
                name = "My Default playlist",
                songs = List(10) {Track.DEFAULT },
            )
        }
    }

    val thumbnail: String
        get() {
            return if (songs.isEmpty()) {
                ""
            } else {
                songs.first().thumbnailUri
            }
        }
}

