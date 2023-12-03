package me.ppvan.moon.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EditEmailDialog(
    currentEmail: String,
    onEmailChanged: (String) -> Unit,
    onCancel: () -> Unit
) {
    var newEmail by remember { mutableStateOf(currentEmail) }

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Change your email") },
        text = {
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newEmail,
                    onValueChange = { newEmail = it },
                    label = { Text("New email") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onEmailChanged(newEmail)

                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(
                onClick = onCancel
            ) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun EditNameDialog(
    currentName: String,
    onNameChanged: (String) -> Unit,
    onCancel: () -> Unit
) {
    var newName by remember { mutableStateOf(currentName) }

    AlertDialog(
        modifier = Modifier,
        onDismissRequest = onCancel,
        title = { Text("Change your name") },
        text = {
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("New name") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onNameChanged(newName)

                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(
                onClick = onCancel
            ) {
                Text("Cancel")
            }
        }
    )
}





@Composable
fun UpdateSuccessfulDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update Successful") },
        text = { Text("Your information has been updated successfully.") },
        confirmButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("OK")
            }
        }
    )
}