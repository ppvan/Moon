package me.ppvan.moon.data.model

data class Album(
    val id: Long = 123,
    val name: String = "Alicization",
    val artist: String?="Kirito",
    var numberOfTracks: Int = 10,
    val thumbnailUri: String = "",
) {
    companion object {
        val DEFAULT = Album()
    }
}
