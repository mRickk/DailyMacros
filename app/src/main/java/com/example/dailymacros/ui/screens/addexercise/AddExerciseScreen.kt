package com.example.dailymacros.ui.screens.addexercise

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dailymacros.data.database.Exercise
import com.example.dailymacros.ui.composables.DMTopAppBar
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExerciseScreen(
    navController: NavHostController,
    addExerciseVM: AddExerciseViewModel,
    exerciseNullName: String?
) {
    if (exerciseNullName != null) {
        addExerciseVM.actions.getExercise(exerciseNullName)
    }
    val exercise = addExerciseVM.state.exercise
    val exerciseName = remember { mutableStateOf("") }
    val exerciseDescription = remember { mutableStateOf("") }
    val kcalBurnedPerHour = remember { mutableStateOf("") }
    val isFormValid = remember {
        derivedStateOf {
            exerciseName.value.isNotBlank() && kcalBurnedPerHour.value.isNotBlank() && kcalBurnedPerHour.value.toIntOrNull() != null
        }
    }

    if (exercise != null) {
        exerciseName.value = exercise.name
        exerciseDescription.value = exercise.description ?: ""
        kcalBurnedPerHour.value = exercise.kcalBurnedSec.times(3600).toString()
    }

    Scaffold(
        topBar = { DMTopAppBar(navController, showBackArrow = true) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            OutlinedTextField(
                value = exerciseName.value,
                onValueChange = { exerciseName.value = it },
                label = { Text("Exercise Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = exerciseDescription.value,
                onValueChange = { exerciseDescription.value = it },
                label = { Text("Description (Optional)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = if (kcalBurnedPerHour.value.isNotBlank()) kcalBurnedPerHour.value.toFloat().roundToInt().toString() else kcalBurnedPerHour.value,
                onValueChange = { kcalBurnedPerHour.value = it },
                label = { Text("Kcal/hour") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    if (isFormValid.value) {
                        addExerciseVM.actions.upsertExercise(
                            Exercise(
                                name = exerciseName.value,
                                description = exerciseDescription.value.takeIf { it.isNotBlank() },
                                kcalBurnedSec = kcalBurnedPerHour.value.toFloat() / 3600
                            )
                        )
                        navController.popBackStack()
                    }
                },
                enabled = isFormValid.value,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(if (exercise != null) "Modify exercise" else "Add new exercise")
            }
        }
    }
}