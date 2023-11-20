package me.ppvan.moon.ui.viewmodel

import android.app.PendingIntent
import android.app.RecoverableSecurityException
import android.content.IntentSender
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.ppvan.moon.data.model.Track
import me.ppvan.moon.data.repository.TrackRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TagEditViewModel @Inject constructor(
    private val trackRepository: TrackRepository,

) :
    ViewModel() {

    val permissionEvents: MutableStateFlow<IntentSender?> = MutableStateFlow(null)


    private val _track = MutableStateFlow(Track.DEFAULT)
    val currentTrack = _track.asStateFlow()

    val cover = MutableStateFlow("")
    val title = MutableStateFlow("")
    val artist = MutableStateFlow("")
    val album = MutableStateFlow("")
    val trackNumber = MutableStateFlow("")
    val discNumber = MutableStateFlow("")
    val comment = MutableStateFlow("")

    init {
        viewModelScope.launch {
            _track.onEach { track ->
                cover.update { track.thumbnailUri }
                title.update { track.title }
                artist.update { track.artist }
                album.update { track.album }
                trackNumber.update { "Track number" }
                discNumber.update { "disc number" }
                comment.update { "comment" }
            }.collect()
        }
    }


    suspend fun loadTrack(mediaId: String) {
        withContext(Dispatchers.IO) {
            _track.update { trackRepository.findTrackById(mediaId) }
        }
    }

    fun onCoverChange(uri: String) {
        this.cover.update { uri }
    }

    fun onTitleChange(title: String) {
        this.title.update { title }
    }

    fun onArtistChange(artist: String) {
        this.artist.update { artist }
    }

    fun onAlbumChange(album: String) {
        this.album.update { album }
    }

    fun onTrackNumberChange(trackNumber: String) {
        this.trackNumber.update { trackNumber }
    }

    fun onDiscNumberChange(discNumber: String) {
        this.discNumber.update { discNumber }
    }

    fun onCommentChange(comment: String) {
        this.comment.update { comment }
    }


    @RequiresApi(Build.VERSION_CODES.R)
    fun onSave() {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                val updatedTrack = _track.value.copy(
                    title = title.value
                )

                trackRepository.save(updatedTrack)
            } catch (securityException: SecurityException) {
                Log.d("INFO", securityException.message.orEmpty())

                val recoverableSecurityException = securityException as?
                        RecoverableSecurityException ?:
                throw RuntimeException(securityException.message, securityException)
                val intentSender =
                    recoverableSecurityException.userAction.actionIntent.intentSender

                permissionEvents.update { intentSender }
//                MediaStore.createWriteRequest(null, listOf())

            }
        }


    }
}