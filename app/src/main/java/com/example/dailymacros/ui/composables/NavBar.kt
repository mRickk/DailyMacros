package com.example.dailymacros.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.dailymacros.ui.NavigationRoute
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun NavBar(navController: NavHostController, selectedIndex: Int = 0) {
    val items = listOf("Diary", "Diet", "Overview")
    val icons = listOf(Icons.Filled.Book, Icons.Filled.PieChart, Icons.Filled.BarChart)
    val routes = listOf(NavigationRoute.Diary.route, NavigationRoute.Diet.route, NavigationRoute.Overview.route)

    NavigationBar(
        modifier = Modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(36.dp)),
        containerColor = MaterialTheme.colorScheme.tertiary,
        contentColor = MaterialTheme.colorScheme.onTertiary
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(icons[index],
                    contentDescription = item,
                    modifier = Modifier.size(40.dp),
                    tint = if (selectedIndex == index) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.6f)
                ) },
                selected = selectedIndex == index,
                onClick = { navController.navigate(routes[index]) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.tertiary,
                ),
                modifier = Modifier.padding(horizontal = 5.dp)
            )
        }
    }
}

data class BarItem(
    val title: String,
    val selectedIcon: Unit,
    val unselectedIcon: Unit,
    val route: String
)