package com.example.dailymacros.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.dailymacros.data.database.FoodUnit
import com.example.dailymacros.ui.theme.Carbs
import com.example.dailymacros.ui.theme.Fat
import com.example.dailymacros.ui.theme.Protein
import kotlin.math.roundToInt

@Composable
fun FoodInfo(
    food: String,
    quantity: Float,
    carbsQty: Float,
    fatQty: Float,
    protQty: Float,
    kcal: Float,
    unit: String,
    modifier: Modifier = Modifier,

) {
    return Column(modifier = modifier.padding(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = food,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "${kcal.roundToInt()} kcal",
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "%.1f".format(quantity) + unit,
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = buildAnnotatedString {
                    append("C:")
                    withStyle(style = SpanStyle(color = Carbs)) {
                        append("%.1f".format(carbsQty) + "g")
                    }
                    append(" F:")
                    withStyle(style = SpanStyle(color = Fat)) {
                        append("%.1f".format(fatQty) + "g")
                    }
                    append(" P:")
                    withStyle(style = SpanStyle(color = Protein)) {
                        append("%.1f".format(protQty) + "g")
                    }
                },
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

}