package me.ppvan.moon.ui.view

import android.widget.Toast
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
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
import me.ppvan.moon.ui.viewmodel.RegisterState
import me.ppvan.moon.ui.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(registerViewModel: RegisterViewModel = hiltViewModel()) {

    val firstName by registerViewModel.firstName.collectAsState()
    val lastName by registerViewModel.lastName.collectAsState()
    val email by registerViewModel.email.collectAsState()
    val password1 by registerViewModel.password1.collectAsState()
    val password2 by registerViewModel.password2.collectAsState()
    val message by registerViewModel.message.collectAsState()

    val uiState by registerViewModel.state.collectAsState()
    val context = LocalContext.current

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
                Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
            }
            RegisterState.FAILURE -> {
                Toast.makeText(context, "Registration Failed. $message", Toast.LENGTH_SHORT).show()
            }
        }
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
                    onValueChange = registerViewModel::onFirstNameChanged,
                    isPasswordTextField = false
                )
                Spacer(modifier = Modifier.height(20.dp))
                CommonTextField(
                    text = lastName,
                    placeholder = "Last Name",
                    onValueChange = registerViewModel::onLastNameChanged,
                    isPasswordTextField = false
                )
                Spacer(modifier = Modifier.height(16.dp))
                CommonTextField(
                    text = email,
                    placeholder = "Email",
                    onValueChange = registerViewModel::onEmailChanged,
                    isPasswordTextField = false
                )
                Spacer(modifier = Modifier.height(16.dp))
                CommonTextField(
                    text = password1,
                    placeholder = "Password",
                    onValueChange = registerViewModel::onPassword1Changed,
                    isPasswordTextField = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                CommonTextField(
                    text = password2,
                    placeholder = "Confirm Password",
                    onValueChange = registerViewModel::onPassword2Changed,
                    isPasswordTextField = true
                )
                Spacer(modifier = Modifier.weight(0.2f))
                CommonLoginButton(text = "Register", modifier = Modifier.fillMaxWidth()) {
                    registerViewModel.register()
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