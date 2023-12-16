package me.ppvan.moon.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import me.ppvan.moon.ui.component.CommonLoginButton
import me.ppvan.moon.ui.component.CommonText
import me.ppvan.moon.ui.component.CommonTextField
import me.ppvan.moon.ui.component.TopAppBarMinimalTitle
import me.ppvan.moon.ui.theme.PinkColor
import me.ppvan.moon.ui.viewmodel.LoginViewModel

@Composable
fun LoginScreen(loginViewModel: LoginViewModel = hiltViewModel()) {
    val email by loginViewModel.email.collectAsState()
    val password by loginViewModel.password.collectAsState()


    var isAuthenticationSuccessful by remember {
        mutableStateOf<Boolean?>(null)
    }
    var message by remember {
        mutableStateOf("")
    }

    var currentScreen by remember {
        mutableStateOf("Login")
    }

    if (currentScreen == "Register") {
        RegisterPreview()
    } else {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 30.dp, end = 30.dp, top = 20.dp, bottom = 20.dp)
                .navigationBarsPadding()
                .statusBarsPadding()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                TopAppBarMinimalTitle {
                    Text(text = "Log In")
                }
                Spacer(modifier = Modifier.height(20.dp))
                CommonTextField(
                    text = email,
                    placeholder = "Email",
                    onValueChange = loginViewModel::onEmailChange,
                    isPasswordTextField = false
                )
                Spacer(modifier = Modifier.height(16.dp))
                CommonTextField(
                    text = password,
                    placeholder = "Password",
                    onValueChange = loginViewModel::onPasswordChange,
                    isPasswordTextField = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Forgot Password?",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))
                CommonLoginButton(text = "Login", modifier = Modifier.fillMaxWidth()) {
                    loginViewModel.login()
                }
                Spacer(modifier = Modifier.weight(0.4f))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CommonText(
                        text = "Don't have an account,",
                        fontSize = 18.sp
                    ) {}
                    Spacer(modifier = Modifier.width(4.dp))
                    ClickableText(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = PinkColor,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.W500
                                )
                            ) {
                                append("Sign up")
                            }
                        },
                        onClick = {
                            currentScreen = "Register"
                        }
                    )
    //                CommonText(
    //                    text = "Sign Up",
    //                    color = PinkColor,
    //                    fontSize = 18.sp,
    //                    fontWeight = FontWeight.W500
    //                ) {
    ////                    navController.navigate("register_screen")
    //                }
                }

                if (isAuthenticationSuccessful != null) {
                    AlertDialog(
                        onDismissRequest = { isAuthenticationSuccessful = null },
                        title = { Text("Login Status") },
                        text = { Text(message) },
                        confirmButton = {
                            Button(
                                onClick = { isAuthenticationSuccessful = null },
                            ) {
                                Text("OK")
                            }
                        }
                    )
                }

                if (currentScreen == "Register") {
                    RegisterPreview()
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginPreview(){
    MaterialTheme {
        LoginScreen()
    }
}