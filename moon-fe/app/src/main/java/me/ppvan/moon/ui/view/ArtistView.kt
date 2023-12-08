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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import me.ppvan.moon.ui.component.TopAppBarMinimalTitle
import me.ppvan.moon.ui.view.home.BottomPlayer
import me.ppvan.moon.ui.view.home.SongList
import me.ppvan.moon.ui.view.home.SongListItem
import me.ppvan.moon.ui.viewmodel.ArtistViewModel
import me.ppvan.moon.ui.viewmodel.MoonPlayer
import me.ppvan.moon.ui.viewmodel.TrackViewModel
import me.ppvan.moon.utils.SlideTransition


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistView(context: ViewContext, artistId: Long) {
    var selectedTab = context.selectedTab
    val artistViewModel: ArtistViewModel = context.artistViewModel
    val trackViewModel: TrackViewModel = context. trackViewModel
    val artist = artistViewModel.getArtistById(artistId)
    val allSongs = artistViewModel.getSongsByAlbumId(artistId)
    val allAlbums = artistViewModel.getAlbumById(artistId)
    val player: MoonPlayer = trackViewModel.player
    val playbackState by player.playbackState.collectAsState()
    val bottomPlayerVisible = playbackState.track != Track.default()
    val isOpenSeeMore:Boolean = (allAlbums.size > 6)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { contentPadding ->
            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ){
                    item {
                        Box{
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(allSongs[0].thumbnailUri)
                                    .error(R.drawable.thumbnail)
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(R.drawable.thumbnail),
                                contentDescription = "Music thumbnail",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(360.dp)
                            )

                            Box(modifier = Modifier
                                .padding(12.dp)
                                .size(32.dp)
                                .align(Alignment.TopStart)
                                .background(Color.Gray, shape = CircleShape)){
                                IconButton(
                                    onClick = { context.navigator.popBackStack() },

                                    ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back",
                                        tint = Color.White
                                    )
                                }
                            }
                            Text(
                                text = artist.name,
                                color = Color.White,
                                fontSize = 25.sp,
                                modifier = Modifier
                                    .padding(end = 8.dp, bottom = 8.dp)
                                    .align(Alignment.BottomEnd)
                            )
                            }

                    }
                    item {
                        Column(modifier = Modifier.padding(top = 8.dp, start = 4.dp)) {
                            Box(modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    ProvideTextStyle(MaterialTheme.typography.titleMedium) {
                                        Text(
                                            text = "Song",
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Icon(
                                        imageVector = Icons.Default.ArrowForwardIos,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                        )
                                }

                            }
                            ArtistSongList(
                                context = context,
                                songs = allSongs,
                            ){
                                player.load(allSongs)
                                player.preparePlay(it)
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ){
                                Box(
                                    modifier = Modifier
                                        .border(
                                            1.dp,
                                            Color.LightGray.copy(0.7f),
                                            RoundedCornerShape(30.dp)
                                        )
                                        .clickable {
                                            context.navigator.navigate(Routes.Playlist.name)
                                        }
                                        .width(120.dp)
                                ) {
                                    Text(
                                        "Xem thêm",
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(bottom = 2.dp)
                                            .fillMaxSize()
                                            .wrapContentSize(Alignment.Center)
                                    )
                                }
                            }
                        }

                    }
                    item {
                        Column(modifier = Modifier.padding(top = 8.dp, start = 4.dp)) {
                            Box(modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    ProvideTextStyle(MaterialTheme.typography.titleMedium) {
                                        Text(
                                            text = "Album",
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Icon(
                                        imageVector = Icons.Default.ArrowForwardIos,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }

                            }
                            AlbumRow(albums = allAlbums, context = context, isOpenSeeMore)
                        }
                    }
                }


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
fun ArtistSongList(
    context: ViewContext,
    songs: List<Track> = List(10) { Track.default() },
    onItemClick: (item: Track) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        songs.take(6).forEach { song ->
            SongListItem(
                modifier = Modifier
                    .fillMaxWidth(),
                track = song,
                onClick = { onItemClick(song) },
                context = context
            )
        }
    }
}
