package me.ppvan.moon.ui.view.home

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
import me.ppvan.moon.ui.viewmodel.ResultItem
import me.ppvan.moon.ui.viewmodel.ResultItemState
import me.ppvan.moon.ui.viewmodel.YTViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(viewModel: YTViewModel) {

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
                RecommendationList(recommendations, viewModel::onSearch)
            }

            Spacer(modifier = Modifier.height(16.dp))
            ResultList(resultItems = resultItems)
        }

    }
}


@Composable
fun ResultList(resultItems: List<ResultItem>) {
    LazyColumn() {
        items(resultItems) { item ->
            ResultItem(resultItem = item)
        }
    }
}

@Composable
fun RecommendationList(recommendations: List<String>, onItemClick: (String) -> Unit) {
    LazyColumn {
        items(recommendations) { item ->
            ListItem(
                modifier = Modifier.clickable { onItemClick(item) },
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

@Composable
fun ResultItem(
    resultItem: ResultItem,
    onCancelClick: (ResultItem) -> Unit = {},
    onDownloadClick: (ResultItem) -> Unit = {},
    onClick: () -> Unit = {}
) {

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
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
//                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = resultItem.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = resultItem.uploader, style = MaterialTheme.typography.labelMedium)

                when (resultItem.state) {
                    ResultItemState.NONE -> {}
                    ResultItemState.DOWNLOADING -> {
                        Text(text = resultItem.state.message, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            if (resultItem.state == ResultItemState.DOWNLOADING) {
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

    val item = ResultItem(
        "BrEp0n4L1Fc",
        "Mareux - The Perfect Girl slowed",
        "B.A.S.L",
        100L,
        "https://i.ytimg.com/vi/gSt9fwuLwAs/hq720.jpg"
    )
    MoonTheme {
        ResultList(listOf(item))
    }

}
