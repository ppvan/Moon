package me.ppvan.moon.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.ppvan.moon.R
import me.ppvan.moon.data.model.Track
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.component.CenterTopAppBarAction


@Composable
fun TagEditView(context: ViewContext, mediaId: String) {

    val track = Track.DEFAULT
    var fileName by remember { mutableStateOf("") }
    var artist by remember { mutableStateOf(track.artist) }
    var musicTitle by remember { mutableStateOf(track.title) }
    var album by remember { mutableStateOf(track.album) }
    var albumArtist by remember { mutableStateOf("") }
    var trackNum by remember { mutableStateOf("") }
    var discNum by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }

    val fillWidth = Modifier.fillMaxWidth()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterTopAppBarAction(
                title = "Edit tags",
                navigationIcon = {
                    IconButton(onClick = {
                        context.navigator.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.Save, contentDescription = "Save")
                    }
                }
            )

        },
        content = { contentPadding ->
            Surface(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.padding(4.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = mediaId)
                    CoverField("") {

                    }
                    TagField(
                        modifier = fillWidth,
                        value = fileName,
                        onValueChange = { fileName = it },
                        label = {
                            Text(text = "File name")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.AudioFile,
                                contentDescription = "music Icon"
                            )
                        }
                    )

                    TagField(
                        modifier = fillWidth,
                        value = musicTitle,
                        onValueChange = { musicTitle = it },
                        label = {
                            Text(text = "Title")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Title,
                                contentDescription = "Title"
                            )
                        }
                    )
                    TagField(
                        modifier = fillWidth,
                        value = artist,
                        onValueChange = { artist = it },
                        label = {
                            Text(text = "Artist")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "artist"
                            )
                        }
                    )

                    TagField(
                        modifier = fillWidth,
                        value = album,
                        onValueChange = { album = it },
                        label = {
                            Text(text = "Album")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Album,
                                contentDescription = "album"
                            )
                        }
                    )

                    Row(
                        modifier = fillWidth
                    ) {
                        TagField(
                            modifier = Modifier.weight(1f),
                            value = trackNum,
                            onValueChange = { trackNum = it },
                            label = {
                                Text(text = "Track number")
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Pin,
                                    contentDescription = "Track number"
                                )
                            }
                        )
                        TagField(
                            modifier = Modifier.weight(1f),
                            value = discNum,
                            onValueChange = { discNum = it },
                            label = {
                                Text(text = "Disc number")
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Pin,
                                    contentDescription = "Disc number"
                                )
                            }
                        )
                    }

                    TagField(
                        modifier = fillWidth,
                        value = comment,
                        label = { Text(text = "Comment") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.AudioFile,
                                contentDescription = "music Icon"
                            )
                        },
                        onValueChange = { comment = it }
                    )
                }
            }
        }
    )

}

@Composable
fun TagField(
    modifier: Modifier = Modifier,
    value: String,
    label: @Composable () -> Unit,
    leadingIcon: @Composable () -> Unit,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier
            .padding(4.dp),
        value = value,
        label = label,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        leadingIcon = leadingIcon,
    )
}

@Composable
fun CoverField(
    contentUrl: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .height(144.dp)
                .aspectRatio(1f)
                .padding(4.dp)
                .clickable { onClick() },
            contentAlignment = Alignment.BottomEnd
        ) {
            Image(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(CircleShape),
                painter = painterResource(id = R.drawable.bocchi),
                contentDescription = "cover",
                contentScale = ContentScale.Crop
            )
            Icon(imageVector = Icons.Filled.AddPhotoAlternate, contentDescription = null)
        }
    }
}

@Composable
fun EditFormBody() {

}

@Preview
@Composable
fun FormPreview() {
    Surface {
        //FormEditView()
//        TagEditView()
    }
}