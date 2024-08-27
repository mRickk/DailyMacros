package com.example.dailymacros.ui.screens.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavHostController

import androidx.compose.foundation.clickable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.DialogHost
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.dailymacros.R
import com.example.dailymacros.ui.NavigationRoute
import com.example.dailymacros.ui.screens.settings.SettingsViewModel

@Composable
fun Login(navController: NavHostController,
          loginViewModel: LoginViewModel) {

    loginViewModel.actions.setUser()
    Log.v("LoginScreen", "Logged user: ${loginViewModel.loggedUser.user}")

    val user = loginViewModel.loggedUser.user

    LaunchedEffect(user) {
        if (user != null) {
            loginViewModel.actions.setUser(loginViewModel.loggedUser.user!!)
            navController.navigate(NavigationRoute.Diary.route)
        }
    }

    val content = LocalContext.current
    val email = remember { mutableStateOf("") }
    val emailError = remember { mutableStateOf(false) }
    val password = remember { mutableStateOf("") }
    val passwordError = remember { mutableStateOf(false) }
    val loginError = remember { mutableStateOf(false) }

    fun validateEmail(email: String): Boolean {
        val regex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$")
        return !regex.matches(email)
    }

    fun validatePassword(password: String): Boolean {
        return password.length < 8
    }

    //TODO: Add Biometric authentication

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = R.drawable.icon),
                contentDescription = "App Icon",
                modifier = Modifier.padding(horizontal = 70.dp, vertical = 50.dp),
                alignment = Alignment.TopCenter
            )

            TextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Email") },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    color = Color.Black
                )
            )
            if(emailError.value) {
                Text(
                    text = "Invalid mail",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            }
            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                textStyle = TextStyle(
                    color = Color.Black
                )
            )
            if(passwordError.value) {
                Text(
                    text = "Invalid password",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            }
            Button(
                onClick = {
                    emailError.value = false
                    passwordError.value = false
                    loginError.value = false
                    if(validateEmail(email.value) || email.value.isEmpty()) {
                        emailError.value = true
                    }
                    else if(validatePassword(password.value) || password.value.isEmpty()) {
                        passwordError.value = true
                    }
                    else {
                        loginViewModel.actions.login(email.value, password.value) {
                            if(loginViewModel.loggedUser.user != null) {
                                loginViewModel.actions.setUser(loginViewModel.loggedUser.user!!)
                                navController.navigate(NavigationRoute.Diary.route)
                            } else {
                                loginError.value = true
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Login")
            }
            if(loginError.value) {
                Text(
                    text = "Invalid credentials",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 8.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            val annotatedText = buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                    append("New to our app? ")
                }
                pushStringAnnotation(tag = "SIGNIN", annotation = "Sign in")
                withStyle(style = SpanStyle(color = Color.Blue)) {
                    append("Sign up")
                }
                pop()
            }
            ClickableText(
                text = annotatedText,
                onClick = { offset ->
                    annotatedText.getStringAnnotations(tag = "SIGNIN", start = offset, end = offset)
                        .firstOrNull()?.let {
                            navController.navigate(NavigationRoute.Signin.route)
                        }
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}