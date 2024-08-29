package com.example.dailymacros.ui.screens.login

import android.util.Log
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
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


    Log.v("LoginScreen", "Logged user: ${loginViewModel.loggedUser.user}")

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
    // Biometric authentication setup
    val contextBiometric = LocalContext.current
    val biometricManager = remember { BiometricManager.from(contextBiometric) }
    val isBiometricAvailable = remember {
        biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
    }

    val executor = remember { ContextCompat.getMainExecutor(contextBiometric) }
    val biometricPrompt = remember {
        BiometricPrompt(
            contextBiometric as FragmentActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(contextBiometric, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Log.v("LoginScreen", "Biometric authentication succeeded")
                    Toast.makeText(contextBiometric, "Authentication succeeded!", Toast.LENGTH_SHORT).show()
                    Log.v("LoginScreen", "Biometric authentication succeeded, user: ${loginViewModel.loggedUser.user}")
                    loginViewModel.actions.setUser(loginViewModel.loggedUser.user!!)
                    navController.navigate(NavigationRoute.Diary.route)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(contextBiometric, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric authentication")
        .setSubtitle("Authenticate using your biometric data")
        .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        .build()

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
                modifier = Modifier.padding(16.dp),
                alignment = Alignment.Center
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
                    if(validateEmail(email.value) || email.value.isEmpty()) {
                        emailError.value = true
                    }
                    else if(validatePassword(password.value) || password.value.isEmpty()) {
                        passwordError.value = true
                    }
                    else {
                        Log.v("LoginScreen", "Email: ${email.value}, Password: ${password.value}, Sono dentro ")
                        loginViewModel.actions.login(email.value, password.value) {
                            if(loginViewModel.loggedUser.user != null) {
                                Log.v("LoginScreen", "Is Bio AV: ${isBiometricAvailable }")
                                if(isBiometricAvailable == BiometricManager.BIOMETRIC_SUCCESS) {
                                    biometricPrompt.authenticate(promptInfo)
                                    Log.v("LoginScreen", "Biometric authentication")
                                }
                                else {
                                    Log.v("LoginScreen", "Biometric authentication not available")
                                    loginViewModel.actions.setUser(loginViewModel.loggedUser.user!!)
                                    navController.navigate(NavigationRoute.Diary.route)
                                }
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
        }
        if(loginError.value) {
            Text(
                text = "Invalid credentials",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 8.dp)
                    .align(Alignment.BottomStart)
            )
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
                pushStringAnnotation(tag = "SIGNUP", annotation = "Sign up")
                withStyle(style = SpanStyle(color = Color.Blue)) {
                    append("Sign up")
                }
                pop()
            }
            ClickableText(
                text = annotatedText,
                onClick = { offset ->
                    annotatedText.getStringAnnotations(tag = "SIGNUP", start = offset, end = offset)
                        .firstOrNull()?.let {
                            navController.navigate(NavigationRoute.Signin.route)
                        }
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}