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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.ppvan.moon.ui.component.CommonLoginButton
import me.ppvan.moon.ui.component.CommonText
import me.ppvan.moon.ui.component.CommonTextField
import me.ppvan.moon.ui.component.TopAppBarMinimalTitle
import me.ppvan.moon.ui.theme.PinkColor

@Composable
//navController: NavController
fun RegisterScreen() {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var isRegistrationSuccessful by remember {
        mutableStateOf<Boolean?>(null)
    }
    var message by remember {
        mutableStateOf("")
    }

    var currentScreen by remember {
        mutableStateOf("Register")
    }

    if (currentScreen == "Login") {
        LoginPreview()
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
                    Text(text = "Sign Up")
                }
                Spacer(modifier = Modifier.height(20.dp))
                CommonTextField(
                    text = firstName,
                    placeholder = "First Name",
                    onValueChange = { firstName = it },
                    isPasswordTextField = false
                )
                Spacer(modifier = Modifier.height(20.dp))
                CommonTextField(
                    text = lastName,
                    placeholder = "Last Name",
                    onValueChange = { lastName = it },
                    isPasswordTextField = false
                )
                Spacer(modifier = Modifier.height(16.dp))
                CommonTextField(
                    text = email,
                    placeholder = "Email",
                    onValueChange = { email = it },
                    isPasswordTextField = false
                )
                Spacer(modifier = Modifier.height(16.dp))
                CommonTextField(
                    text = password,
                    placeholder = "Password",
                    onValueChange = { password = it },
                    isPasswordTextField = true
                )
    //            Spacer(modifier = Modifier.height(16.dp))
    //            CommonTextField(
    //                text = confirmPassword,
    //                placeholder = "Confirm Password",
    //                onValueChange = { confirmPassword = it },
    //                isPasswordTextField = true
    //            )
                Spacer(modifier = Modifier.weight(0.2f))
                CommonLoginButton(text = "Register", modifier = Modifier.fillMaxWidth()) {

                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CommonText(
                        text = "I already have an account,",
                        fontSize = 18.sp,
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
                                append("Sign in")
                            }
                        },
                        onClick = {
                            currentScreen = "Login"
                        }
                    )
    //                CommonText(
    //                    text = "Sign In",
    //                    color = PinkColor,
    //                    fontSize = 18.sp,
    //                    fontWeight = FontWeight.W500
    //
    //                ) {}
                }

                if (isRegistrationSuccessful != null) {
                    AlertDialog(
                        onDismissRequest = { isRegistrationSuccessful = null },
                        title = { Text("Registration Status") },
                        text = { Text(message) },
                        confirmButton = {
                            Button(
                                onClick = { isRegistrationSuccessful = null },
                            ) {
                                Text("OK")
                            }
                        }
                    )
                }

                if (currentScreen == "Login") {
                    LoginPreview()
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun RegisterPreview(){
    MaterialTheme {
        RegisterScreen()
    }
}