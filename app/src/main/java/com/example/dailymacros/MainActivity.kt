package com.example.dailymacros

import android.Manifest
import android.provider.Settings
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.dailymacros.ui.theme.DailyMacrosTheme
import com.example.dailymacros.ui.NavGraph
import com.example.dailymacros.ui.composables.NavBar

import com.example.dailymacros.ui.screens.settings.SettingsViewModel
import com.example.dailymacros.ui.screens.settings.Theme
import com.example.dailymacros.utilities.LocationService
import com.example.dailymacros.utilities.PermissionStatus
import com.example.dailymacros.utilities.StartMonitoringResult
import com.example.dailymacros.utilities.rememberPermission
import org.koin.androidx.compose.koinViewModel


class MainActivity : FragmentActivity() {
    lateinit var locationService: LocationService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationService = LocationService(this)

        setContent {
            val settingsViewModel = koinViewModel<SettingsViewModel>()
            val themeState by settingsViewModel.state.collectAsStateWithLifecycle()
            DailyMacrosTheme(
                darkTheme = when (themeState.theme) {
                    Theme.Light -> false
                    Theme.Dark -> true
                    Theme.System -> isSystemInDarkTheme()
                }
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val ctx = LocalContext.current
                    val snackbarHostState = remember { SnackbarHostState() }

                    val navController = rememberNavController()

                    /* GPS */
                    var showLocationDisabledAlert by remember { mutableStateOf(false) }
                    var showPermissionDeniedAlert by remember { mutableStateOf(false) }
                    var showPermissionPermanentlyDeniedSnackbar by remember { mutableStateOf(false) }

                    fun onLocationGranted(){
                        val res = locationService.requestCurrentLocation()
                        showLocationDisabledAlert = res == StartMonitoringResult.GPSDisabled
                    }

                    val locationPermission = rememberPermission(
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) { status ->
                        when (status) {
                            PermissionStatus.Granted ->
                                showLocationDisabledAlert =
                                    locationService.requestCurrentLocation() == StartMonitoringResult.GPSDisabled

                            PermissionStatus.Denied -> showPermissionDeniedAlert = true
                            PermissionStatus.PermanentlyDenied -> showPermissionPermanentlyDeniedSnackbar =
                                true

                            PermissionStatus.Unknown -> {}
                        }
                    }


                    fun requestLocation() {
                        if (locationPermission.status.isGranted) {
                            onLocationGranted()
                        } else {
                            locationPermission.launchPermissionRequest()
                        }
                    }

                    LaunchedEffect(Unit) {
                        requestLocation()
                    }

                    Scaffold {contentPadding ->
                        NavGraph(navController = navController,
                            Modifier.padding(contentPadding),
                            settingsViewModel,
                            locationService)
                        if (showLocationDisabledAlert) {
                            AlertDialog(
                                title = { Text("Location disabled") },
                                text = { Text("Location must be enabled to get your current location in the app.") },
                                confirmButton = {
                                    Button(onClick = {
                                        locationService.openLocationSettings()
                                        showLocationDisabledAlert = false
                                    }) {
                                        Text("Enable")
                                    }
                                },
                                dismissButton = {
                                    OutlinedButton(
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onTertiary),
                                        onClick = { showLocationDisabledAlert = false }
                                    ) {
                                        Text("Dismiss", color=MaterialTheme.colorScheme.onTertiary)
                                    }
                                },
                                onDismissRequest = { showLocationDisabledAlert = false }
                            )
                        }

                        if (showPermissionDeniedAlert) {
                            AlertDialog(
                                title = { Text("Location permission denied") },
                                text = { Text("Location permission is required to get your current location in the app.") },
                                confirmButton = {
                                    Button(onClick = {
                                        locationPermission.launchPermissionRequest()
                                        showPermissionDeniedAlert = false
                                    }) {
                                        Text("Grant")
                                    }
                                },
                                dismissButton = {
                                    OutlinedButton(
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onTertiary),
                                        onClick = { showPermissionDeniedAlert = false }
                                    ) {
                                        Text("Dismiss", color=MaterialTheme.colorScheme.onTertiary)
                                    }
                                },
                                onDismissRequest = { showPermissionDeniedAlert = false }
                            )
                        }
                        if (showPermissionPermanentlyDeniedSnackbar) {
                            LaunchedEffect(snackbarHostState) {
                                val res = snackbarHostState.showSnackbar(
                                    "Location permission is required.",
                                    "Go to Settings",
                                    duration = SnackbarDuration.Long
                                )
                                if (res == SnackbarResult.ActionPerformed) {
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                        data = Uri.fromParts("package", ctx.packageName, null)
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    }
                                    if (intent.resolveActivity(ctx.packageManager) != null) {
                                        ctx.startActivity(intent)
                                    }
                                }
                                showPermissionPermanentlyDeniedSnackbar = false
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        locationService.pauseLocationRequest()
    }

    override fun onResume() {
        super.onResume()
        locationService.resumeLocationRequest()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DailyMacrosTheme {
        Greeting("Android")
    }
}