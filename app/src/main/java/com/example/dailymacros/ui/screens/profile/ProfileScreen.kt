package com.example.dailymacros.ui.screens.profile

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.core.view.get
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dailymacros.MainActivity
import com.example.dailymacros.ui.NavigationRoute
import com.example.dailymacros.ui.composables.DMTopAppBar
import com.example.dailymacros.utilities.LocationService
import com.example.dailymacros.utilities.rememberCamera
import com.example.dailymacros.utilities.rememberPermission
import com.example.dailymacros.utilities.saveImageToStorage
import kotlin.math.log
import com.utsman.osmandcompose.rememberMapViewWithLifecycle
import org.osmdroid.util.GeoPoint

@SuppressLint("ClickableViewAccessibility")
@Composable
fun Profile(navController: NavHostController, profileViewModel: ProfileViewModel, locationService: LocationService) {
    val compositionCount = remember { mutableStateOf(0) }
    compositionCount.value++
    LaunchedEffect(compositionCount.value) {
        if (profileViewModel.loggedUser.user == null && compositionCount.value == 2) {
            navController.navigate(NavigationRoute.Login.route)
        }
    }

    Log.v("ProfileScreen", "ProfileImage: ${profileViewModel.loggedUser.user}")
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val mapView = rememberMapViewWithLifecycle()

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
    val cameraPermission = rememberPermission(android.Manifest.permission.CAMERA) { status ->
        if (status.isGranted) {
            cameraLauncher.takePicture()
        }
        else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    fun launchCamera() =
        if(cameraPermission.status.isGranted) {
            cameraLauncher.takePicture()
        } else {
            Log.v("ProfileScreen", "Requesting camera permission ${cameraPermission.status}")
            cameraPermission.launchPermissionRequest()
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
                .border(1.dp, MaterialTheme.colorScheme.onBackground, CircleShape)
        ) {
            if (profileImage != null && profileImage.path != null) {
                AsyncImage(
                    ImageRequest.Builder(context)
                        .data(profileImage)
                        .crossfade(true)
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

    Scaffold (
        topBar = { DMTopAppBar(navController = navController, showBackArrow = true, showProfile = false) }
    ){ paddingValues ->


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(5.dp))

                ProfileImage(profileImage = profileViewModel.loggedUser.user?.pictureUrl?.toUri())

                Spacer(modifier = Modifier.height(5.dp))

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
                    Divider()
                }
                if (locationService.coordinates != null) {
                    Spacer(modifier = Modifier.height(50.dp))
                    Box(
                        Modifier
                            .height(75.dp)
                            .clickable(onClick = {}, enabled = false)
                    ) {
                        AndroidView(
                            factory = { mapView },
                            update = { view ->
                                // Update your MapView here
                                view.setMultiTouchControls(false)
                                view.isClickable = false
                                view.setOnTouchListener { _, _ -> true } // Disable all touch events
                                view.controller.setZoom(15.0)
                                view.controller.setCenter(
                                    GeoPoint(
                                        locationService.coordinates?.latitude
                                            ?: 44.14807981060653,
                                        locationService.coordinates?.longitude
                                            ?: 12.235592781952542
                                    )
                                )

                                if (locationService.coordinates == null) {
                                    // Apply grayscale filter
                                    view.overlayManager.tilesOverlay.setColorFilter(android.graphics.ColorMatrixColorFilter(
                                        android.graphics.ColorMatrix()
                                            .apply { setSaturation(0f) }
                                    ))
                                } else {
                                    // Remove grayscale filter
                                    view.overlayManager.tilesOverlay.setColorFilter(null)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent)
                                .pointerInput(Unit) {} // Disable click events
                        )
                    }
                    Spacer(modifier = Modifier.height(65.dp))
                }
                Spacer(modifier = Modifier.height(10.dp))
                // Edit and logout Profile Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(onClick = {
                        profileViewModel.actions.logout(navController)
                    }, border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)) {
                        Text("Logout", color = MaterialTheme.colorScheme.error)
                    }
                    Button(onClick = { navController.navigate(NavigationRoute.EditProfile.route) }) {
                        Text("Edit Profile")
                    }
                }


            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Change Profile Picture", textAlign = TextAlign.Center) },
                text = { Text("Choose an option", textAlign = TextAlign.Center) },
                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                showDialog = false
                                galleryLauncher.launch("image/*")
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp)
                        ) {
                            Text("Select from Gallery", textAlign = TextAlign.Center)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                showDialog = false
                                launchCamera()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp)
                        ) {
                            Text("Take a Photo", textAlign = TextAlign.Center)
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