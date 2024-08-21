package com.example.dailymacros.ui.screens.overview

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.dailymacros.ui.composables.DMTopAppBar
import com.example.dailymacros.ui.composables.NavBar

@Composable
fun Overview(navController: NavHostController) {
    Scaffold (
        topBar = { DMTopAppBar(navController) },
        bottomBar = { NavBar(navController, selectedIndex = 2) }
    ){paddingValues ->
        Text("Overview", modifier = Modifier.padding(paddingValues))
    }
}