package com.example.dailymacros.ui.screens.addfood

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dailymacros.data.database.Food
import com.example.dailymacros.data.database.FoodUnit
import com.example.dailymacros.ui.NavigationRoute
import com.example.dailymacros.ui.composables.DMTopAppBar
import com.example.dailymacros.utilities.MacrosKcal
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodScreen(
    navController: NavHostController,
    addFoodVM: AddFoodViewModel,
    foodNullName: String?
) {
    val compositionCount = remember { mutableStateOf(0) }
    compositionCount.value++
    LaunchedEffect(compositionCount.value) {
        if (addFoodVM.loggedUser.user == null && compositionCount.value == 2) {
            navController.navigate(NavigationRoute.Diary.route)
        }
    }

    if (foodNullName != null) {
        addFoodVM.actions.getFood(foodNullName)
    }
    val expanded = remember { mutableStateOf(false) }
    val food = addFoodVM.state.food
    val foodName = remember { mutableStateOf("") }
    val foodDescription = remember { mutableStateOf("") }
    val carbs = remember { mutableStateOf("") }
    val fat = remember { mutableStateOf("") }
    val prot = remember { mutableStateOf("") }
    var kcal = ((carbs.value.toFloatOrNull() ?: 0f)*MacrosKcal.CARBS.kcal + (fat.value.toFloatOrNull() ?: 0f)*MacrosKcal.FAT.kcal + (prot.value.toFloatOrNull() ?: 0f)*MacrosKcal.PROTEIN.kcal)
    val qty = remember { mutableStateOf("100") }
    val unit = remember { mutableStateOf(FoodUnit.GRAMS) }
    val isFormValid = remember {
        derivedStateOf {
            foodName.value.isNotBlank()
                    && carbs.value.isNotBlank() && carbs.value.toFloatOrNull() != null
                    && fat.value.isNotBlank() && fat.value.toFloatOrNull() != null
                    && prot.value.isNotBlank() && prot.value.toFloatOrNull() != null
                    && qty.value.isNotBlank() && qty.value.toFloatOrNull() != null
        }
    }

    LaunchedEffect(food) {
        if (food != null) {
            val defaultQty = 100
            foodName.value = food.name
            foodDescription.value = food.description ?: ""
            kcal = food.kcalPerc * defaultQty
            carbs.value = "%.2f".format(food.carbsPerc * defaultQty)
            fat.value = "%.2f".format(food.fatPerc * defaultQty)
            prot.value = "%.2f".format(food.proteinPerc * defaultQty)
            qty.value = defaultQty.toString()
            unit.value = food.unit

        }
    }

    Scaffold(
        topBar = { DMTopAppBar(navController, showBackArrow = true) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            OutlinedTextField(
                value = foodName.value,
                onValueChange = { foodName.value = it },
                label = { Text("Food Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = foodDescription.value,
                onValueChange = { foodDescription.value = it },
                label = { Text("Description (Optional)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = kcal.roundToInt().toString(),
                onValueChange = {},
                label = { Text("Kcal") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )
            OutlinedTextField(
                value = carbs.value,
                onValueChange = { carbs.value = it },
                label = { Text("Carbs (g)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = fat.value,
                onValueChange = { fat.value = it },
                label = { Text("Fat (g)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = prot.value,
                onValueChange = { prot.value = it },
                label = { Text("Protein (g)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = qty.value,
                onValueChange = { qty.value = it },
                label = { Text("Quantity (${unit.value.string})") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            // Dropdown for unit selection
            Column {
                OutlinedTextField(
                    value = unit.value.string,
                    onValueChange = {},
                    label = { Text("Unit") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded.value = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    }
                )
                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false }
                ) {
                    FoodUnit.entries.forEach { foodUnit ->
                        DropdownMenuItem(
                            onClick = {
                                unit.value = foodUnit
                                expanded.value = false
                            },
                            text = { Text(foodUnit.string) }
                        )
                    }
                }
            }
            Button(
                onClick = {
                    if (isFormValid.value) {
                        addFoodVM.actions.upsertFood(
                            name = foodName.value,
                            description = foodDescription.value.takeIf { it.isNotBlank() },
                            kcalPerc = kcal / qty.value.toFloat(),
                            carbsPerc = carbs.value.toFloat() / qty.value.toFloat(),
                            fatPerc = fat.value.toFloat() / qty.value.toFloat(),
                            proteinPerc = prot.value.toFloat() / qty.value.toFloat(),
                            unit = unit.value
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
                Text(if (food != null) "Modify food" else "Add new food")
            }
        }
    }
}