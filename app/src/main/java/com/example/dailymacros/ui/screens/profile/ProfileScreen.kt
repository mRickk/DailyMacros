package com.example.dailymacros.ui.screens.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.ui.draw.clip

@Composable
fun Profile(navController: NavHostController, state: UserState, actions: ProfileActions) {
    var showDialog by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        // Handle the image from the gallery
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        // Handle the image from the camera
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Profile Picture
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .clickable {
                        showDialog = true
                    }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // User Information
            state.user?.let { user ->
                UserInfoRow(title = "Email", value = user.email)
                Divider()
                UserInfoRow(title = "Username", value = user.username)
                Divider()
                UserInfoRow(title = "Height", value = user.height.toString(), unit = "cm")
                Divider()
                UserInfoRow(title = "Weight", value = user.weight.toString(), unit = "kg")
                Divider()
                UserInfoRow(title = "Gender", value = user.gender.name)
                Divider()
                UserInfoRow(title = "Age", value = user.age.toString())
                Divider()
                UserInfoRow(title = "Activity Level", value = user.activity.name)
                Divider()
                UserInfoRow(title = "Goal", value = user.goal.name)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Edit Profile Button
            Button(onClick = {
                // Handle edit profile
            }) {
                Text("Edit Profile")
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Change Profile Picture") },
                text = { Text("Choose an option") },
                confirmButton = {
                    Column {
                        Button(onClick = {
                            showDialog = false
                            galleryLauncher.launch("image/*")
                        }) {
                            Text("Select from Gallery")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            showDialog = false
                            cameraLauncher.launch(null)
                        }) {
                            Text("Take a Photo")
                        }
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun UserInfoRow(title: String, value: String, unit: String? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
            unit?.let {
                Spacer(modifier = Modifier.width(4.dp)) // Add space between value and unit
                Text(text = it, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}