package me.ppvan.moon.utils

import android.content.Context
import android.media.MediaScannerConnection
import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yausername.ffmpeg.FFmpeg
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.Path
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadViewModel @Inject constructor(@ApplicationContext context: Context) : ViewModel() {

    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    private val _downloadProgress = MutableStateFlow(0.0f)
    val downloadProgress = _downloadProgress.asStateFlow()

    init {
        viewModelScope.launch (Dispatchers.IO) {
            YoutubeDL.getInstance().init(context)
            FFmpeg.getInstance().init(context)
        }
    }

    /**
     * Download audio from a youtube url.
     * @param url
     * @param downloadDir Default to user's download
     */
    fun downloadAudio(url: String, downloadDir: Path = defaultDownloadLocation()) {

        val request = YoutubeDLRequest(url)
        request.addOption("-o", "${downloadDir}/%(title)s.mp3")
        request.addOption("-x")
        request.addOption("--audio-format", "mp3")
        request.addOption("--audio-quality", "0")
        request.addOption("--embed-metadata")
        request.addOption("--parse-metadata", "%(release_year,upload_date)s:%(meta_date)s")
        request.addOption("--parse-metadata", "%(album,title)s:%(meta_album)s")
        request.addOption("--embed-thumbnail")

        viewModelScope.launch(Dispatchers.IO) {
            YoutubeDL.getInstance().execute(request, null, ::onProgress)
//            scanMedia(listOf())
        }
    }

    private fun onProgress(progress: Float, duration: Long, message: String) {
        Log.i("INFO", "progress = $progress, duration = $duration, message = $message")


        _message.update { message }
        _downloadProgress.update { progress }
    }

    fun scanMedia(files: List<String>, context: Context) : List<String> {
        try {
            val paths = files.sortedByDescending { File(it).length() }
            runCatching {
                paths.forEach {
                    MediaScannerConnection.scanFile(context, arrayOf(it), null, null)
                }
            }
            return paths
        }catch (e: Exception){
            e.printStackTrace()
        }

        return listOf()
    }


    private fun defaultDownloadLocation() =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)!!
            .toPath().resolve("Moon").toAbsolutePath()
}