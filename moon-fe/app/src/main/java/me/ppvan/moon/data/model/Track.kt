package me.ppvan.moon.data.model

import me.ppvan.moon.ui.player.PlayerState

data class Track(
    val id: Long = 123,
    val title: String = "Faded",
    val artist: String = "Alan Walker",
    val album: String = "Faded Song",
    val thumbnailUri: String = "",
    val contentUri: String = "",
    var state: PlayerState = PlayerState.STATE_IDLE
) {
    companion object {
        val DEFAULT = Track()
    }
}