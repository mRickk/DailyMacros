package com.example.dailymacros.ui.screens.editprofile

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dailymacros.data.database.Gender

@Composable
fun EditProfile(navController: NavController, editProfileViewModel: EditProfileViewModel) {
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
    var age = remember {
        mutableStateOf(
            ""
        )
    }
    var gender by remember { mutableStateOf(Gender.OTHER) }

    Log.v("EditProfileScreen", "EditProfile: ${weight}, ${height}, ${age}, $gender")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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

            value = age.value,
            onValueChange = { age.value = it },
            label = { Text("Age") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                Text(gender.string)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                Gender.values().forEach { option ->
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
                    "Save: ${weight}, ${height.toFloat()}, ${age}, $gender"
                )
                editProfileViewModel.actions.updateProfile(
                    weight.toFloat(),
                    height.toFloat(),
                    age.value.toInt(),
                    gender
                )

                navController.popBackStack()
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