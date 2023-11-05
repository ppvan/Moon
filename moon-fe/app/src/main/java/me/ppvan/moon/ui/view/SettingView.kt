package me.ppvan.moon.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import me.ppvan.moon.ui.activity.ViewContext

@Composable
fun SettingView(context: ViewContext) {

    Column {
        Text(text = "Setting 1")
        Text(text = "Setting 2")
    }
}