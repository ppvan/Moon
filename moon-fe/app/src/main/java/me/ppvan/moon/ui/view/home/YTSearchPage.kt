package me.ppvan.moon.ui.view.home

import android.util.Log
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.SaveAlt
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import me.ppvan.moon.R
import me.ppvan.moon.data.model.Track
import me.ppvan.moon.ui.activity.Routes
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.component.IconTextBody
import me.ppvan.moon.ui.component.LoadingShimmerEffect
import me.ppvan.moon.ui.component.SpeechToTextButton
import me.ppvan.moon.ui.viewmodel.ResultItem
import me.ppvan.moon.ui.viewmodel.ResultItemState
import me.ppvan.moon.ui.viewmodel.YTViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPage(context: ViewContext) {


    val ytViewModel = context.ytViewModel
    val downloadViewModel = context.downloadViewModel
    val player = context.trackViewModel.player

    val resultItems by ytViewModel.searchResult.collectAsState()
    val isLoading by ytViewModel.isDataLoaded.collectAsState()
    when {
        resultItems.isEmpty() -> IconTextBody(
            icon = { modifier ->
                Icon(
                    Icons.Filled.Search,
                    null,
                    modifier = modifier,
                )
            },
            content = { Text("Search your favourite music") }
        )

        else -> Column {

            Spacer(modifier = Modifier.height(16.dp))
            ResultList(
                resultItems = resultItems,
                isLoading = isLoading,
                onDownloadClick = {
                    Log.d("YTSearch", it.playbackUrl)
                },
                onItemClick = { item ->
                    val url = item.playbackUrl
                    Log.d("INFO", url)
                    val track = Track.default().copy(contentUri = url)
                    player.load(listOf(track))
                    context.navigator.navigate(Routes.NowPlaying.name)
                }
            )
        }
}


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    viewModel: YTViewModel,
    active: Boolean,
    recommendations: List<String>,
    closeClick: () -> Unit,
) {
    DockedSearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(7.dp),
        query = query,
        onQueryChange = viewModel::onQueryChange,
        onSearch = { viewModel.onSearch(query) },
        active = active,
        onActiveChange = viewModel::onActiveChange,
        placeholder = {
            Text(text = "Search online music")
        },
        leadingIcon = {
            IconButton(onClick = closeClick) {
                Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "BackButton")
            }

        },
        trailingIcon = {
            if (!active) {
                IconButton(onClick = viewModel::onClose) {
                    Icon(imageVector = Icons.Outlined.Close, contentDescription = "Close Icon")
                }
            } else {
                SpeechToTextButton { spokenText ->
                    viewModel.onQueryChange(spokenText)
                    viewModel.onSearch(spokenText)
                }
            }
        }
    ) {
        RecommendationList(recommendations, viewModel::onSearch)
    }

}

@Composable
fun ResultList(
    resultItems: List<ResultItem>,
    isLoading: Boolean,
    onDownloadClick: (ResultItem) -> Unit = {},
    onItemClick: (ResultItem) -> Unit = {}
) {
    if (!isLoading) {
        LoadingShimmerEffect()
    } else {
        LazyColumn() {
            items(resultItems) { item ->
                ResultItem(
                    resultItem = item,
                    onDownloadClick = onDownloadClick,
                    isLoading = isLoading,
                    onClick = { onItemClick(item) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun RecommendationList(recommendations: List<String>, onItemClick: (String) -> Unit) {
    LazyColumn(modifier = Modifier.imePadding()) {
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
    onDownloadClick: (ResultItem) -> Unit = {},
    onClick: () -> Unit = {},
    isLoading: Boolean
) {

    var openDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .clickable {
                onClick()
            }
            .padding(8.dp),
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

                IconButton(onClick = { openDialog = true }) {
                    Icon(imageVector = Icons.Outlined.SaveAlt, contentDescription = "SaveAlt")
                }

                if (openDialog) {
                    ConfirmDialog(
                        title = "",
                        content = "Download ${resultItem.title} ?",
                        onDismissRequest = { openDialog =  false }) {

                        onDownloadClick(resultItem)
                        openDialog = false
                    }
                }
            }


    }
}

@Composable
fun ConfirmDialog(
    title: String,
    content: String,

    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,



) {
    AlertDialog(
        text = {
            Text(text = content)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Cancel")
            }
        }
    )

}

