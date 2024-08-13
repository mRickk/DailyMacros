package com.example.dailymacros.ui.screens.signup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dailymacros.ui.NavigationRoute
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions

@Composable
fun Signup(navController: NavHostController) {
    val email = remember { mutableStateOf("") }
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val gender = remember { mutableStateOf("Male") }
    val age = remember { mutableStateOf("") }
    val weight = remember { mutableStateOf("") }
    val height = remember { mutableStateOf("") }
    val activity = remember { mutableStateOf("Low") }
    val goal = remember { mutableStateOf("Lose Weight") }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .padding(top = 200.dp) // Adjust this value to start from the middle
        ) {
            TextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Email") },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                textStyle = TextStyle(color = Color.Black)
            )
            TextField(
                value = username.value,
                onValueChange = { username.value = it },
                label = { Text("Username") },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                textStyle = TextStyle(color = Color.Black)
            )
            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                textStyle = TextStyle(color = Color.Black)
            )
            TextField(
                value = confirmPassword.value,
                onValueChange = { confirmPassword.value = it },
                label = { Text("Confirm Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                textStyle = TextStyle(color = Color.Black)
            )
            Text("Gender", style = MaterialTheme.typography.bodyMedium)
            RadioButtonGroup(
                options = listOf("Male", "Female", "Other"),
                selectedOption = gender.value,
                onOptionSelected = { gender.value = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = age.value,
                onValueChange = { age.value = it },
                label = { Text("Age") },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                textStyle = TextStyle(color = Color.Black)
            )
            TextField(
                value = weight.value,
                onValueChange = { weight.value = it },
                label = { Text("Weight") },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                textStyle = TextStyle(color = Color.Black)
            )
            TextField(
                value = height.value,
                onValueChange = { height.value = it },
                label = { Text("Height") },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                textStyle = TextStyle(color = Color.Black)
            )
            Text("Activity Level", style = MaterialTheme.typography.bodyMedium)
            RadioButtonGroup(
                options = listOf("Low", "Medium", "High"),
                selectedOption = activity.value,
                onOptionSelected = { activity.value = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Goal", style = MaterialTheme.typography.bodyMedium)
            RadioButtonGroup(
                options = listOf("Lose Weight", "Maintain Weight", "Gain Weight"),
                selectedOption = goal.value,
                onOptionSelected = { goal.value = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Handle sign-up logic */ },
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp) // Add bottom padding
            ) {
                Text("Sign Up")
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
                    append("Already subscribed? ")
                }
                pushStringAnnotation(tag = "LOGIN", annotation = "Log in")
                withStyle(style = SpanStyle(color = Color.Blue)) {
                    append("Log in")
                }
                pop()
            }
            ClickableText(
                text = annotatedText,
                onClick = { offset ->
                    annotatedText.getStringAnnotations(tag = "LOGIN", start = offset, end = offset)
                        .firstOrNull()?.let {
                            navController.navigate(NavigationRoute.Login.route)
                        }
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun RadioButtonGroup(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column {
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = option == selectedOption,
                    onClick = { onOptionSelected(option) }
                )
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}