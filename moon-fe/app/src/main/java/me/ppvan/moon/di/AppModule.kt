package me.ppvan.moon.di

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.ppvan.moon.data.dao.MoonDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Depending on the scope you want
object AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun providePlayer(@ApplicationContext context: Context): Player {
        return ExoPlayer.Builder(context).build()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MoonDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            MoonDatabase::class.java,
            "moon_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver {
        return context.contentResolver
    }
}
