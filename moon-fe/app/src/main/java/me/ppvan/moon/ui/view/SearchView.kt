package me.ppvan.moon.ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import me.ppvan.moon.ui.viewmodel.YoutubeViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(viewModel: YoutubeViewModel) {

    val query by viewModel.searchQuery.collectAsState()
    val recommendations by viewModel.recommendations.collectAsState()
    val active by viewModel.active.collectAsState()
    val isRecommendationsLoading by viewModel.isSearching.collectAsState()

    Column (
        modifier =Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            query = query,
            onQueryChange = viewModel::onQueryChange,
            onSearch = viewModel::onSearch,
            active = active,
            onActiveChange = viewModel::onActiveChange,
            placeholder = {
                Text(text = "Search")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search Icon")
            },
            trailingIcon = {
                IconButton(onClick = viewModel::onClose ) {
                    Icon(imageVector = Icons.Outlined.Close, contentDescription = "Close Icon")
                }
            }
        ) {
            if (isRecommendationsLoading) {
// I think it's more annoying than show loading in recommendations
//                Column (
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalAlignment = Alignment.CenterHorizontally
//
//                ) {
//                    Spacer(modifier = Modifier.height(36.dp))
//                    CircularProgressIndicator()
//                }
            } else {
                LazyColumn {
                    items(recommendations) {item ->
                        ListItem(
                            modifier = Modifier.clickable { viewModel.onSearch(item) },
                            headlineContent = { Text(text = item) },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Outlined.Search,
                                    contentDescription = "Search Icon"
                                )
                            }
                        )
                    }
                }
            }
        }
        
        Box(modifier = Modifier
            .weight(1f)
            .background(Color.Red))
    }
}