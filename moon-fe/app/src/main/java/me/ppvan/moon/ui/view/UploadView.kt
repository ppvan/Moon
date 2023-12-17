package me.ppvan.moon.ui.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import me.ppvan.moon.R
import me.ppvan.moon.data.model.Track
import me.ppvan.moon.ui.activity.Routes
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.component.AlbumRow
import me.ppvan.moon.ui.component.CenterTopAppBar
import me.ppvan.moon.ui.view.home.BottomPlayer
import me.ppvan.moon.ui.view.home.SongListItem
import me.ppvan.moon.ui.viewmodel.ArtistViewModel
import me.ppvan.moon.ui.viewmodel.MoonPlayer
import me.ppvan.moon.ui.viewmodel.TrackViewModel
import me.ppvan.moon.utils.SlideTransition

@Composable
fun UploadView(context: ViewContext) {
    val trackViewModel = context.trackViewModel
    var selectedTab = context.selectedTab
    val player = trackViewModel.player
    val playbackState by player.playbackState.collectAsState()
    val bottomPlayerVisible = playbackState.track != Track.default()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterTopAppBar(
                title = "Upload",
                menuItems = {
                    DropdownMenuItem(
                        text = { Text("ReScan") },
                        onClick = { context.trackViewModel.reloadTracks() },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Refresh,
                                contentDescription = "ReScan"
                            )
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Setting") },
                        onClick = { context.navigator.navigate(route = Routes.Settings.name) },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Settings,
                                contentDescription = "Settings"
                            )
                        }
                    )
                }
            )
        },
        content = { contentPadding ->
            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                SongUploadList(context = context)
            }

        },
        bottomBar = {
            Column {

                AnimatedContent(
                    targetState = bottomPlayerVisible,
                    label = "player",
                    transitionSpec = {
                        SlideTransition.slideUp.enterTransition()
                            .togetherWith(SlideTransition.slideDown.exitTransition())
                    }
                ) { visible ->
                    if (visible) {
                        BottomPlayer(
                            playbackState = playbackState,
                            onPausePlayClick = { player.playPause() },
                            onNextClick = { player.next() },
                            onClick = { context.navigator.navigate(Routes.NowPlaying.name) }
                        )
                    }
                }
                NavigationBar {
                    for (tab in MoonPages.values()) {
                        NavigationBarItem(
                            icon = { Icon(tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label) },
                            selected = selectedTab.value == tab,
                            onClick = {
                                context.updateSelectedTab(tab)
                                context.navigator.navigate(Routes.Home.name)
                            }
                        )
                    }
                }
            }
        }
    )
}


@Composable
fun SongUploadList(
    context: ViewContext,
    songs: List<Track> = List(15) { Track.default() },
    onItemClick: (item: Track) -> Unit = {},

) {
    var selectedSongsCount by remember { mutableStateOf(0) }
    LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

        items(songs) { song ->
            var isChecked by rememberSaveable { mutableStateOf(false) }
            SongUploadListItem(
                Modifier.fillMaxWidth(),
                track = song,
                isChecked = isChecked,
                onClick = {
                    isChecked = !isChecked
                    onItemClick(song)
                    selectedSongsCount += if (isChecked) 1 else -1
                },
                context = context
            )
        }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            context.notificationViewModel.showProgress(selectedSongsCount)
                        }
                    ) {
                        Text(text = "Tải lên $selectedSongsCount bài hát ")
                    }
                }
            }
        }

    }

@Composable
fun SongUploadListItem(
    modifier: Modifier = Modifier,
    track: Track = Track.default(),
    onClick: () -> Unit = {},
    context: ViewContext,
    isChecked: Boolean,
) {
    Column(
        modifier = modifier
            .clickable {
                onClick()
            }
            .background(
                color = if (isChecked) {
                    Color.Gray.copy(0.1f)
                } else {
                    MaterialTheme.colorScheme.surface
                }
            ),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(50.dp, 50.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(track.thumbnailUri)
                        .error(R.drawable.thumbnail)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.thumbnail),
                    contentDescription = "Music thumbnail",
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = track.title)
                Text(text = track.artist, style = MaterialTheme.typography.labelMedium)
            }

            Checkbox(
                checked = isChecked,
                onCheckedChange = { newChecked ->
                    onClick()
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.Green.copy(0.6f),
                )
            )
        }
    }
}
