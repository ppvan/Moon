package me.ppvan.moon.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beust.klaxon.JsonArray
import com.beust.klaxon.Parser
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import me.ppvan.moon.utils.await
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Singleton


const val SEARCH_URL = "https://suggestqueries.google.com/complete/search?client=youtube&ds=yt&client=firefox&q="

@Singleton
class YoutubeViewModel @Inject constructor() : ViewModel() {

    private val okHttpClient = OkHttpClient()

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
            .onEach { _isSearching.update { true } }
            .map { text -> getSearchRecommendation(text) }
            .onEach { _isSearching.update { false } }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(1000L),
                _recommendations.value
            )


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
        _active.update { false }
        _query.update { query }

        Log.i("INFO", "onSearch")
    }

    fun onActiveChange(active: Boolean) {
        _active.update { active }

        Log.i("INFO", "active = $active")
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
}