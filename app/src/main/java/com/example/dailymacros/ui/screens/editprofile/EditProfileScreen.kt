package com.example.dailymacros.ui.screens.editprofile

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.ContentAlpha
import com.example.dailymacros.data.database.Gender
import com.example.dailymacros.ui.NavigationRoute

@Composable
fun EditProfile(navController: NavController, editProfileViewModel: EditProfileViewModel) {

    editProfileViewModel.actions.getUser()
    var username by remember {
        mutableStateOf(
            ""
        )
    }
    var weight by remember {
        mutableStateOf(
            ""
        )
    }
    var height by remember {
        mutableStateOf(
            ""
        )
    }
    var age by remember {
        mutableStateOf(
            ""
        )
    }

    var gender by remember { mutableStateOf(Gender.OTHER) }

    val user = editProfileViewModel.loggedUser.user

    LaunchedEffect(user) {
        if (user != null) {
            username = user.username
            weight = user.weight.toString()
            height = user.height.toString()
            age = user.age.toString()
            gender = user.gender
        }
    }
    Log.v("EditProfileScreen", "EditProfile: ${weight}, ${height}, ${age}, $gender")

    val usernameError = remember { mutableStateOf(false) }
    val weightError = remember { mutableStateOf(false) }
    val heightError = remember { mutableStateOf(false) }
    val ageError = remember { mutableStateOf(false) }

    fun checkUsername() : Boolean {
        return username.isEmpty()
    }

    fun checkWeight() : Boolean {
        return weight.isEmpty()
    }

    fun checkHeight() : Boolean {
        return height.isEmpty()
    }

    fun checkAge() : Boolean {
        return age.isEmpty()
    }

    //TODO ADD ERROR MESSAGES

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Height (cm)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(

            value = age,
            onValueChange = { age = it },
            label = { Text("Age") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        var expanded by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled)
                )
        ) {

            Text(
                text = gender.string,
                modifier = Modifier.padding(16.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth(),

            ) {
                Gender.entries.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.string) },
                        onClick = {
                            gender = option
                            expanded = false
                        }
                    )
                }
            }

        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                // Save action
                Log.v(
                    "EditProfileScreen",
                    "Save: ${username} ${weight}, ${height.toFloat()}, ${age}, $gender"
                )
                if(checkUsername()){
                    usernameError.value = true
                }
                else if(checkWeight()){
                    weightError.value = true
                }
                else if(checkHeight()){
                    heightError.value = true
                }
                else if(checkAge()){
                    ageError.value = true
                }
                else {
                    editProfileViewModel.actions.updateProfile(
                        username,
                        weight.toFloat(),
                        height.toFloat(),
                        age.toInt(),
                        gender
                    )

                    navController.popBackStack()
                }
            }) {
                Text("Save")
            }
            Button(onClick = {
                // Cancel action
                navController.popBackStack()
            }) {
                Text("Cancel")
            }
        }

    }
}