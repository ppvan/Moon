package me.ppvan.moon.ui.component


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import me.ppvan.moon.R
import me.ppvan.moon.data.model.Album
import me.ppvan.moon.data.model.Track
import me.ppvan.moon.ui.activity.Routes
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.theme.LightGrayColor
import me.ppvan.moon.ui.theme.gray_blue
import me.ppvan.moon.ui.theme.md_theme_light_onPrimary
import me.ppvan.moon.ui.view.home.SongListItem

@Composable
fun AlbumRow(
    albums: List<Album>,
    context: ViewContext,
    showSeeMore: Boolean,
) {
    val player = context.trackViewModel.player
    val maxVisibleItems = 6

    LazyRow(
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(albums.subList(0, minOf(maxVisibleItems, albums.size)),) { album ->
           AlbumRowTile(context = context, album = album) {
               context.navigator.navigate("album_page/${album.id}")
           }
        }
        if(showSeeMore) {
            item {
                Box(
                    modifier = Modifier
                        .size(150.dp, 150.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .clickable { context.navigator.navigate(Routes.Song.name) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray.copy(0.7f)) // Sửa lại thành màu sáng để phân biệt
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Xem Chi Tiết",
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(1.dp)
                                    .align(Alignment.Center)
                            )
                        }

                        Text(
                            text = "Xem Chi Tiết",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier
                                .padding(top = 1.dp)
                                .fillMaxWidth(),
                            maxLines = 2,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun AlbumRowTile(context: ViewContext, album: Album, onClick: () -> Unit) {
    Column() {
        Box(
            Modifier
                .size(150.dp, 150.dp)
                .clip(shape = RoundedCornerShape(15.dp))
                .clickable(onClick = onClick)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(album.thumbnailUri)
                    .error(R.drawable.thumbnail)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.thumbnail),
                contentDescription = "Music thumbnail",
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = album.name,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .width(150.dp)
                .padding(2.dp),
            textAlign = TextAlign.Center
        )

    }
}