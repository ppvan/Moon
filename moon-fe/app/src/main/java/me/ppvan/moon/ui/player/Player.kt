package me.ppvan.moon.ui.player

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import me.ppvan.moon.R
import me.ppvan.moon.ui.theme.MoonTheme


@Composable
fun Player(viewmodel: PlayerViewModel = hiltViewModel(), modifier: Modifier = Modifier) {

    Row(
        modifier = modifier
            .background(Color(0xff2d3436))
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Box (
                modifier
                    .size(50.dp, 50.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(Color.Red)) {
                Image(painter = painterResource(id = R.drawable.bocchi), contentDescription = "Song thumbnail")
            }
        }

        Row (
            modifier = modifier
                .weight(1f)
                .padding(12.dp, 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(text = "By My Side")
                Text(text = "Alan Walker", fontSize = 12.sp)
            }

            Box () {
                Icon(painter = painterResource(id = R.drawable.play_24), contentDescription = "Play icon")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MoonTheme {
        Player()
    }
}