package com.example.dailymacros.ui.screens.diary

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.dailymacros.ui.composables.DMTopAppBar
import com.example.dailymacros.ui.composables.datePickerWithDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Diary(navController: NavHostController) {
    Scaffold (
        topBar = { DMTopAppBar(navController) }
    ){paddingValues ->
        val dateState = datePickerWithDialog(Modifier.padding(paddingValues))
    }
}