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
import androidx.compose.ui.platform.LocalContext
import androidx.room.PrimaryKey
import com.example.dailymacros.data.database.ActivityType
import com.example.dailymacros.data.database.Gender
import com.example.dailymacros.data.database.GoalType
import com.example.dailymacros.data.database.User

@Composable
fun Signup(navController: NavHostController,
           signupViewModel: SignupViewModel,
           userState: UserState,
           actions: SignupActions) {

    val context = LocalContext.current

    val email = remember { mutableStateOf("") }
    val emailError = remember { mutableStateOf(false) }

    val username = remember { mutableStateOf("") }
    val usernameError = remember { mutableStateOf(false) }

    val password = remember { mutableStateOf("") }
    val passwordError = remember { mutableStateOf(false) }
    val passwordVisibility = remember { mutableStateOf(false) }

    val confirmPassword = remember { mutableStateOf("") }
    val confirmPasswordError = remember { mutableStateOf(false) }

    val gender = remember { mutableStateOf("Male") }

    val age = remember { mutableStateOf("") }
    val ageError = remember { mutableStateOf(false) }

    val weight = remember { mutableStateOf("") }
    val weightError = remember { mutableStateOf(false) }

    val height = remember { mutableStateOf("") }
    val heightError = remember { mutableStateOf(false) }

    val activity = remember { mutableStateOf("Low") }

    val goal = remember { mutableStateOf("Lose Weight") }

    fun validatePassword(pswd: String): Boolean {
        return pswd.length >= 8
    }

    fun isPasswordMatching(pswd: String, confirmPswd: String): Boolean {
        return pswd == confirmPswd
    }

    fun validateNumber(number: String): Boolean {
        return number.isNotEmpty() && number.any { !it.isDigit() }
    }

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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textStyle = TextStyle(color = Color.Black)
            )
            if (emailError.value) {
                Text(
                    text = "Invalid email",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            }
            TextField(
                value = username.value,
                onValueChange = { username.value = it },
                label = { Text("Username") },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textStyle = TextStyle(color = Color.Black)
            )
            if (usernameError.value) {
                Text(
                    text = "Invalid username",
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
                    .padding(bottom = 16.dp),
                textStyle = TextStyle(color = Color.Black)
            )
            if (passwordError.value) {
                Text(
                    text = "Invalid password",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            }
            TextField(
                value = confirmPassword.value,
                onValueChange = { confirmPassword.value = it },
                label = { Text("Confirm Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textStyle = TextStyle(color = Color.Black)
            )
            if (confirmPasswordError.value) {
                Text(
                    text = "Passwords do not match",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            }
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textStyle = TextStyle(color = Color.Black)
            )
            if (ageError.value) {
                Text(
                    text = "Invalid age",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            }
            TextField(
                value = weight.value,
                onValueChange = { weight.value = it },
                label = { Text("Weight") },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textStyle = TextStyle(color = Color.Black)
            )
            if (weightError.value) {
                Text(
                    text = "Invalid weight",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            }
            TextField(
                value = height.value,
                onValueChange = { height.value = it },
                label = { Text("Height") },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textStyle = TextStyle(color = Color.Black)
            )
            if (heightError.value) {
                Text(
                    text = "Invalid height",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            }
            Text("Activity Level", style = MaterialTheme.typography.bodyMedium)
            RadioButtonGroup(
                options = listOf("Sedentary", "Lightly Active", "Moderately Active", "Very Active", "Super Active"),
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
                onClick = {
                          //Signup logic
                            if (email.value.isEmpty()) {
                                emailError.value = true
                            } else if (username.value.isEmpty()) {
                                usernameError.value = true
                            } else if (password.value.isEmpty() || !validatePassword(password.value)) {
                                passwordError.value = true
                            } else if (confirmPassword.value.isEmpty() || !isPasswordMatching(password.value, confirmPassword.value)) {
                                confirmPasswordError.value = true
                            } else if (age.value.isEmpty() || validateNumber(age.value)) {
                                ageError.value = true
                            } else if (weight.value.isEmpty() || validateNumber(weight.value)) {
                                weightError.value = true
                            } else if (height.value.isEmpty() || validateNumber(height.value)) {
                                heightError.value = true
                            } else {
                                val newUser = User(
                                    email = email.value,
                                    password = password.value,
                                    username = username.value,
                                    pictureUrl = null,
                                    height = height.value.toFloat(),
                                    weight = weight.value.toFloat(),
                                    gender = Gender.valueOf(gender.value.uppercase()),
                                    age = age.value.toInt(),
                                    activity = ActivityType.valueOf(activity.value.uppercase().replace(" ", "_")),
                                    goal = GoalType.valueOf(goal.value.uppercase().replace(" ", "_")),
                                    bmr = 0, //TODO: Calculate BMR
                                    dailyKcal = 0 //TODO: Calculate daily kcal
                                )
                                actions.signupUser(newUser) {
                                    if(signupViewModel.loggedUser.value != null) {
                                        signupViewModel.actions.setUser(signupViewModel.loggedUser.value!!)
                                        navController.navigate(NavigationRoute.Diary.route)
                                    }

                                }

                            }
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp) // Add bottom padding
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