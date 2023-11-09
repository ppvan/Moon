package me.ppvan.moon.ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import me.ppvan.moon.R
import me.ppvan.moon.ui.theme.MoonTheme
import me.ppvan.moon.ui.viewmodel.SearchItem
import me.ppvan.moon.ui.viewmodel.YoutubeViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(viewModel: YoutubeViewModel) {

    val query by viewModel.searchQuery.collectAsState()
    val recommendations by viewModel.recommendations.collectAsState()
    val active by viewModel.active.collectAsState()
    val isRecommendationsLoading by viewModel.isRecommending.collectAsState()
    val resultItems by viewModel.searchResult.collectAsState()

    Scaffold { _ ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
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
                    IconButton(onClick = viewModel::onClose) {
                        Icon(imageVector = Icons.Outlined.Close, contentDescription = "Close Icon")
                    }
                }
            ) {
                if (isRecommendationsLoading) {
                    // I think it's more annoying than show loading in recommendations
                } else {
                    LazyColumn {
                        items(recommendations) { item ->
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

            SearchResult(resultItems)
        }
    }
}


@Composable
fun SearchResult(searchItems: List<SearchItem>) {
    LazyColumn() {
        items(searchItems) { item ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .padding(vertical = 4.dp)
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.thumbnailUrl)
                        .error(R.drawable.thumbnail)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewCardList() {

    val item = SearchItem(
        "BrEp0n4L1Fc", "Mareux - The Perfect Girl slowed", "B.A.S.L", 100L, "https://i.ytimg.com/vi/gSt9fwuLwAs/hq720.jpg" )
    MoonTheme {
        Box(
            modifier = Modifier
                .size(400.dp, 300.dp)
                .clip(RoundedCornerShape(percent = 10))
                .background(Color.Red)
                .padding(vertical = 4.dp)
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.hq720),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
    }
    
}
