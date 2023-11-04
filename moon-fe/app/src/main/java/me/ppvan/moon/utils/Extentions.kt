package me.ppvan.moon.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.ppvan.moon.ui.player.MoonPlayer
import me.ppvan.moon.ui.player.PlayerState

/**
 * Collects the player state from [myPlayer] and provides updates via the [updateState] function.
 *
 * @param myPlayer The player whose state is to be collected.
 * @param updateState A function to process the player state updates.
 */
fun CoroutineScope.collectPlayerState(
    myPlayer: MoonPlayer, updateState: (PlayerState) -> Unit
) {
    this.launch {
        myPlayer.playerState.collect {
            updateState(it)
        }
    }
}