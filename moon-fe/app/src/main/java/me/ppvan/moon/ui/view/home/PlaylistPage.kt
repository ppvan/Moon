package me.ppvan.moon.ui.view.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun PlaylistPage() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Cyan)) {
        Text(text = "PlaylistPage")
    }
}