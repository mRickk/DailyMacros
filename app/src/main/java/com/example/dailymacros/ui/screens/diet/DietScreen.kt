package com.example.dailymacros.ui.screens.diet

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.ContentAlpha
import com.example.dailymacros.ui.composables.DMTopAppBar
import com.example.dailymacros.ui.composables.NavBar
import com.example.dailymacros.ui.composables.PieChart
import com.example.dailymacros.ui.theme.Carbs
import com.example.dailymacros.ui.theme.Fat
import com.example.dailymacros.ui.theme.Protein

@Composable
fun Diet(navController: NavHostController, dietScreenViewModel: DietViewModel) {

    val user = dietScreenViewModel.loggedUser.user




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
                    text = "Total calorie needs",
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
                        text = "${user?.dailyKcal} kcal",
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
                Spacer(modifier = Modifier.height(16.dp)) // Spacer between title and macronutrients
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
                        Text(text = "200g", fontWeight = FontWeight.Bold, color = Carbs)
                        Text(text = "800 kcal", color = Carbs)
                    }
                    // Fats Column
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Fats", color = Fat)
                        Text(text = "70g", fontWeight = FontWeight.Bold, color = Fat)
                        Text(text = "630 kcal", color = Fat)
                    }
                    // Proteins Column
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Proteins", color = Protein)
                        Text(text = "100g", fontWeight = FontWeight.Bold, color = Protein)
                        Text(text = "400 kcal", color = Protein)
                    }
                }
            }
            item {
                val chartColors = listOf(
                    Carbs,
                    Fat,
                    Protein
                )

                val chartValues = listOf(60f, 110f, 20f)

                PieChart(
                    modifier = Modifier.padding(20.dp),
                    colors = chartColors,
                    inputValues = chartValues,
                    textColor = MaterialTheme.colorScheme.onSurface
                )
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled)
                )
            }
            item {
                // Section: Additional Options
                Text(
                    text = "Additional Options",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp)) // Spacer between title and boxes
                // Column for clickable boxes
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Type of Diet Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled))
                            .clickable { /* Handle click */ }
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Type of diet")
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Value 1")
                                Icon(imageVector = Icons.Default.ArrowForwardIos, contentDescription = null)
                            }
                        }
                    }
                    // Activity Level Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled))
                            .clickable { /* Handle click */ }
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Activity level")
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "${user?.activity?.string}")
                                Icon(imageVector = Icons.Default.ArrowForwardIos, contentDescription = null)
                            }
                        }
                    }
                    // Obiettivo Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled))
                            .clickable { /* Handle click */ }
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Objective")
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "${user?.goal?.string}")
                                Icon(imageVector = Icons.Default.ArrowForwardIos, contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
    }
}