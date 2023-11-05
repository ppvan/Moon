package me.ppvan.moon.services

import android.media.session.MediaSession
import android.util.Log
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MoonMediaService @Inject constructor(
    val player: ExoPlayer,
    val mediaSession: MediaSession,
) : MediaSessionService() {

    override fun onCreate() {
        super.onCreate()
        Log.i("INFO", "MoonMediaService started")
    }


    override fun onGetSession(controllerInfo: androidx.media3.session.MediaSession.ControllerInfo): androidx.media3.session.MediaSession? {
        TODO("Not yet implemented")
    }
}