package me.ppvan.moon.ui.component


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.ppvan.moon.ui.activity.ViewContext


@Composable
fun PlaylistAlbumTabRow(context: ViewContext) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Album", "Playlist")


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // TabRow để chứa các tab
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, title ->
                val textColor = if (selectedTabIndex == index) Color.Black else Color.Gray.copy(0.7f)
                Text(
                    color= textColor,
                    text = title,
                    modifier = Modifier
                        .clickable {
                            selectedTabIndex = index
                        }
                        .padding(16.dp)
                        .wrapContentSize(Alignment.Center)
                        .drawBehind {
                            if (selectedTabIndex == index) {
                                drawLine(
                                    color = Color(0xFFFFB3B5),
                                    start = Offset(3f, size.height),
                                    end = Offset(size.width, size.height),
                                    strokeWidth = 6f
                                )
                            }
                        }

                )
            }
        }

        when (selectedTabIndex) {
            0 -> AlbumList(context = context)
            1 -> PlayList(context = context)
        }
    }
}