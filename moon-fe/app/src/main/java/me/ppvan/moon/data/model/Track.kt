package me.ppvan.moon.data.model

import me.ppvan.moon.ui.player.PlayerStates

data class Track(
    val mediaId: String = "123",
    val title: String = "Faded",
    val album: String = "Faded Song",
    val artist: String = "Alan Walker",
    var state: PlayerStates = PlayerStates.STATE_IDLE
) {
    companion object {
        val DEFAULT = Track()
    }
}