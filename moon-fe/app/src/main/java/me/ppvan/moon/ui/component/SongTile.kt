package me.ppvan.moon.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import me.ppvan.moon.R
import me.ppvan.moon.data.model.Track
import me.ppvan.moon.ui.activity.ViewContext

@Composable
fun SongTile(context: ViewContext, track: Track, onClick: () -> Unit) {
    Column() {
        Box(
            Modifier
                .size(100.dp, 100.dp)
                .clip(shape = RoundedCornerShape(15.dp))
                .clickable(onClick = onClick)
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
        Text(
            text = track.title,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(100.dp)
                .padding(2.dp),
            textAlign = TextAlign.Center
        )

    }
}
