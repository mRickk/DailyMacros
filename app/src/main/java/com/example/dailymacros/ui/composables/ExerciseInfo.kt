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
import com.example.dailymacros.data.database.ExerciseInsideDay
import com.example.dailymacros.ui.NavigationRoute
import com.example.dailymacros.ui.screens.diary.DiaryActions
import kotlin.math.roundToInt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseInfo(
    exerciseInfoList: List<ExerciseInfoData>,
    navController: NavHostController,
    date: String,
    diaryActions: DiaryActions
) {
    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 20.dp).clip(RoundedCornerShape(12.dp))) {
        exerciseInfoList.forEach { exerciseInfo ->
            val dismissState = rememberDismissState(
                confirmValueChange = {
                    if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
                        diaryActions.removeExerciseInsideDay(
                            ExerciseInsideDay(
                                exerciseName = exerciseInfo.exercise,
                                date = date,
                                duration = exerciseInfo.duration
                            )
                        )
                        true
                    } else {
                        false
                    }
                }
            )
            SwipeToDismiss(
                state = dismissState,
                background = {
                    val color = if (dismissState.dismissDirection == DismissDirection.StartToEnd) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color)
                            .padding(8.dp)
                    )
                },
                dismissContent = {
                    ExerciseInfoBar(
                        exercise = exerciseInfo.exercise,
                        caloriesBurned = exerciseInfo.caloriesBurned,
                        duration = exerciseInfo.duration,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    NavigationRoute.SelectExercise.route + "?exerciseName=${exerciseInfo.exercise}"
                                )
                            }
                    )
                }
            )
            Divider(color = MaterialTheme.colorScheme.surface, thickness = 1.dp)
        }
    }
}
data class ExerciseInfoData(
    val exercise: String,
    val caloriesBurned: Float,
    val duration: Int
)