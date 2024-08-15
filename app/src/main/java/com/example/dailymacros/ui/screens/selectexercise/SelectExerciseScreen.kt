package com.example.dailymacros.ui.screens.selectexercise

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dailymacros.ui.composables.DMTopAppBar
import com.example.dailymacros.ui.composables.ExerciseInfoBar
import com.example.dailymacros.ui.NavigationRoute
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SelectExerciseScreen(
    navController: NavHostController,
    viewModel: SelectExerciseViewModel,
    state: SelectExerciseState
) {
    Log.v("SelectExerciseScreen", "state.exerciseList: ${state.exerciseList}")

    Scaffold(
        topBar = { DMTopAppBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(NavigationRoute.AddExercise.route) },
                modifier = Modifier.padding(8.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Insert new Exercise",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(state.exerciseList) { sport ->
                ExerciseInfoBar(
                    exercise = sport.name,
                    caloriesBurned = sport.kcalBurnedSec * 3600,
                    duration = 3600
                )
            }
        }
    }
}