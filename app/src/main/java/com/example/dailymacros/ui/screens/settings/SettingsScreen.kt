package com.example.dailymacros.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dailymacros.ui.composables.DMTopAppBar


@Composable
fun Settings(navController: NavHostController, state: ThemeState, onThemeSelected: (theme: Theme) -> Unit) {
    val radioOptions = listOf(Theme.Light, Theme.Dark, Theme.System)
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[1] ) }

    Scaffold(
        topBar = { DMTopAppBar(navController, showBackArrow = true, isSettings = true) }
    ) {paddingValues ->
        Column(Modifier.selectableGroup().padding(paddingValues))
        {
            Theme.entries
                .forEach { theme ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)

                            .selectable(
                                selected = (theme == state.theme),
                                onClick = {
                                    onThemeSelected(theme)
                                },
                                role = Role.RadioButton

                            )
                            .padding(
                                horizontal = 16
                                    .dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        RadioButton(selected = (theme == state.theme), onClick = null)
                        Text(
                            text = theme.toString(),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
        }
    }
}
