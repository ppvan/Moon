package me.ppvan.moon.ui.view

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import me.ppvan.moon.R
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.component.CenterTopAppBarAction


@Composable
fun TagEditView(
    viewContext: ViewContext,
    mediaId: String
) {
    LaunchedEffect(key1 = "mediaId") {
        viewContext.tagEditViewModel.loadTrack(mediaId)
    }

    val context = LocalContext.current
    val tagEditViewModel = viewContext.tagEditViewModel
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {uri ->
            tagEditViewModel.onCoverChange(uri.toString())
        }
    )

    val cover by tagEditViewModel.cover.collectAsState()
    val artist by tagEditViewModel.artist.collectAsState()
    val title by tagEditViewModel.title.collectAsState()
    val album by tagEditViewModel.album.collectAsState()
    val trackNum by tagEditViewModel.trackNumber.collectAsState()
    val discNum by tagEditViewModel.discNumber.collectAsState()
    val comment by tagEditViewModel.comment.collectAsState()

    val fillWidth = Modifier.fillMaxWidth()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterTopAppBarAction(
                title = "Edit tags",
                navigationIcon = {
                    IconButton(onClick = {
                        viewContext.navigator.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        tagEditViewModel.onSaveRequest()
//                        viewContext.navigator.popBackStack()
                        Toast.makeText(context, "Tag Updated", Toast.LENGTH_SHORT).show()
                    }) {
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
                    CoverField(cover) {
                        photoPicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }

                    TagField(
                        modifier = fillWidth,
                        value = title,
                        onValueChange = tagEditViewModel::onTitleChange,
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
                        onValueChange = tagEditViewModel::onArtistChange,
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
                        onValueChange = tagEditViewModel::onAlbumChange,
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
                            onValueChange = tagEditViewModel::onTrackNumberChange,
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
                            onValueChange = tagEditViewModel::onDiscNumberChange,
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
                        onValueChange = tagEditViewModel::onCommentChange
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

            AsyncImage(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(CircleShape),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(contentUrl)
                    .error(R.drawable.thumbnail)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.thumbnail),
                contentDescription = "Music thumbnail",
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