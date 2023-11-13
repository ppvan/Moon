package me.ppvan.moon.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import me.ppvan.moon.ui.activity.ViewContext

@Composable
fun DownloadView(context: ViewContext) {
    Text(text = "Download")
    Box(modifier = Modifier.fillMaxSize().background(Color.Red))
}