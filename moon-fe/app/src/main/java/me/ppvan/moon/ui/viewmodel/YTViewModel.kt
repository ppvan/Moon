package me.ppvan.moon.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
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
import me.ppvan.moon.utils.await
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Singleton


const val RECOMMEND_API =
    "https://suggestqueries.google.com/complete/search?client=youtube&ds=yt&client=firefox&q="
const val SEARCH_API = "https://pipedapi.kavin.rocks/"

@Singleton
class YTViewModel @Inject constructor() : ViewModel() {

    private val okHttpClient = OkHttpClient()

    private val _isRecommending = MutableStateFlow(false)
    val isRecommending = _isRecommending.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _active = MutableStateFlow(false)
    val active = _active.asStateFlow()

    private val _query = MutableStateFlow("")
    val searchQuery = _query.asStateFlow()

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
        withContext(Dispatchers.IO) {
            val url = SEARCH_API + "search?q=${query}&filter=videos&region=vi"
            val parser: Parser = Parser.default()
            val request = Request.Builder()
                .header(
                    "User-Agent",
                    "Mozilla/5.0 (X11; Linux x86_64; rv:109.0) Gecko/20100101 Firefox/119.0"
                )
                .url(url).build()

            val response = okHttpClient.newCall(request).await()
            val content = response.body!!.string()
            Log.e("INFO", content)
            val items = (parser.parse(StringBuilder(content)) as JsonObject)
                .array<JsonObject>("items")!!

            for (item in items) {
                val id = getIdFromURL(item.string("url").orEmpty())
                val title = item.string("title").orEmpty()
                val uploader = item.string("uploaderName").orEmpty()
                val duration = item.long("duration") ?: 0
                val thumb = "https://i.ytimg.com/vi/$id/hqdefault.jpg"

                result.add(
                    ResultItem(
                        id = id,
                        title = title,
                        uploader = uploader,
                        duration = duration,
                        thumbnailUrl = thumb
                    )
                )
            }
        }


        return result
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
        val url = RECOMMEND_API + query
        val request = Request.Builder().url(url).build()
        val parser: Parser = Parser.default()

        val recommends = withContext(Dispatchers.IO) {
            val response = okHttpClient.newCall(request).await()
            val jsonStr = response.body!!.string()
            val array = parser.parse(StringBuilder(jsonStr)) as JsonArray<*>
            // If out of index or something, api changed and should be fix quickly

            return@withContext array[1] as JsonArray<String>
        }

        return recommends.value.toList()
    }
}

data class ResultItem(
    val id: String,
    val title: String,
    val uploader: String,
    val duration: Long,
    val thumbnailUrl: String,

    val state: ResultItemState = ResultItemState.NONE
)

enum class ResultItemState(var message: String) {
    NONE("Not downloaded"),
    DOWNLOADING("Downloading")
    ;
}