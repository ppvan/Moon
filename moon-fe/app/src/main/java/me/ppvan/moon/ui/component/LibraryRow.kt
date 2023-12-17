package me.ppvan.moon.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PeopleOutline
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import me.ppvan.moon.ui.activity.Routes
import me.ppvan.moon.ui.activity.ViewContext


@Composable
fun LibraryRow(
    context: ViewContext,
) { // Set up the tabs

    Column {

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(MusicTab.values()) { tab ->
                ElevatedCard(
                    modifier = Modifier
                        .width(150.dp)
                        .height(100.dp)
                        .clickable {
                            handleTabClick(tab,context)
                        },
                    ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Icon ở trên
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .padding(8.dp)
                        )

                        // Text tiêu đề ở dưới
                        Text(
                            text = tab.label,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth(),
                            maxLines = 2,
                        )
                    }
                }
            }
        }
    }
}

enum class MusicTab(val label: String, val icon: ImageVector) {
    Favorite("Favourite", Icons.Default.FavoriteBorder),
    Downloaded("Download", Icons.Default.ArrowCircleDown),
    Artist("Artist", Icons.Default.PeopleOutline),
    Upload("Upload", Icons.Default.CloudUpload)
}
fun handleTabClick(tab: MusicTab, context: ViewContext) {
    when (tab) {
        MusicTab.Favorite -> {
            // Xử lý khi nhấn vào tab Bài hát yêu thích
        }
        MusicTab.Downloaded -> {
            // Xử lý khi nhấn vào tab Đã tải xuống
        }
        MusicTab.Artist -> {
            context.navigator.navigate(Routes.Artist.name)
        }
        MusicTab.Upload -> {
            context.navigator.navigate(Routes.Upload.name)
        }
    }
}