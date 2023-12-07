package me.ppvan.moon.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.ppvan.moon.R
import me.ppvan.moon.data.model.Album
import me.ppvan.moon.data.model.Track
import me.ppvan.moon.ui.activity.Routes
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.view.home.SongListItem

@Composable
fun AlbumList(
    context: ViewContext,
    albums: List<Album> = List(10) { Album.default() },
    maxVisibleItems: Int = 6
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 16.dp, top = 4.dp),
    ) {

        albums.take(maxVisibleItems).forEach { album ->
            AlbumItem(album, context) {

            }
        }
    }
    Text(
        "Xem thêm...",
        modifier =  Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(50.dp)
            .background(
                Color.LightGray.copy(0.7f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable {
                context.navigator.navigate(Routes.Album.name)
            }
            .wrapContentHeight(Alignment.CenterVertically),
        textAlign = TextAlign.Center
    )
}
