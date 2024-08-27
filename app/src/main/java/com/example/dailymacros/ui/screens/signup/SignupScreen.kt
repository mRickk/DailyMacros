package com.example.dailymacros.ui.screens.signup

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.platform.LocalContext
import com.example.dailymacros.data.database.ActivityType
import com.example.dailymacros.data.database.DietType
import com.example.dailymacros.data.database.Gender
import com.example.dailymacros.data.database.GoalType
import com.example.dailymacros.data.database.User
import com.example.dailymacros.utilities.calculateBMR
import com.example.dailymacros.utilities.calculateDailyKcal

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

    val gender = remember { mutableStateOf(Gender.MALE) }

    val age = remember { mutableStateOf("") }
    val ageError = remember { mutableStateOf(false) }

    val weight = remember { mutableStateOf("") }
    val weightError = remember { mutableStateOf(false) }

    val height = remember { mutableStateOf("") }
    val heightError = remember { mutableStateOf(false) }

    val activity = remember { mutableStateOf(ActivityType.LIGHTLY_ACTIVE) }

    val goal = remember { mutableStateOf(GoalType.MAINTAIN_WEIGHT) }

    var expandedActivity by remember { mutableStateOf(false) }
    var expandedGoal by remember { mutableStateOf(false) }
    var expandedGender by remember { mutableStateOf(false) }

    fun validateEmail(email: String): Boolean {
        val regex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$")
        return regex.matches(email)
    }

    fun validatePassword(pswd: String): Boolean {
        return pswd.length >= 8
    }

    fun isPasswordMatching(pswd: String, confirmPswd: String): Boolean {
        return pswd == confirmPswd
    }

    fun validateNumber(number: String): Boolean {
        return number.isNotEmpty() && number.toFloatOrNull() == null
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
                label = { Text("Password (min. 8 characters)") },
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
                    text = "Password must be at least 8 characters long",
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                OutlinedTextField(
                    value = gender.value.string,
                    onValueChange = {},
                    label = { Text("Gender") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { expandedGender = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
                DropdownMenu(
                    expanded = expandedGender,
                    onDismissRequest = { expandedGender = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                ) {
                    Gender.entries.forEach { g ->
                        DropdownMenuItem(
                            onClick = {
                                gender.value = g
                                expandedGender = false
                            },
                            text = { Text(text = g.string) }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = age.value,
                onValueChange = { age.value = it },
                label = { Text("Age (years)") },
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
                label = { Text("Weight (kg)") },
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
                label = { Text("Height (cm)") },
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                OutlinedTextField(
                    value = activity.value.string,
                    onValueChange = {},
                    label = { Text("Activity Level") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { expandedActivity = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
                DropdownMenu(
                    expanded = expandedActivity,
                    onDismissRequest = { expandedActivity = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                ) {
                    ActivityType.entries.forEach { a ->
                        DropdownMenuItem(
                            onClick = {
                                activity.value = a
                                expandedActivity = false
                            },
                            text = { Text(text = a.string) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                OutlinedTextField(
                    value = goal.value.string,
                    onValueChange = {},
                    label = { Text("Goal") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { expandedGoal = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
                DropdownMenu(
                    expanded = expandedGoal,
                    onDismissRequest = { expandedGoal = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                ) {
                    GoalType.entries.forEach { g ->
                        DropdownMenuItem(
                            onClick = {
                                goal.value = g
                                expandedGoal = false
                            },
                            text = { Text(text = g.string) }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    emailError.value = false
                    usernameError.value = false
                    passwordError.value = false
                    confirmPasswordError.value = false
                    ageError.value = false
                    weightError.value = false
                    heightError.value = false
                    //Signup logic
                    if (email.value.isEmpty() || !validateEmail(email.value)) {
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
                        val bmr = calculateBMR(height.value.toFloat(), weight.value.toFloat(), age.value.toInt(), gender.value.k)
                        val newUser = User(
                            email = email.value,
                            password = password.value,
                            username = username.value,
                            pictureUrl = null,
                            height = height.value.toFloat(),
                            weight = weight.value.toFloat(),
                            gender = gender.value,
                            age = age.value.toInt(),
                            activity = activity.value,
                            goal = goal.value,
                            bmr = bmr,
                            dailyKcal = calculateDailyKcal(bmr, activity.value.k, goal.value.k),
                            diet = DietType.STANDARD
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
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f)),
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