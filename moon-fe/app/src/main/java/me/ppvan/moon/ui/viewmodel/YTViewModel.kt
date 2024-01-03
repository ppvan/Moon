package me.ppvan.moon.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import me.ppvan.moon.data.model.Track
import me.ppvan.moon.data.retrofit.ApiService
import javax.inject.Inject
import javax.inject.Singleton


const val RECOMMEND_API = "http://139.59.227.169:8080"
const val SEARCH_API = "https://api-piped.mha.fi/"

@Singleton
class YTViewModel @Inject constructor(private val moonService: ApiService) : ViewModel() {

    private val _isRecommending = MutableStateFlow(false)
    val isRecommending = _isRecommending.asStateFlow()

    private val _active = MutableStateFlow(false)
    val active = _active.asStateFlow()

    private val _query = MutableStateFlow("")
    val searchQuery = _query.asStateFlow()

    private val _isDataLoaded = MutableStateFlow(true)
    val isDataLoaded = _isDataLoaded.asStateFlow()

    private val _recommendations = MutableStateFlow(listOf<String>())

    @OptIn(FlowPreview::class)
    val recommendations: StateFlow<List<String>> =
        searchQuery
            .debounce(500L)
            .onEach { _isRecommending.update { true } }
            .map { text -> getSearchRecommendation(text) }
            .onEach { _isRecommending.update { false } }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(1000L),
                _recommendations.value
            )

    private val _searchResult = MutableStateFlow(listOf<ResultItem>())

    @OptIn(FlowPreview::class)
    val searchResult: StateFlow<List<ResultItem>> = active
        .debounce(500L)
        .combine(searchQuery) { active, query ->
            active to query
        }
        .filter { (active, query) ->
            query.isNotEmpty() && !active
        }.map { (_, query) ->
            getMatchedSearchItem(query)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), _searchResult.value)

    fun onClose() {
        if (_query.value.isNotEmpty()) {
            _query.update { "" }
        } else {
            _active.update { false }
        }
    }

    fun onQueryChange(query: String) {
        _query.value = query

        Log.i("INFO", "query = ${this.searchQuery.value}")
    }

    fun onSearch(query: String) {
        _query.update { query }
        _active.update { false }

        Log.i("INFO", "onSearch")
    }

    fun onActiveChange(active: Boolean) {
        _active.update { active }

        Log.i("INFO", "active = $active")
    }


    /**
     * Search youtube videos (not logged in).
     * region is fixed to vietnam
     */
    private suspend fun getMatchedSearchItem(query: String): List<ResultItem> {

        val result = mutableListOf<ResultItem>()
//        return emptyList()
        _isDataLoaded.emit(false)
        withContext(Dispatchers.IO) {
            val response = moonService.search(query)
            withContext(Dispatchers.Main) {
                if (!response.isSuccessful || response.body() == null) {
                    Log.d("SearchVM", "Failed: ${response.code()}")
                } else {
                    Log.d("SearchVM", "Success")

                    response.body()?.let {songs ->
                        result.addAll(songs.map { it.toResultItem() })
                    }
                }
            }
        }

        _isDataLoaded.emit(true)
        return result
    }

    fun getPlayableUrl(id: String): String {
        val request = YoutubeDLRequest("https://www.youtube.com/watch?v=${id}")
        val streamInfo = YoutubeDL.getInstance().getInfo(request)

        val directUrl = streamInfo.formats!!.filter { format ->
            format.vcodec == "none" && format.acodec != "none"
        }.map { it.url.orEmpty() }.onEach {
            Log.d("INFO", it)
        }.first()

        return directUrl
    }

    private fun getIdFromURL(url: String): String {
        var el: Array<String?> =
            url.split("/".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
        var query = el[el.size - 1]
        if (query!!.contains("watch?v=")) {
            query = query.substring(8)
        }
        el = query.split("&".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        query = el[0]
        el = query!!.split("\\?".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        query = el[0]
        return query!!
    }

    private suspend fun getSearchRecommendation(query: String): List<String> {
        val suggestions = mutableListOf<String>()

        withContext(Dispatchers.IO) {
            val response = moonService.suggest(query)

            withContext(Dispatchers.Main) {
                if (!response.isSuccessful || response.body() == null) {
                    Log.d("SearchVM", "Failed: ${response.code()}")
                } else {
                    Log.d("SearchVM", "Success")
                    suggestions.addAll(response.body()!!)
                }
            }
        }

        return suggestions
    }
}

data class ResultItem(
    val id: String,
    val title: String,
    val uploader: String,
    val duration: Long,
    val thumbnailUrl: String,
    val playbackUrl: String,
    val isLoading: Boolean = true,

    val state: ResultItemState = ResultItemState.NONE
) {
    fun toTrack(): Track {
        return Track(
            id = id.toLong(),
            title = title,
            artist = uploader,
            album = "",
            thumbnailUri = thumbnailUrl,
            contentUri = playbackUrl
        )
    }
}

enum class ResultItemState(var message: String) {
    NONE("Not downloaded"),
    DOWNLOADING("Downloading")
    ;
}