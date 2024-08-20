package com.example.dailymacros.utilities

import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

enum class PermissionStatus {
    NOT_REQUESTED,
    GRANTED,
    DENIED,
    DENIED_FOREVER;

    val isGranted: Boolean
        get() = this == GRANTED
    val isDenied: Boolean
        get() = this == DENIED || this == DENIED_FOREVER
}

interface Permissions {
    val permissionStatus: PermissionStatus
    fun requestPermission()
}

@Composable
fun rememberPermission(permission: String,
                       onResult: (status: PermissionStatus) -> Unit = {}
): Permissions {

    var status by remember { mutableStateOf(PermissionStatus.NOT_REQUESTED) }
    val activity = (LocalContext.current as ComponentActivity)

    var permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        status = if (isGranted) {
            PermissionStatus.GRANTED
        } else {
            if (activity.shouldShowRequestPermissionRationale(permission)) {
                PermissionStatus.DENIED
            } else {
                PermissionStatus.DENIED_FOREVER
            }
        }
        onResult(status)
    }

    val permissionHandler by remember {
        derivedStateOf {
            object : Permissions {
                override val permissionStatus = status
                override fun requestPermission() = permissionLauncher.launch(permission)
            }
        }
    }
    return permissionHandler


}
