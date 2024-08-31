package com.example.dailymacros.ui.screens.selectexercise

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    selectedExerciseNameNull: String?,
    selectedDuration: Int?
) {
    val context = LocalContext.current
    var selectedExercise by remember { mutableStateOf<Exercise?>(null) }
    var toDeleteExercise by remember { mutableStateOf<Exercise?>(null) }
    var durationMinutes by remember { mutableStateOf(if (selectedDuration != null) (selectedDuration / 60).toString() else "") }
    var durationSeconds by remember { mutableStateOf(if (selectedDuration != null) (selectedDuration % 60).toString() else "") }
    var duration by remember { mutableIntStateOf(selectedDuration ?: 0) }
    var showDialog by remember { mutableStateOf(false) }
    var hasFavouriteChanged by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("Favourites") }
    var expanded by remember { mutableStateOf(false) }

    val isDurationValid by remember {
        derivedStateOf {
            (durationMinutes.toIntOrNull() ?: 0) * 60 + (durationSeconds.toIntOrNull() ?: 0) > 0
        }
    }
    selectedExercise = state.exerciseList.firstOrNull { it.name == selectedExerciseNameNull }

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

        val filteredExerciseList = when (selectedFilter) {
            "Favourites" -> state.exerciseList.sortedByDescending { it.isFavourite }
            "High-calorie burned" -> state.exerciseList.sortedByDescending { it.kcalBurnedSec }
            "Low-calorie burned" -> state.exerciseList.sortedBy { it.kcalBurnedSec }
            "Name" -> state.exerciseList.sortedBy { it.name }
            else -> state.exerciseList
        }

        Column(modifier = Modifier.padding(paddingValues)) {
            hasFavouriteChanged // Force recomposition when favourite status changes
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                OutlinedTextField(
                    value = selectedFilter,
                    onValueChange = {},
                    label = { Text("Order by") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                ) {
                    listOf(
                        "Favourites",
                        "High-calorie burned",
                        "Low-calorie burned",
                        "Name"
                    ).forEach { filter ->
                        DropdownMenuItem(
                            onClick = {
                                selectedFilter = filter
                                expanded = false
                            },
                            text = { Text(text = filter) }
                        )
                    }
                }
            }

            LazyColumn {
                items(filteredExerciseList) { exercise ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (exercise.isFavourite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                            contentDescription = if (exercise.isFavourite) "Favourite" else "Not Favourite",
                            tint = if (exercise.isFavourite) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.clickable {
                                exercise.isFavourite = !exercise.isFavourite
                                hasFavouriteChanged = !hasFavouriteChanged
                                viewModel.actions.toggleFavourite(exercise)
                            }
                        )
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
                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            IconButton(onClick = {
                                navController.navigate(NavigationRoute.AddExercise.route + "?exerciseName=${exercise.name}")
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Modify Exercise"
                                )
                            }
                            IconButton(onClick = {
                                toDeleteExercise = selectedExercise
                                showDialog = true
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Exercise"
                                )
                            }
                        }
                    }

                    Text(text = "Name: ${exercise.name}", fontSize = 20.sp)
                    Text(text = "Description: ${exercise.description ?: "-"}", fontSize = 16.sp)
                    Text(
                        text = "Calories: ${(exercise.kcalBurnedSec * duration).roundToInt()} kcal",
                        fontSize = 16.sp
                    )

                    OutlinedTextField(
                        value = durationMinutes,
                        onValueChange = {
                            duration =
                                (it.toIntOrNull() ?: 0) * 60 + (durationSeconds.toIntOrNull() ?: 0)
                            durationMinutes = it
                        },
                        label = { Text("Minutes") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )

                    OutlinedTextField(
                        value = durationSeconds,
                        onValueChange = {
                            duration =
                                (durationMinutes.toIntOrNull() ?: 0) * 60 + (it.toIntOrNull() ?: 0)
                            durationSeconds = it
                        },
                        label = { Text("Seconds") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )

                    Button(
                        onClick = {
                            if (viewModel.loggedUser.user != null && !viewModel.loggedUser.user!!.b2) {
                                viewModel.loggedUser.user!!.b2 = true
                                viewModel.actions.updateUser(viewModel.loggedUser.user!!)
                                Toast.makeText(context, "Badge n.2 unlocked! You inserted your first exercise inside the diary!", Toast.LENGTH_LONG).show() // Display toast message
                            }
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
                        Text(if (selectedExercise!!.name == selectedExerciseNameNull) "Update inserted duration" else "Insert exercise")
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Delete ${toDeleteExercise?.name}") },
            text = {
                Text(
                    "Are you sure to permanently delete this exercise?",
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )
                Text(
                    "\nWARNING: every record of this exercise inside the diary will be removed!",
                    fontWeight = FontWeight.Bold
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.actions.deleteExercise(toDeleteExercise!!)
                        showDialog = false
                        selectedExercise = null
                        toDeleteExercise = null
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
