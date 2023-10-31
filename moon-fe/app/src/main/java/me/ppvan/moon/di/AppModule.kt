package me.ppvan.moon.di

import android.app.Application
import android.content.Context
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.ppvan.moon.ui.player.MoonPlayer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Depending on the scope you want
object AppModule {

    @Provides
    @Singleton
    fun provideExoPlayer(@ApplicationContext context: Context): ExoPlayer {
        return ExoPlayer.Builder(context).build()
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideMoonPlayer(player: ExoPlayer): MoonPlayer {
        return MoonPlayer(player)
    }

}
