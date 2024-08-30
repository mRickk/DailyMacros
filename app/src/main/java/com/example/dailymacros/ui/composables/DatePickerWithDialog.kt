package com.example.dailymacros.ui.composables

import android.graphics.drawable.Icon
import android.text.format.DateUtils
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.dailymacros.ui.screens.diary.DiaryViewModel
import com.example.dailymacros.ui.screens.overview.OverviewViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun datePickerWithDialog(
    modifier: Modifier = Modifier,
): Long? {
    val startOfDayMillis = LocalDate.now()
        .atStartOfDay(ZoneOffset.UTC)
        .toInstant()
        .toEpochMilli()
    var selectedDateMillis by remember { mutableStateOf<Long?>(startOfDayMillis) }
    val dateState = rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis)
    var showDialog by remember { mutableStateOf(false) }
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                )
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    selectedDateMillis = selectedDateMillis!! - DateUtils.DAY_IN_MILLIS
                },
                enabled = selectedDateMillis != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                )
            ) { Icon(Icons.Default.ArrowBackIos, contentDescription = "Day before") }
            Text(
                text = selectedDateMillis?.let { dateFormat.format(Date(it)) } ?: dateFormat.format(Date()),
                color = MaterialTheme.colorScheme.onTertiary,
                modifier = Modifier
                    .clickable(onClick = {
                        showDialog = true
                    })
                    .padding(horizontal = 4.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium
            )
            Button(
                onClick = {
                    selectedDateMillis = selectedDateMillis!! + DateUtils.DAY_IN_MILLIS
                },
                enabled = selectedDateMillis != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                )
            ) {
                Icon(Icons.Default.ArrowForwardIos, contentDescription = "Day after")
            }
        }
        if (showDialog) {
            DatePickerDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Button(
                        onClick = {
                            selectedDateMillis = dateState.selectedDateMillis
                            showDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        )
                    ) {
                        Text(text = "OK")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        )
                    ) {
                        Text(text = "Cancel")
                    }
                },
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    titleContentColor = MaterialTheme.colorScheme.onSecondary,
                    headlineContentColor = MaterialTheme.colorScheme.onSecondary,
                    weekdayContentColor = MaterialTheme.colorScheme.onSecondary,
                    subheadContentColor = MaterialTheme.colorScheme.onSecondary,
                    yearContentColor = MaterialTheme.colorScheme.onSecondary,
                    currentYearContentColor = MaterialTheme.colorScheme.onSecondary,
                    selectedYearContentColor = MaterialTheme.colorScheme.onSecondary,
                    selectedYearContainerColor = MaterialTheme.colorScheme.secondary,
                    dayContentColor = MaterialTheme.colorScheme.onSecondary,
                    disabledDayContentColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.38f),
                    selectedDayContentColor = MaterialTheme.colorScheme.onSecondary,
                    disabledSelectedDayContentColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.38f),
                    selectedDayContainerColor = MaterialTheme.colorScheme.secondary,
                    disabledSelectedDayContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.38f),
                    todayContentColor = MaterialTheme.colorScheme.onSecondary,
                    todayDateBorderColor = MaterialTheme.colorScheme.onSecondary,
                    dayInSelectionRangeContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f),
                    dayInSelectionRangeContentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                DatePicker(
                    state = dateState,
                    showModeToggle = true,
                    modifier = Modifier.background(MaterialTheme.colorScheme.secondary),
                    colors = DatePickerDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        titleContentColor = MaterialTheme.colorScheme.onSecondary,
                        headlineContentColor = MaterialTheme.colorScheme.onSecondary,
                        weekdayContentColor = MaterialTheme.colorScheme.onSecondary,
                        subheadContentColor = MaterialTheme.colorScheme.onSecondary,
                        yearContentColor = MaterialTheme.colorScheme.onSecondary,
                        currentYearContentColor = MaterialTheme.colorScheme.onSecondary,
                        selectedYearContentColor = MaterialTheme.colorScheme.onSecondary,
                        selectedYearContainerColor = MaterialTheme.colorScheme.secondary,
                        dayContentColor = MaterialTheme.colorScheme.onSecondary,
                        disabledDayContentColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.38f),
                        selectedDayContentColor = MaterialTheme.colorScheme.onBackground,
                        disabledSelectedDayContentColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.38f),
                        selectedDayContainerColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.38f),
                        disabledSelectedDayContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.38f),
                        todayContentColor = MaterialTheme.colorScheme.onSecondary,
                        todayDateBorderColor = MaterialTheme.colorScheme.onSecondary,
                        dayInSelectionRangeContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f),
                        dayInSelectionRangeContentColor = MaterialTheme.colorScheme.onSecondary
                    )
                )
            }
        }
    }
    return selectedDateMillis
}