package com.example.dailymacros.ui.screens.selectexercise

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.dailymacros.ui.composables.DMTopAppBar
import com.example.dailymacros.ui.composables.ExerciseInfoBar
import com.example.dailymacros.ui.NavigationRoute
import com.example.dailymacros.data.database.Exercise
import kotlin.math.roundToInt

@Composable
fun SelectExerciseScreen(
    navController: NavHostController,
    viewModel: SelectExerciseViewModel,
    state: SelectExerciseState,
    date: String?,
    exerciseName: String?,
    selectedDuration: Int?
) {
    var selectedExercise by remember { mutableStateOf<Exercise?>(null) }
    var durationMinutes by remember { mutableStateOf(if (selectedDuration != null) (selectedDuration / 60).toString() else "") }
    var durationSeconds by remember { mutableStateOf(if (selectedDuration != null) (selectedDuration % 60).toString() else "") }
    var duration by remember { mutableIntStateOf(selectedDuration ?: 0) }
    var showDialog by remember { mutableStateOf(false) }

    val isDurationValid by remember {
        derivedStateOf {
            (durationMinutes.toIntOrNull() ?: 0) * 60 + (durationSeconds.toIntOrNull() ?: 0) > 0
        }
    }
    selectedExercise = state.exerciseList.firstOrNull { it.name == exerciseName }
    Scaffold(
        topBar = { DMTopAppBar(navController, showBackArrow = true) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(NavigationRoute.AddExercise.route) },
                modifier = Modifier.padding(8.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add new Exercise",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            LazyColumn {
                items(state.exerciseList) { exercise ->
                    ExerciseInfoBar(
                        exercise = exercise.name,
                        caloriesBurned = exercise.kcalBurnedSec * 3600,
                        duration = 3600,
                        modifier = Modifier.clickable {
                            selectedExercise = exercise
                        }
                    )
                }
            }

            selectedExercise?.let { exercise ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable { /* Prevent clicks on underlying content */ }
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.background)
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { selectedExercise = null }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Close"
                                )
                            }
                            IconButton(onClick = {
                                navController.navigate(NavigationRoute.AddExercise.route + "?exerciseName=${exercise.name}")
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Modify Exercise"
                                )
                            }
                            IconButton(onClick = { showDialog = true }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Exercise"
                                )
                            }
                        }

                        Text(text = "Name: ${exercise.name}", fontSize = 20.sp)
                        Text(text = "Description: ${exercise.description ?: "-"}", fontSize = 16.sp)
                        Text(text = "Calories: ${(exercise.kcalBurnedSec * duration).roundToInt()} kcal", fontSize = 16.sp)

                        OutlinedTextField(
                            value = durationMinutes,
                            onValueChange = {
                                duration = (it.toIntOrNull() ?: 0) * 60 + (durationSeconds.toIntOrNull() ?: 0)
                                durationMinutes = it
                            },
                            label = { Text("Minutes") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        )

                        OutlinedTextField(
                            value = durationSeconds,
                            onValueChange = {
                                duration = (durationMinutes.toIntOrNull() ?: 0) * 60 + (it.toIntOrNull() ?: 0)
                                durationSeconds = it
                            },
                            label = { Text("Seconds") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        )

                        Button(
                            onClick = {
                                viewModel.actions.insertExerciseInsideDay(
                                    id = null,
                                    exercise = exercise,
                                    date = date!!,
                                    duration = duration
                                )
                                navController.navigate(NavigationRoute.Diary.route)
                            },
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            enabled = isDurationValid && date != null
                        ) {
                            Text("Insert exercise")
                        }
                    }
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Delete ${selectedExercise?.name}") },
                text = {
                    Text("Are you sure to permanently delete this exercise?", fontSize = MaterialTheme.typography.bodySmall.fontSize)
                    Text("\nWARNING: every record of this exercise inside the diary will be removed!", fontWeight = FontWeight.Bold)
               },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.actions.deleteExercise(selectedExercise!!)
                            showDialog = false
                            selectedExercise = null
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showDialog = false }) {
                        Text("Cancel", color = MaterialTheme.colorScheme.onBackground)
                    }
                }
            )
        }
    }
}