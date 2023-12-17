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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
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
                        context.navigator.navigate(Routes.Register.name)
                    }
                )
            }
        }
    }
}