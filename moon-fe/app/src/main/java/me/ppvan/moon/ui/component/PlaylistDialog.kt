package me.ppvan.moon.ui.component

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.ppvan.moon.data.model.Track
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.theme.MoonTheme

@Composable
fun AddToPlaylistDialog(
    context: ViewContext,
    songIds: List<Long>,
    onDismissRequest: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var showNewPlaylistDialog by remember { mutableStateOf(false) }
    val playlists = emptyList<Track>()

    PlayListDialogContent(onDismissRequest)

    if (showNewPlaylistDialog) {

    }
}

@Composable
fun PlayListDialogContent(
    onDismissRequest: () -> Unit
) {
    var showNewPlaylistDialog by remember {
        mutableStateOf(false)
    }
    val playlistEmpty = false

    ScaffoldDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = "Add to playlist")
        },
        content = {
            when (playlistEmpty) {
                false -> {
                    Box (Modifier.padding(8.dp)) {
                        Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "No play list found")
                    }
                }
                else -> {
                    Text(text = "Some playlist")
                }
            }

        },
        removeActionsHorizontalPadding = true,
        actions = {
            TextButton(
                modifier = Modifier.offset(y = (-8).dp),
                onClick = {
                    showNewPlaylistDialog = !showNewPlaylistDialog
                }
            ) {
                Text("New playlist")
            }
            Spacer(modifier = Modifier.weight(1f))
        },
    )

    if (showNewPlaylistDialog) {
        NewPlaylistDialog(onDismissRequest = {
            showNewPlaylistDialog = false
        }) {
            Log.d("INFO", "Playlist created")
        }
    }
}

@Composable
fun NewPlaylistDialog(
    onDismissRequest: () -> Unit,
    onDone: ()-> Unit
) {
    var input by remember { mutableStateOf("") }
    val songIds = remember { mutableStateListOf<Long>() }
    val songIdsImmutable = songIds.toList()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(LocalContext.current) {
        focusRequester.requestFocus()
    }

    ScaffoldDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = "New Playlist")
        },
        content = {
            Box(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 12.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = DividerDefaults.color,
                    ),
                    value = input,
                    onValueChange = {
                        input = it
                    }
                )
            }
        },
        actions = {
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                enabled = input.isNotBlank(),
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Done")
            }
        },
    )
}

@Preview
@Composable
fun PlaylistDialogPreview() {
    MoonTheme {
        PlayListDialogContent(onDismissRequest = {})
        NewPlaylistDialog(onDismissRequest = {}, onDone = {})
    }
}