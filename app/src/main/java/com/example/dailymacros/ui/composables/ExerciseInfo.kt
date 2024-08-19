package com.example.dailymacros.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dailymacros.ui.NavigationRoute
import kotlin.math.roundToInt

@Composable
fun ExerciseInfo(
    exerciseInfoList: List<ExerciseInfoData>,
    navController: NavHostController,
    date : String
) {
    var expanded by remember { mutableStateOf(false) }

    // Calculate total calories burned
    val totalCalories = if (exerciseInfoList.isNotEmpty()) exerciseInfoList.sumOf { it.caloriesBurned.toDouble() } else 0.0

    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 20.dp).clip(RoundedCornerShape(12.dp))) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Exercise",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "-${totalCalories.roundToInt()} kcal",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = expanded,
            enter = slideInVertically(animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)),
            exit = slideOutVertically(animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.2f))
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                exerciseInfoList.forEach { exerciseInfo ->
                    ExerciseInfoBar(
                        exercise = exerciseInfo.exercise,
                        caloriesBurned = exerciseInfo.caloriesBurned,
                        duration = exerciseInfo.duration, // Pass duration to ExerciseInfoBar
                        modifier = Modifier.fillMaxWidth()
                    )
                    Divider(color = MaterialTheme.colorScheme.surface, thickness = 1.dp)
                }
                Button(
                    onClick = { navController.navigate(NavigationRoute.SelectExercise.route + "?date=${date}") },
                    modifier = Modifier
                        .width(180.dp)
                        .padding(top = 8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Select exercise", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}
data class ExerciseInfoData(
    val exercise: String,
    val caloriesBurned: Float,
    val duration: Int
)