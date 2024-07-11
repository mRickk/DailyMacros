package com.example.dailymacros.ui.screens.diary

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.dailymacros.ui.composables.DMTopAppBar

@Composable
fun Diary(navController: NavHostController) {
    Scaffold (
        topBar = { DMTopAppBar(navController) }
    ){paddingValues ->
        Text("Diary", modifier = Modifier.padding(paddingValues))
    }
}

