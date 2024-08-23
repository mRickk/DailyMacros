package com.example.dailymacros.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.dailymacros.data.database.FoodInsideMealWithFood
import com.example.dailymacros.ui.theme.Carbs
import com.example.dailymacros.ui.theme.Fat
import com.example.dailymacros.ui.theme.Protein
import java.util.SortedMap


data class StackedData(
    val inputs: List<Float>,
    val colors: List<Color>
)
@Composable
fun StackedBarChart(groupedMeals: SortedMap<Long, List<FoodInsideMealWithFood>>) {
    val maxHeight = 200.dp
    val maxCaloriesInOneDay = groupedMeals.values.maxOfOrNull { it.sumOf { i -> (i.food.kcalPerc * i.foodInsideMeal.quantity).toDouble() }.toFloat() } ?: 0f
    val inputs = groupedMeals.values.map {
        Pair(
            it.sumOf { i -> (i.food.kcalPerc * i.foodInsideMeal.quantity).toDouble() }.toFloat() / maxCaloriesInOneDay,
            StackedData(
                listOf(
                    it.sumOf { i -> (i.food.carbsPerc * i.foodInsideMeal.quantity).toDouble() }.toFloat(),
                    it.sumOf { i -> (i.food.fatPerc * i.foodInsideMeal.quantity).toDouble() }.toFloat(),
                    it.sumOf { i -> (i.food.proteinPerc * i.foodInsideMeal.quantity).toDouble() }.toFloat()
                ).toPercent(),
                listOf(Carbs, Fat, Protein)
            )
        )
    }

    val borderColor = MaterialTheme.colorScheme.onSecondary
    val density = LocalDensity.current
    val strokeWidth = with(density) { 1.dp.toPx() }

    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .height(maxHeight)
            .drawBehind {
                // draw X-Axis
                drawLine(
                    color = borderColor,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
                // draw Y-Axis
                drawLine(
                    color = borderColor,
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height),
                    strokeWidth = strokeWidth
                )
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        inputs.forEach { (heightPerc ,item) ->

            Column(modifier = Modifier.weight(1f)) {

                item.inputs.forEachIndexed { index, input ->

                    val itemHeight = remember(input) { input * (heightPerc * maxHeight.value) / 100 }

                    Spacer(
                        modifier = Modifier
                            .padding(horizontal = if (groupedMeals.size <= 7) 10.dp else if(groupedMeals.size <= 31) 3.dp else 0.dp)
                            .height(itemHeight.dp)
                            .fillMaxWidth()
                            .background(item.colors[index])
                    )
                }
            }
        }
    }
}

private fun List<Float>.toPercent(): List<Float> {
    return this.map { item ->
        item * 100 / this.sum()
    }
}