package me.ppvan.moon.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.ppvan.moon.R
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.component.CenterTopAppBar
import me.ppvan.moon.ui.component.EditEmailDialog
import me.ppvan.moon.ui.component.EditNameDialog
import me.ppvan.moon.ui.component.OptionRow
import me.ppvan.moon.ui.component.UpdateSuccessfulDialog
import me.ppvan.moon.ui.component.UserImage


@Composable
fun ProfileView(context: ViewContext) {
    val profileViewModel = context.profileViewModel
    val user by profileViewModel.loggedInUser
    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var isEditingName by remember { mutableStateOf(false) }
    var isEditingEmail by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf(name) }
    var newEmail by remember { mutableStateOf(email) }
    var isUpdateSuccessfulDialogOpen by remember { mutableStateOf(false) }

    // Biến để theo dõi xem có sự thay đổi nào đã được thực hiện hay không
    var isChangesMade by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterTopAppBar(
                title = "Profile",
                navigationIcon = {
                    IconButton(onClick = { context.navigator.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                menuItems = {
                    // Empty for now, you can add menu items if needed
                }
            )
        },
        content = { contentPadding ->
            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    UserImage(R.drawable.bocchi)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
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
                    OptionRow("Name", name) {
                        isEditingName = true
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Option: Email with Edit button
                    OptionRow("Email", email) {
                        isEditingEmail = true
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
                                // Handle Save button click
                                if (isChangesMade) {
                                    isEditingName = false
                                    isEditingEmail = false
                                    isChangesMade = false
                                    isUpdateSuccessfulDialogOpen = true
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            enabled = isChangesMade // Kích hoạt/Ẩn nút tùy thuộc vào sự thay đổi
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
            }
        }
    )
    if (isUpdateSuccessfulDialogOpen) {
        UpdateSuccessfulDialog {
            // Xác nhận và đóng dialog
            isUpdateSuccessfulDialogOpen = false
            profileViewModel.updateName(newName)
            profileViewModel.updateEmail(newEmail)
        }
    }
    if (isEditingName) {
        EditNameDialog(
            currentName = name,
            onNameChanged = { newName ->
                name = newName
                isChangesMade = true
                isEditingName = false
            },
            onCancel = {
                isEditingName = false
            }
        )
    }

    if (isEditingEmail) {
        EditEmailDialog(
            currentEmail = email,
            onEmailChanged = { newEmail ->
                email = newEmail
                isChangesMade = true
                isEditingEmail = false
            },
            onCancel = {
                isEditingEmail = false
            }
        )
    }
}



