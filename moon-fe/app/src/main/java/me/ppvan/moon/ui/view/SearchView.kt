package me.ppvan.moon.ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.SaveAlt
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
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
                    Text(text = "Search music on Youtube")
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

            val item = SearchItem(
                "BrEp0n4L1Fc", "Mareux - The Perfect Girl slowed", "B.A.S.L", 100L, "https://i.ytimg.com/vi/gSt9fwuLwAs/hq720.jpg" )
//            SearchResult(List(3) {item})

            Spacer(modifier = Modifier.height(16.dp))
            SearchResult(searchItems = resultItems)
        }

    }
}


@Composable
fun SearchResult(searchItems: List<SearchItem>) {

//    Column (
//        modifier = Modifier.padding(8.dp),
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//
//    ) {
//        for(item in searchItems) {
//            SearchResultListItem(item)
//        }
//    }

    LazyColumn() {
        items(searchItems) { item ->
            SearchResultListItem(resultItem = item)
        }
    }
}


@Composable
fun SearchResultListItem(
    resultItem: SearchItem,
    onCancelClick: (SearchItem) -> Unit  = {},
    onDownloadClick: (SearchItem) -> Unit = {},
    onClick: () -> Unit = {}
) {
    var expand by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.clickable {
            onClick()
        },
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .background(color = MaterialTheme.colorScheme.surface),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(50.dp, 50.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(resultItem.thumbnailUrl)
                        .error(R.drawable.thumbnail)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.thumbnail),
                    contentDescription = "Music thumbnail",
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
//                verticalArrangement = Arrangement.SpaceBetween
                ) {
                Text(text = resultItem.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = resultItem.uploader, style = MaterialTheme.typography.labelMedium)
                Text(text = resultItem.message, style = MaterialTheme.typography.labelSmall)
            }

            if (resultItem.isDownloading) {
                IconButton(onClick = { onCancelClick(resultItem) }) {
                    Icon(imageVector = Icons.Outlined.Close, contentDescription = "Close")
                }
            } else {
                IconButton(onClick = { onDownloadClick(resultItem) }) {
                    Icon(imageVector = Icons.Outlined.SaveAlt, contentDescription = "SaveAlt")
                }
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
        SearchResult(listOf(item))
    }
    
}
