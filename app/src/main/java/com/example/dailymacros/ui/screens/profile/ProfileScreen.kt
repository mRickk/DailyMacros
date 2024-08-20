package com.example.dailymacros.ui.screens.profile

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dailymacros.utilities.rememberCamera
import com.example.dailymacros.utilities.rememberPermission
import com.example.dailymacros.utilities.saveImageToStorage
import kotlin.math.log

@Composable
fun Profile(navController: NavHostController, profileViewModel: ProfileViewModel) {
    Log.v("ProfileScreen", "ProfileImage: ${profileViewModel.loggedUser.user}")
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    //Camera launcher
    val cameraLauncher = rememberCamera {
        Log.v("ProfileScreen", "CameraLauncher: $it")
        profileViewModel.actions.setProfilePicUrl(profileViewModel.loggedUser.user?.email ?: "", it.toString())
    }

    //Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                saveImageToStorage(it, context.applicationContext.contentResolver)
                profileViewModel.actions.setProfilePicUrl(profileViewModel.loggedUser.user?.email ?: "", it.toString())
            }
        }
    )

    //Camera permissions
    val cameraPermission = rememberPermission(android.Manifest.permission.CAMERA) {
            status ->
        if (status.isGranted) {
            cameraLauncher.takePicture()
        }
        else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    @Composable
    fun ProfileImage(profileImage: Uri?) {
        Box(
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape)
                .clickable {
                    showDialog = true
                }
                .border(2.dp, Color.Black, CircleShape)
        ) {
            if (profileImage != null && profileImage.path != null) {
                AsyncImage(
                    ImageRequest.Builder(context)
                        .data(profileImage)
                        .build(),
                    "Profile Image",
                    modifier = Modifier
                        .size(128.dp)
                        .clip(CircleShape)
                )
            } else {
                //Default profile pic
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(128.dp)
                        .clip(CircleShape)
                )
            }
        }
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
            ProfileImage(profileImage = profileViewModel.loggedUser.user?.pictureUrl?.toUri())

            Spacer(modifier = Modifier.height(16.dp))

            // User Information
            profileViewModel.loggedUser.user?.let { user ->
                UserInfoRow(title = "Email", value = user.email)
                Divider()
                UserInfoRow(title = "Username", value = user.username)
                Divider()
                UserInfoRow(title = "Height", value = user.height.toString(), unit = "cm")
                Divider()
                UserInfoRow(title = "Weight", value = user.weight.toString(), unit = "kg")
                Divider()
                UserInfoRow(title = "Gender", value = user.gender.string)
                Divider()
                UserInfoRow(title = "Age", value = user.age.toString())
                Divider()
                UserInfoRow(title = "Activity Level", value = user.activity.string)
                Divider()
                UserInfoRow(title = "Goal", value = user.goal.string)
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
                            cameraLauncher.takePicture()
                        }) {
                            Text("Take a Photo")
                        }
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