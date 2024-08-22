package com.example.dailymacros.ui.screens.selectfood

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Delete
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
import com.example.dailymacros.ui.NavigationRoute
import com.example.dailymacros.data.database.Food
import com.example.dailymacros.data.database.MealType
import com.example.dailymacros.ui.composables.FoodInfo
import kotlin.math.roundToInt

@Composable
fun SelectFoodScreen(
    navController: NavHostController,
    viewModel: SelectFoodViewModel,
    state: SelectFoodState,
    date: String?,
    mealType: MealType?,
    selectedFoodNameNull: String?,
    selectedQuantity: Float?
) {
    val defaultQty = 100f
    var selectedFood by remember { mutableStateOf<Food?>(null) }
    var quantity by remember { mutableFloatStateOf(selectedQuantity ?: 100f) }
    var showDialog by remember { mutableStateOf(false) }

    val isQuantityValid by remember {
        derivedStateOf { quantity > 0f }
    }
    selectedFood = state.foodList.firstOrNull { it.name == selectedFoodNameNull }
    Scaffold(
        topBar = { DMTopAppBar(navController, showBackArrow = true) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(NavigationRoute.AddFood.route) },
                modifier = Modifier.padding(8.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add new Food",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { paddingValues ->

        Box(modifier = Modifier.padding(paddingValues)) {
            LazyColumn {
                items(state.foodList) { food ->
                    FoodInfo(
                        food = food.name,
                        quantity = defaultQty,
                        carbsQty = food.carbsPerc * defaultQty,
                        fatQty = food.fatPerc * defaultQty,
                        protQty = food.proteinPerc * defaultQty,
                        kcal = food.kcalPerc * defaultQty,
                        unit = food.unit.string,
                        modifier = Modifier.clickable {
                            selectedFood = food
                        }
                    )
                }
            }

            selectedFood?.let { food ->
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
                            IconButton(onClick = { selectedFood = null }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Close"
                                )
                            }
                            IconButton(onClick = {
                                navController.navigate(NavigationRoute.AddFood.route + "?foodName=${food.name}")
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Modify Food"
                                )
                            }
                            IconButton(onClick = { showDialog = true }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Food"
                                )
                            }
                        }

                        Text(text = "Name: ${food.name}", fontSize = 20.sp)
                        Text(text = "Description: ${food.description ?: "-"}", fontSize = 16.sp)
                        Text(text = "Calories: ${(food.kcalPerc * quantity).roundToInt()} kcal", fontSize = 16.sp)
                        Text(text = "Carbs: ${"%.1f".format(food.carbsPerc * quantity)}g", fontSize = 16.sp)
                        Text(text = "Fat: ${"%.1f".format(food.fatPerc * quantity)}g", fontSize = 16.sp)
                        Text(text = "Protein: ${"%.1f".format(food.proteinPerc * quantity)}g", fontSize = 16.sp)

                        OutlinedTextField(
                            value = quantity.toString(),
                            onValueChange = {
                                quantity = (it.toFloatOrNull() ?: 0f)
                            },
                            label = { Text("Quantity (${food.unit.string})") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        )

                        Button(
                            onClick = {
                                viewModel.actions.insertFoodInsideMeal(
                                    food = food,
                                    date = date!!,
                                    mealType = mealType!!,
                                    quantity = quantity
                                )
                                navController.navigate(NavigationRoute.Diary.route)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            enabled = isQuantityValid && date != null && mealType != null
                        ) {
                            Text("Insert food")
                        }
                    }
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Delete ${selectedFood?.name}") },
                text = {
                    Text("Are you sure to permanently delete this food?", fontSize = MaterialTheme.typography.bodySmall.fontSize)
                    Text("\nWARNING: every record of this food inside the diary will be removed!", fontWeight = FontWeight.Bold)
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.actions.deleteFood(selectedFood!!)
                            showDialog = false
                            selectedFood = null
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