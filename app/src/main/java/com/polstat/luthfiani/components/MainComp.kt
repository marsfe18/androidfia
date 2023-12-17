package com.polstat.luthfiani.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.polstat.luthfiani.Destinations

@Composable
fun BottomBarApp(
    navigationSelectedItem: Int,
    navController: NavHostController = rememberNavController(),
    pindahSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color.Transparent,
//        modifier = Modifier.height(50.dp)
    ) {
        BottomNavItems.forEachIndexed { index, bottomNavItem ->
            NavigationBarItem(
                selected = index == navigationSelectedItem,
                onClick = {
                    pindahSelected(index)
                    navController.navigate(bottomNavItem.route)
                },
                icon = {
                    Icon(
                        imageVector = bottomNavItem.icon,
                        contentDescription = bottomNavItem.label
                    )
                },
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route:String,
)

val BottomNavItems = listOf(
    BottomNavItem(
        label = "Home",
        icon = Icons.Filled.Home,
        route = Destinations.HOME_SCREEN
    ),
    BottomNavItem(
        label = "Account",
        icon = Icons.Filled.Person,
        route = Destinations.PROFILE_SCREEN
    )
)