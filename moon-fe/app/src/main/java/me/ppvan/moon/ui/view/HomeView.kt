package me.ppvan.moon.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import me.ppvan.moon.ui.activity.Routes
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.component.CenterTopAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(context: ViewContext) {
    var presses by remember { mutableIntStateOf(0) }
    var selectedTab by remember { mutableStateOf(MoonPages.Home) }

    Scaffold(
        topBar = {
            CenterTopAppBar(
                title = "Home",
                menuItems = {
                    DropdownMenuItem(
                        text = { Text("ReScan") },
                        onClick = { /* Handle edit! */ },
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
        bottomBar = {
            NavigationBar {
                for (tab in MoonPages.values()) {
                    NavigationBarItem(
                        icon = { Icon(tab.icon, contentDescription = tab.label,) },
                        label = { Text(tab.label) },
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab }
                    )
                }
            }
        },

        floatingActionButton = {
            FloatingActionButton(onClick = { presses++ }) {
                Icon(Icons.Default.Shuffle, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            when(selectedTab) {
                MoonPages.Home -> Text(text = "Home")
                MoonPages.Song -> Text(text = "Song")
                MoonPages.Album -> Text(text = "Album")
                MoonPages.Artist -> Text(text = "Artist")
            }
        }
    }
}

enum class MoonPages constructor(val label: String, val icon: ImageVector) {

    Home("Home", Icons.Filled.Home),
    Song("Song", Icons.Filled.MusicNote),
    Album("Album", Icons.Filled.Album),
    Artist("Artist", Icons.Filled.People),
    ;
}