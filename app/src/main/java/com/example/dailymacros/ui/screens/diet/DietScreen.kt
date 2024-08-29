package com.example.dailymacros.ui.screens.diet

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.ContentAlpha
import com.example.dailymacros.data.database.ActivityType
import com.example.dailymacros.data.database.DietType
import com.example.dailymacros.data.database.GoalType
import com.example.dailymacros.ui.composables.DMTopAppBar
import com.example.dailymacros.ui.composables.NavBar
import com.example.dailymacros.ui.composables.PieChart
import com.example.dailymacros.ui.theme.Carbs
import com.example.dailymacros.ui.theme.Fat
import com.example.dailymacros.ui.theme.Protein
import com.example.dailymacros.utilities.MacrosKcal
import kotlin.math.roundToInt

@Composable
fun Diet(navController: NavHostController, dietViewModel: DietViewModel) {
    dietViewModel.actions.getUser()
    var user = dietViewModel.loggedUser.user
    var diet by remember { mutableStateOf(DietType.STANDARD) }
    var goal by remember { mutableStateOf(GoalType.MAINTAIN_WEIGHT) }
    var activity by remember { mutableStateOf(ActivityType.SEDENTARY) }
    var dailyKcal by remember { mutableStateOf(0) }

    fun updateValues() {
        diet = user!!.diet
        goal = user!!.goal
        activity = user!!.activity
        dailyKcal = user!!.dailyKcal
    }

    LaunchedEffect(user) {
        if (user != null) {
            updateValues()
        }
    }

    Scaffold(
        topBar = { DMTopAppBar(navController) },
        bottomBar = { NavBar(navController, selectedIndex = 1) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // Section: Total Calorie Needs
                Text(
                    text = "Total caloric needs",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${dailyKcal ?: "N/A"} kcal",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled)
                )
            }
            item {
                // Section: Macronutrients Split
                Text(
                    text = "Macronutrients split",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp)) // Spacer between title and macronutrients
                // Row for Macronutrients
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Carbohydrates Column
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Carbohydrates", color = Carbs)
                        Text(text = "%.1f".format(dailyKcal * diet.carbsPerc / MacrosKcal.CARBS.kcal) + "g", fontWeight = FontWeight.Bold, color = Carbs)
                        Text(text = "${(dailyKcal * diet.carbsPerc).roundToInt()} kcal", color = Carbs)
                    }
                    // Fats Column
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Fats", color = Fat)
                        Text(text = "%.1f".format(dailyKcal * diet.fatPerc / MacrosKcal.FAT.kcal) + "g", fontWeight = FontWeight.Bold, color = Fat)
                        Text(text = "${(dailyKcal * diet.fatPerc).roundToInt()} kcal", color = Fat)
                    }
                    // Proteins Column
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Proteins", color = Protein)
                        Text(text = "%.1f".format(dailyKcal * diet.proteinPerc / MacrosKcal.PROTEIN.kcal) + "g", fontWeight = FontWeight.Bold, color = Protein)
                        Text(text = "${(dailyKcal * diet.proteinPerc).roundToInt()} kcal", color = Protein)
                    }
                }
            }
            item {
                val chartColors = listOf(
                    Carbs,
                    Fat,
                    Protein
                )

                val chartValues = listOf(diet.carbsPerc, diet.fatPerc, diet.proteinPerc)

                PieChart(
                    modifier = Modifier.padding(horizontal = 85.dp, vertical = 20.dp),
                    colors = chartColors,
                    inputValues = chartValues,
                    textColor = MaterialTheme.colorScheme.onSurface
                )
            }
            item {
                // Column for dropdown menus
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    var expandedDiet by remember { mutableStateOf(false) }
                    var expandedActivity by remember { mutableStateOf(false) }
                    var expandedGoal by remember { mutableStateOf(false) }

                    // Type of Diet Dropdown
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled))
                            .clickable { expandedDiet = true }
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Diet", color = MaterialTheme.colorScheme.onSecondary)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = diet.string ?: "N/A")
                                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        }
                        DropdownMenu(
                            expanded = expandedDiet,
                            onDismissRequest = { expandedDiet = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            DietType.entries.forEach { option ->
                                DropdownMenuItem(
                                    onClick = {
                                        Log.v("DietScreen", "Option: $option")
                                        expandedDiet = false
                                        dietViewModel.actions.updateUser(option, dietViewModel.loggedUser.user!!.activity, dietViewModel.loggedUser.user!!.goal)
                                        user = dietViewModel.loggedUser.user
                                        updateValues()
                                    },
                                    text = { Text(text = option.string) }
                                )
                            }
                        }
                    }

                    // Activity Level Dropdown
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled))
                            .clickable { expandedActivity = true }
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Activity level", color = MaterialTheme.colorScheme.onSecondary)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column (horizontalAlignment = Alignment.End) {
                                    Text(text = activity.string ?: "N/A")
                                    Text(text = activity.description,
                                        fontSize = MaterialTheme.typography.bodySmall.fontSize)
                                }

                                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        }
                        DropdownMenu(
                            expanded = expandedActivity,
                            onDismissRequest = { expandedActivity = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ActivityType.entries.forEach { option ->
                                DropdownMenuItem(
                                    onClick = {
                                        Log.v("DietScreen", "Option: $option")
                                        expandedActivity = false
                                        dietViewModel.actions.updateUser(dietViewModel.loggedUser.user!!.diet, option, dietViewModel.loggedUser.user!!.goal)
                                        user = dietViewModel.loggedUser.user
                                        updateValues()
                                    },
                                    text = { Text(text = option.string) }
                                )
                            }
                        }
                    }

                    // Goal Dropdown
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled))
                            .clickable { expandedGoal = true }
                            .padding(16.dp),
                        contentAlignment = Alignment.Center

                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Goal", color = MaterialTheme.colorScheme.onSecondary)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = goal.string ?: "N/A")
                                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        }
                        DropdownMenu(
                            expanded = expandedGoal,
                            onDismissRequest = { expandedGoal = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            GoalType.entries.forEach { option ->
                                DropdownMenuItem(
                                    onClick = {
                                        Log.v("DietScreen", "Option: $option")
                                        expandedGoal = false
                                        dietViewModel.actions.updateUser(dietViewModel.loggedUser.user!!.diet, dietViewModel.loggedUser.user!!.activity, option)
                                        user = dietViewModel.loggedUser.user
                                        updateValues()
                                    },
                                    text = { Text(text = option.string) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}