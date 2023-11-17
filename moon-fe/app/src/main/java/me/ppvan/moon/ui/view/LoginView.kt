package me.ppvan.moon.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import me.ppvan.moon.ui.component.CommonLoginButton
import me.ppvan.moon.ui.component.CommonText
import me.ppvan.moon.ui.component.CommonTextField
import me.ppvan.moon.ui.component.TopAppBarMinimalTitle
import me.ppvan.moon.ui.theme.LightGrayColor
import me.ppvan.moon.ui.theme.PinkColor

@Composable
//navController: NavController
fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 30.dp, end = 30.dp, top = 20.dp, bottom = 20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            TopAppBarMinimalTitle {
                Text(text = "Log in")
            }
            Spacer(modifier = Modifier.height(20.dp))
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
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Forgot Password?",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            CommonLoginButton(text = "Login", modifier = Modifier.fillMaxWidth()) {}
            Spacer(modifier = Modifier.weight(0.4f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                CommonText(text = "I'm a new user,", fontSize = 18.sp) {}
                Spacer(modifier = Modifier.width(4.dp))
                CommonText(
                    text = "Sign Up",
                    color = PinkColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                ) {
//                    navController.navigate("register_screen")
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginPreview(){
    LoginScreen()
}