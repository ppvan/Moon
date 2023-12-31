package me.ppvan.moon.ui.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import me.ppvan.moon.R
import me.ppvan.moon.ui.activity.Routes
import me.ppvan.moon.ui.activity.ViewContext
import me.ppvan.moon.ui.component.CommonLoginButton
import me.ppvan.moon.ui.component.CommonText
import me.ppvan.moon.ui.component.CommonTextField
import me.ppvan.moon.ui.component.TopAppBarMinimalTitle
import me.ppvan.moon.ui.theme.PinkColor
import me.ppvan.moon.ui.viewmodel.LoginState
import me.ppvan.moon.ui.viewmodel.LoginViewModel

@Composable
fun LoginScreen(context: ViewContext, loginViewModel: LoginViewModel = hiltViewModel()) {
    val email by loginViewModel.email.collectAsState()
    val password by loginViewModel.password.collectAsState()
    val state by loginViewModel.state.collectAsState()
    val currentContext = LocalContext.current
    var isPasswordOpen by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        when (state) {
            LoginState.IDLE -> {
                // do nothing
            }
            LoginState.LOADING -> {
                // TODO: show loading ui
            }
            LoginState.SUCCESS -> {
                Toast.makeText(currentContext, "Login Successful", Toast.LENGTH_SHORT).show()
                context.navigator.navigate(Routes.Home.name)
            }
            LoginState.FAILURE -> {
                Toast.makeText(currentContext, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(contentAlignment = Alignment.TopCenter) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(contentAlignment = Alignment.TopCenter) {
                Image(
                    painter = painterResource(id = R.drawable.login_pic),
                    contentDescription = "Moon-App",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                ,
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 0.dp
                ),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Log In with Email",
                        color = Color.Black,
                        fontSize = 14.sp,
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            loginViewModel::onEmailChange
                        },

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
                        value = password,
                        onValueChange = {
                            loginViewModel::onPasswordChange
                        },

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

                        visualTransformation = if (!isPasswordOpen)
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
                                isPasswordOpen = !isPasswordOpen
                            }) {
                                if (!isPasswordOpen) {
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
                        onClick = { loginViewModel.login() },
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
                            text = "Login"
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
                        onClick = {context.navigator.navigate(Routes.Register.name)},
                        contentPadding = PaddingValues(vertical = 0.dp)
                    ) {
                        Text(
                            text = "Don't have an Account ? Sign Up",
                            color = Color.LightGray.copy(0.7f),
                            fontSize = 12.sp,
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}