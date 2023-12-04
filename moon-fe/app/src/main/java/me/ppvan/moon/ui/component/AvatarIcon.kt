package me.ppvan.moon.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun AvatarIcon(imageResource: Int, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                .clip(CircleShape)
        )
    }
}