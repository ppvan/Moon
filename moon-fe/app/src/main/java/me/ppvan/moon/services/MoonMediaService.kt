package me.ppvan.moon.services

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.AndroidEntryPoint
import me.ppvan.moon.ui.activity.MainActivity
import me.ppvan.moon.ui.player.MoonPlayer
import javax.inject.Inject


@AndroidEntryPoint
class MoonMediaService : MediaSessionService() {

    lateinit var player: ExoPlayer
    private lateinit var mediaSession: MediaSession


    @Inject
    lateinit var moonPlayer: MoonPlayer

    @UnstableApi
    override fun onCreate() {
        super.onCreate()
        Log.w("INFO", "MoonMediaService started")


        player = ExoPlayer.Builder(this)
            .setWakeMode(C.WAKE_MODE_NETWORK)
            .setHandleAudioBecomingNoisy(true)
            .build()

        mediaSession = provideMediaSession(
            context = this,
            player = player
        )

        val sessionToken = SessionToken(
            applicationContext,
            ComponentName(applicationContext, MoonMediaService::class.java)
        )
        val controllerFuture =
            MediaController.Builder(applicationContext, sessionToken).buildAsync()
        controllerFuture.addListener({
            moonPlayer.setCustomPlayer(controllerFuture.get())
        }, MoreExecutors.directExecutor())
    }

    override fun onDestroy() {
        super.onDestroy()

        mediaSession.run {
            release()
            if (player.playbackState != Player.STATE_IDLE) {
                player.release()
            }
        }
    }


//    @UnstableApi
//    fun provideCoilBitmapLoader(context: Context): CoilBitmapLoader = CoilBitmapLoader(context)

    @UnstableApi
    fun provideMediaSession(
        context: Context,
        player: ExoPlayer
    ): MediaSession =
        MediaSession.Builder(context, player)
//            .setCallback(callback)
            .setSessionActivity(
                PendingIntent.getActivity(
                    context, 0, Intent(context, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
//            .setBitmapLoader(provideCoilBitmapLoader(context))
            .build()


    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession {
        return mediaSession
    }
}