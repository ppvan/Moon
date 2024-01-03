package me.ppvan.moon.ui.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DriveFileRenameOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import me.ppvan.moon.R
import me.ppvan.moon.ui.activity.Routes
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.viewmodel.RegisterState
import me.ppvan.moon.ui.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(context: ViewContext, registerViewModel: RegisterViewModel = hiltViewModel()) {

    val firstName by registerViewModel.firstName.collectAsState()
    val lastName by registerViewModel.lastName.collectAsState()
    val email by registerViewModel.email.collectAsState()
    val password1 by registerViewModel.password1.collectAsState()
    val password2 by registerViewModel.password2.collectAsState()
    val message by registerViewModel.message.collectAsState()

    val uiState by registerViewModel.state.collectAsState()
    val currentContext = LocalContext.current
    var isPassword1Open by remember { mutableStateOf(false) }
    var isPassword2Open by remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        when (uiState) {
            RegisterState.IDLE -> {
                // do nothing
            }
            RegisterState.LOADING -> {
                // TODO: show loading ui
            }
            RegisterState.VALIDATION_ERROR -> {
                // TODO: show validation error
            }
            RegisterState.SUCCESS -> {
                Toast.makeText(currentContext, "Registration Successful", Toast.LENGTH_SHORT).show()
                context.navigator.navigate(Routes.Login.name)
            }
            RegisterState.FAILURE -> {
                Toast.makeText(currentContext, "Registration Failed. $message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(contentAlignment = Alignment.TopCenter) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(contentAlignment = Alignment.TopCenter,
                modifier = Modifier.background(Color.White)
                    .fillMaxHeight(0.25f)) {
                Image(
                    painter = painterResource(id = R.drawable.sign_up),
                    contentDescription = "Moon-App",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.background(Color.White).fillMaxHeight()
            ) {
                Text(
                    text = "Sign Up with Email",
                    color = Color.Black,
                    fontSize = 14.sp,
                )

                OutlinedTextField(
                    value = firstName,
                    onValueChange = {},

                    label = {
                        Text(
                            text = "First Name",
                        )
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 10.dp),

                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF3B82F6),
                        focusedLabelColor = Color(0xFF3B82F6),
                        unfocusedLabelColor = Color.Gray.copy(0.7f),
                        disabledLabelColor = Color(0xFF3B82F6),
                        focusedTextColor = Color(0xFF3B82F6),
                        unfocusedTextColor = Color.Gray.copy(0.7f),
                        focusedLeadingIconColor  = Color(0xFF3B82F6),
                        unfocusedLeadingIconColor  = Color.Gray.copy(0.7f),
                    ),

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),

                    singleLine = true,

                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.DriveFileRenameOutline,
                            contentDescription = "Email icon",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                )

                OutlinedTextField(
                    value = lastName,
                    onValueChange = {},

                    label = {
                        Text(
                            text = "Last Name",
                        )
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 10.dp),

                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF3B82F6),
                        focusedLabelColor = Color(0xFF3B82F6),
                        unfocusedLabelColor = Color.Gray.copy(0.7f),
                        disabledLabelColor = Color(0xFF3B82F6),
                        focusedTextColor = Color(0xFF3B82F6),
                        unfocusedTextColor = Color.Gray.copy(0.7f),
                        focusedLeadingIconColor  = Color(0xFF3B82F6),
                        unfocusedLeadingIconColor  = Color.Gray.copy(0.7f),
                    ),

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),

                    singleLine = true,

                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.DriveFileRenameOutline,
                            contentDescription = "Email icon",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = {},

                    label = {
                        Text(
                            text = "Email Address",
                        )
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 10.dp),

                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF3B82F6),
                        focusedLabelColor = Color(0xFF3B82F6),
                        unfocusedLabelColor = Color.Gray.copy(0.7f),
                        disabledLabelColor = Color(0xFF3B82F6),
                        focusedTextColor = Color(0xFF3B82F6),
                        unfocusedTextColor = Color.Gray.copy(0.7f),
                        focusedLeadingIconColor  = Color(0xFF3B82F6),
                        unfocusedLeadingIconColor  = Color.Gray.copy(0.7f),
                    ),

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),

                    singleLine = true,

                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_email),
                            contentDescription = "Email icon",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                )

                OutlinedTextField(
                    value = password1,
                    onValueChange = {},

                    label = {
                        Text(
                            text = "Password",
                        )
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 10.dp),

                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF3B82F6),
                        focusedLabelColor = Color(0xFF3B82F6),
                        unfocusedLabelColor = Color.Gray.copy(0.7f),
                        disabledLabelColor = Color(0xFF3B82F6),
                        focusedTextColor = Color(0xFF3B82F6),
                        unfocusedTextColor = Color.Gray.copy(0.7f),
                        focusedLeadingIconColor  = Color(0xFF3B82F6),
                        unfocusedLeadingIconColor  = Color.Gray.copy(0.7f),
                        focusedTrailingIconColor = Color(0xFF3B82F6),
                        unfocusedTrailingIconColor = Color.Gray.copy(0.7f),
                    ),

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),

                    singleLine = true,

                    visualTransformation = if (!isPassword1Open)
                        PasswordVisualTransformation()
                    else
                        VisualTransformation.None,

                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_password),
                            contentDescription = "Password icon",
                            modifier = Modifier.size(24.dp)
                        )
                    },

                    trailingIcon = {
                        IconButton(onClick = {
                            isPassword1Open = !isPassword1Open
                        }) {
                            if (!isPassword1Open) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_eyeopen),
                                    contentDescription = "Eye open icon ",

                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_eyeclose),
                                    contentDescription = "Eye close icon ",

                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                )

                OutlinedTextField(
                    value = password2,
                    onValueChange = {},

                    label = {
                        Text(
                            text = "Password (confirm)",
                        )
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 10.dp),

                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF3B82F6),
                        focusedLabelColor = Color(0xFF3B82F6),
                        unfocusedLabelColor = Color.Gray.copy(0.7f),
                        disabledLabelColor = Color(0xFF3B82F6),
                        focusedTextColor = Color(0xFF3B82F6),
                        unfocusedTextColor = Color.Gray.copy(0.7f),
                        focusedLeadingIconColor  = Color(0xFF3B82F6),
                        unfocusedLeadingIconColor  = Color.Gray.copy(0.7f),
                        focusedTrailingIconColor = Color(0xFF3B82F6),
                        unfocusedTrailingIconColor = Color.Gray.copy(0.7f),
                    ),

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),

                    singleLine = true,

                    visualTransformation = if (!isPassword2Open)
                        PasswordVisualTransformation()
                    else
                        VisualTransformation.None,

                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_password),
                            contentDescription = "Password icon",
                            modifier = Modifier.size(24.dp)
                        )
                    },

                    trailingIcon = {
                        IconButton(onClick = {
                            isPassword2Open = !isPassword2Open
                        }) {
                            if (!isPassword2Open) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_eyeopen),
                                    contentDescription = "Eye open icon ",

                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_eyeclose),
                                    contentDescription = "Eye close icon ",

                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                )

                Button(
                    onClick = {  },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3B82F6),
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(vertical = 14.dp)
                ) {
                    Text(
                        text = "Register"
                    )

                }

                Spacer(modifier = Modifier.padding(top = 26.dp))

                TextButton(
                    onClick = {},
                    contentPadding = PaddingValues(vertical = 0.dp)
                ) {
                    Text(
                        text = "Forgot Password ?",
                        color = Color.LightGray.copy(0.7f),
                        fontSize = 12.sp
                    )
                }

                TextButton(
                    onClick = {context.navigator.navigate(Routes.Login.name)},
                    contentPadding = PaddingValues(vertical = 0.dp)
                ) {
                    Text(
                        text = "Already have an Account ? Sign In",
                        color = Color.LightGray.copy(0.7f),
                        fontSize = 12.sp,
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun RegisterPreview(){
    MaterialTheme {
//        RegisterScreen()
    }
}