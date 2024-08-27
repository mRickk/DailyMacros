package com.example.dailymacros.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import com.example.dailymacros.ui.NavigationRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DMTopAppBar(
    navController: NavHostController,
    showBackArrow : Boolean = false,
    showProfile: Boolean = true,
    isSettings: Boolean = false
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            titleContentColor = MaterialTheme.colorScheme.onTertiary,
        ),
        title = {
            Text(
                "DailyMacros",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            if (showBackArrow) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back button",
                        tint = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
        },
        actions = {
            if (showProfile) {
                if (!isSettings) {
                    IconButton(onClick = { navController.navigate(NavigationRoute.Profile.route) }) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Profile button",
                            tint = MaterialTheme.colorScheme.onTertiary
                        )
                    }
                }
            } else {
                IconButton(onClick = { navController.navigate(NavigationRoute.Settings.route) }) {
                    Icon(
                        imageVector = Icons.Filled.BrightnessMedium,
                        contentDescription = "Settings button",
                        tint = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
        }
    )
}