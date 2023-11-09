package me.ppvan.moon.utils

import android.content.Context
import android.os.Environment
import android.util.Log
import com.yausername.ffmpeg.FFmpeg
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.nio.file.Path

object DownloadUtils {

    private val IOScope = CoroutineScope(Dispatchers.IO)
    private var initialized = false

    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    private val _downloadProgress = MutableStateFlow(0.0f)
    val downloadProgress = _downloadProgress.asStateFlow()

    fun init(context: Context) {

        IOScope.launch {
            YoutubeDL.getInstance().init(context)
            FFmpeg.getInstance().init(context)

            initialized = true
        }
    }


    /**
     * Download audio from a youtube url.
     * @param url
     * @param downloadDir Default to user's download
     */
    fun downloadAudio(url: String, downloadDir: Path = defaultDownloadLocation()) {

        assert(initialized) { "Not initialized, call init() first" }

        val request = YoutubeDLRequest(url)
        request.addOption("-o", "${downloadDir}/%(title)s.%(ext)s")
        request.addOption("-x")
        request.addOption("--audio-quality", "0")
        request.addOption("--embed-metadata")
        request.addOption("--parse-metadata", "%(release_year,upload_date)s:%(meta_date)s")
        request.addOption("--parse-metadata", "%(album,title)s:%(meta_album)s")
        request.addOption("--embed-thumbnail")

        IOScope.launch {
            YoutubeDL.getInstance().execute(request, null, ::onProgress)
        }
    }

    private fun onProgress(progress: Float, duration: Long, message: String) {
        Log.i("INFO", "progress = $progress, duration = $duration, message = $message")


        _message.update { message }
        _downloadProgress.update { progress }
    }


    private fun defaultDownloadLocation() =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)!!
            .toPath().resolve("Moon").toAbsolutePath()
}