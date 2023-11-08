package me.ppvan.moon.ui.viewmodel

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beust.klaxon.JsonArray
import com.beust.klaxon.Parser
import com.yausername.ffmpeg.FFmpeg
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.ppvan.moon.utils.await
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.nio.file.Path
import javax.inject.Inject
import javax.inject.Singleton


const val SEARCH_URL = "https://suggestqueries.google.com/complete/search?client=youtube&ds=yt&client=firefox&q="

@Singleton
class YoutubeViewModel @Inject constructor(context: Context) : ViewModel() {

    private val ytdlp: YoutubeDL = YoutubeDL.getInstance()
    private val ffmpeg = FFmpeg.getInstance()
    private val okHttpClient = OkHttpClient()
    private val ioWorker = CoroutineScope(Dispatchers.IO)

    private val _downloadProgress = MutableStateFlow(0.0f)
    val downloadProgress = _downloadProgress.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _recommendations = MutableStateFlow(listOf<String>())
    @OptIn(FlowPreview::class)
    val recommendations: StateFlow<List<String>> =
        query
            .debounce(1000L)
            .onEach { _isSearching.update { true } }
            .map { text -> getSearchRecommendation(text) }
            .onEach { _isSearching.update { false } }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(1000L),
                _recommendations.value
            )

    init {
        ioWorker.launch {
            ytdlp.init(context)
            ffmpeg.init(context)
        }
    }

    /**
     * Download audio from a youtube url.
     * @param url
     * @param downloadDir Default to user's download
     */
    fun downloadFromURL(url: String, downloadDir: Path? = getDownloadLocation()) {
        val request = YoutubeDLRequest(url)
        request.addOption("-o", "${downloadDir}/%(title)s.%(ext)s")
        request.addOption("-x")
        request.addOption("--audio-quality", "0")
        request.addOption("--embed-metadata")
        request.addOption("--parse-metadata", "%(release_year,upload_date)s:%(meta_date)s")
        request.addOption("--parse-metadata", "%(album,title)s:%(meta_album)s")
//        request.addOption("--parse-metadata \"%(album,title)s:%(meta_album)s\"")
        request.addOption("--embed-thumbnail")

        ioWorker.launch {
            ytdlp.execute(request, null, ::onProgress)
        }
    }

    private fun onProgress(progress: Float, duration: Long, temp: String) {
        Log.i("INFO", "progress = $progress, duration = $duration, temp = $temp")
        _downloadProgress.update { progress }
    }

    /**
     * Return a list of youtube recommendation with query.
     *
     * @param query User query
     * @return list of match strings
     */
    fun search(query: String): List<String> {
        return emptyList()
    }


    fun onSearch(query: String) {
        _query.value = query

        Log.i("INFO", "query = ${this.query.value}")
    }

    private suspend fun getSearchRecommendation(query: String): List<String> {
        val url = SEARCH_URL + query

        val request = Request.Builder().url(url).build()
        val response = okHttpClient.newCall(request).await()

        val parser: Parser = Parser.default()
        val jsonStr = response.body!!.string()
        val array = parser.parse(StringBuilder(jsonStr)) as JsonArray<*>

        // If out of index or something, api changed and should be fix quickly
        val recommends: JsonArray<String> = array[1] as JsonArray<String>

        Log.i("INFO", recommends.toString())

        return recommends.value.toList()
    }

    /**
     * Copied from: https://mmmnnnmmm.com/okhttp-kotlin-flow-coroutine.html
     */
    private fun fetch(
        url: String,
        client: OkHttpClient = OkHttpClient.Builder().build(),
        request: Request.Builder = Request.Builder(),
    ) = callbackFlow<Response> {
        val req = request.url(url).build()
        client.newCall(req).enqueue(object : Callback {
            override fun onResponse(call: Call, resp: Response) {
                if (resp.isSuccessful) {
                    trySendBlocking(resp)
                        .onFailure { /* log it */ }
                } else {
                    cancel("bad http code")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                cancel("okhttp error", e)
            }
        })
    }

    private fun getDownloadLocation() =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)!!
            .toPath().resolve("Moon").toAbsolutePath()
}