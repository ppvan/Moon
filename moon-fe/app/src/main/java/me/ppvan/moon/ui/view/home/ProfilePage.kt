package me.ppvan.moon.ui.view.home

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import me.ppvan.moon.R
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.component.EditEmailDialog
import me.ppvan.moon.ui.component.EditNameDialog
import me.ppvan.moon.ui.component.OptionRow

@Composable
fun ProfilePage(context: ViewContext) {
    val profileViewModel = context.profileViewModel
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            profileViewModel.onAvatarUrlChange(uri.toString())
        }
    )

    val firstName by profileViewModel.firstName.collectAsState()
    val lastName by profileViewModel.lastName.collectAsState()
    val avatarUrl by profileViewModel.avatarUrl.collectAsState()

    val editFirstName by profileViewModel.editFirstName.collectAsState()
    val editLastName by profileViewModel.editLastName.collectAsState()
    val currentContext = LocalContext.current

    var isChangesMade by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxSize(), color = MaterialTheme.colorScheme.surface
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(avatarUrl)
                    .error(R.drawable.bocchi).build(),
                contentScale = ContentScale.Crop,
                contentDescription = "User Avatar",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(250.dp),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        photoPicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )

                        isChangesMade = true
                    }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier
                        .size(16.dp)
                        .padding(end = 4.dp)
                )
                Text(
                    text = "Change profile picture",
                    color = MaterialTheme.colorScheme.primary,
                )
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Option: Name with Edit button
            OptionRow("FirstName", firstName) {
                profileViewModel.toggleEditFirstName(true)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Option: Email with Edit button
            OptionRow("LastName", lastName) {
                profileViewModel.toggleEditLastName(true)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Save and Log Out buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Nút "Save" với kiểu khác nhau tùy thuộc vào sự thay đổi đã được thực hiện hay không
                Button(
                    onClick = {
                        profileViewModel.saveProfile()
                        isChangesMade = false
                        Toast.makeText(currentContext, "Profile Saved", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    enabled = isChangesMade
                ) {
                    Text("Save")
                }

                // Nút "Log Out"
                Button(
                    onClick = {
                        // Handle Log Out button click
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Text("Log Out")
                }
            }

        }
        if (editFirstName) {
            EditNameDialog(
                currentName = firstName,
                onNameChanged = { newName ->
                    profileViewModel.onFirstNameChange(newName)
                    isChangesMade = true
                    profileViewModel.toggleEditFirstName(false)
                },
                onCancel = {
                    profileViewModel.toggleEditFirstName(false)
                }
            )
        }

        if (editLastName) {
            EditEmailDialog(
                currentEmail = lastName,
                onEmailChanged = { newLastName ->
                    profileViewModel.onLastNameChange(newLastName)
                    isChangesMade = true
                    profileViewModel.toggleEditLastName(false)
                },
                onCancel = {
                    profileViewModel.toggleEditLastName(false)
                }
            )
        }
    }
}
