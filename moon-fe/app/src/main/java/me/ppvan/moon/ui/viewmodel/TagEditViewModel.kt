package me.ppvan.moon.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anggrayudi.storage.media.MediaStoreCompat
import com.anggrayudi.storage.media.MediaType
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
import me.ppvan.moon.utils.scanMedia
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.audio.exceptions.CannotWriteException
import org.jaudiotagger.tag.FieldKey
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TagEditViewModel @Inject constructor(
    private val trackRepository: TrackRepository,

) :
    ViewModel() {

    val pendingTrackWriteRequest: MutableStateFlow<Track> = MutableStateFlow(Track.default())


    private val _track = MutableStateFlow(Track.default())
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

    fun writeTagToFile(context: Context) {
        try {
            val updatedTrack = _track.value.copy(title = title.value)
            val mediaFile = MediaStoreCompat.fromMediaId(context, MediaType.AUDIO, updatedTrack.id)
            val file = File(mediaFile?.absolutePath!!)
            val audioFile = AudioFileIO.read(file)
            audioFile.run {
                tag.setField(FieldKey.TITLE, title.value)
                tag.setField(FieldKey.ARTIST, artist.value)
                tag.setField(FieldKey.ALBUM, album.value)
                tag.setField(FieldKey.TRACK, trackNumber.value)
                tag.setField(FieldKey.DISC_NO, discNumber.value)
                tag.setField(FieldKey.COMMENT, comment.value)
            }

            audioFile.commit()
            scanMedia(listOf(mediaFile.absolutePath), context)

        } catch (exception: CannotWriteException) {
            Log.d("Exception", exception.message.toString())
        }

        trackRepository.findAll().forEach {
            Log.d("Track", it.title)
        }
    }

    fun onSaveRequest() {
        val updatedTrack = _track.value.copy(title = title.value)

//        _track.update { updatedTrack }
        pendingTrackWriteRequest.update { updatedTrack }
    }
}