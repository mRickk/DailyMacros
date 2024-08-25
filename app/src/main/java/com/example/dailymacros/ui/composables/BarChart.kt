package com.example.dailymacros.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.dailymacros.ui.theme.Cal
import kotlin.math.roundToInt

@Composable
fun BarChart(
    modifier: Modifier = Modifier,
    values: List<Float>,
    maxHeight: Dp = 200.dp,
    barColor: Color = Cal
) {

    val borderColor = MaterialTheme.colorScheme.onSecondary
    val density = LocalDensity.current
    val strokeWidth = with(density) { 1.dp.toPx() }
    val maxCaloriesBurned = values.maxOrNull() ?: 0f

    Row(
        modifier = modifier.then(
            Modifier
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
                }
        ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        values.forEach { item ->
            val itemHeight = remember(item) { maxHeight.value * (item / maxCaloriesBurned) }


            Column(
                modifier = Modifier
                    .padding(horizontal = if (values.size <= 8) 4.dp else if(values.size <= 31) 2.dp else 0.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                if (values.size <= 8) {
                    Text(
                        text = "-" + (if (item < 1000.0) item.roundToInt().toString() else "%.1f".format(item / 1000) + "k"),
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(itemHeight.dp)
                        .fillMaxWidth()
                        .background(barColor)
                )

            }
        }
    }

}


