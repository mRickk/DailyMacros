package com.example.dailymacros.ui.screens.diet

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.dailymacros.ui.composables.DMTopAppBar
import com.example.dailymacros.ui.composables.NavBar

@Composable
fun Diet(navController: NavHostController) {
    Scaffold (
        topBar = { DMTopAppBar(navController) },
        bottomBar = { NavBar(navController, selectedIndex = 1) }
    ){paddingValues ->
        Text("Diet", modifier = Modifier.padding(paddingValues))
    }
}