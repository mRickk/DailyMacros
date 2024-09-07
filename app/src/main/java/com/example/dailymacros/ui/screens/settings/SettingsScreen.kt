package com.example.dailymacros.ui.screens.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.ContentAlpha
import com.example.dailymacros.ui.composables.DMTopAppBar

@Composable
fun Settings(navController: NavHostController, state: ThemeState, settingsViewModel: SettingsViewModel, onThemeSelected: (theme: Theme) -> Unit) {
    val radioOptions = listOf(Theme.Light, Theme.Dark, Theme.System)
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[1]) }
    val context = LocalContext.current

    Scaffold(
        topBar = { DMTopAppBar(navController, showBackArrow = true, isSettings = true) }
    ) { paddingValues ->
        Column(Modifier.selectableGroup().padding(paddingValues)) {
            // Theme selection section
            Text(
                text = "Change theme:",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )
            Theme.entries.forEach { theme ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = (theme == state.theme),
                            onClick = { onThemeSelected(theme) },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = (theme == state.theme), onClick = null)
                    Text(
                        text = theme.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled)
            )

            // Change password section
            Text(
                text = "Change password:",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )
            val oldPassword = remember { mutableStateOf("") }
            val newPassword = remember { mutableStateOf("") }
            val confirmPassword = remember { mutableStateOf("") }
            val passwordError = remember { mutableStateOf(false) }

            TextField(
                value = oldPassword.value,
                onValueChange = { oldPassword.value = it },
                label = { Text("Old Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            TextField(
                value = newPassword.value,
                onValueChange = { newPassword.value = it },
                label = { Text("New Password(at least 8 characters)") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            TextField(
                value = confirmPassword.value,
                onValueChange = { confirmPassword.value = it },
                label = { Text("Confirm New Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            if (passwordError.value) {
                Text(
                    text = "Passwords do not match",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                )
            }
            Button(
                onClick = {
                    if(newPassword.value.length < 8) {
                        Toast.makeText(context, "Password must be at least 8 characters long!", Toast.LENGTH_SHORT).show()
                    } else if (newPassword.value != confirmPassword.value) {
                        Toast.makeText(context, "New passwords do not match!", Toast.LENGTH_SHORT).show()
                    } else if (oldPassword.value != settingsViewModel.loggedUser.user!!.password) {
                        Toast.makeText(context, "Old password is incorrect!", Toast.LENGTH_SHORT).show()
                    } else if (newPassword.value.isEmpty() || confirmPassword.value.isEmpty()) {
                        Toast.makeText(context, "Please fill all fields!", Toast.LENGTH_SHORT).show()
                    } else {
                        settingsViewModel.actions.UpdatePassword(newPassword.value)
                        Toast.makeText(context, "Password changed successfully!", Toast.LENGTH_SHORT).show()
                        oldPassword.value = ""
                        newPassword.value = ""
                        confirmPassword.value = ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text("Save Password")
            }
        }
    }
}