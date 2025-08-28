package com.example.evbuddy

import androidx.compose.foundation.background

import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.*

sealed class BottomNavItem(val route: String, val label: String, val icon: Int) {
    object Home : BottomNavItem("home", "Home", android.R.drawable.ic_menu_view)
    object Requests : BottomNavItem("requests", "My Requests", android.R.drawable.ic_menu_agenda)
    object Profile : BottomNavItem("profile", "Profile", android.R.drawable.ic_menu_myplaces)
}


data class Driver(
    val name: String,
    val distanceKm: Double,
    val etaMinutes: Int,
    val rating: Double
)



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("EV Buddy") })
        },
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) { HomeScreen() }
            composable(BottomNavItem.Requests.route) { RequestsScreen() }
            composable(BottomNavItem.Profile.route) { ProfileScreen() }
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Requests,
        BottomNavItem.Profile
    )

    NavigationBar {
        val currentRoute = currentRoute(navController)
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun DriverRow(driver: Driver) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(driver.name, style = MaterialTheme.typography.bodyLarge)
            Text("${driver.distanceKm} km • ETA ${driver.etaMinutes} min",
                style = MaterialTheme.typography.bodySmall)
        }
        Text("⭐ ${driver.rating}", style = MaterialTheme.typography.bodyMedium)
    }
}


// Screens
@Composable
fun HomeScreen() {
    var showDrivers by remember { mutableStateOf(false) }

    // dummy driver list
    val drivers = listOf(
        Driver("Alice Johnson", 2.5, 5, 4.8),
        Driver("Brian Smith", 4.0, 8, 4.6),
        Driver("Carlos Lopez", 1.2, 3, 4.9),
        Driver("Diana Lee", 3.7, 7, 4.7)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showDrivers = !showDrivers },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Find Driver")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { /*Find Charger */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text("Find Charger")
        }

        Spacer(modifier = Modifier.height(24.dp))


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 8.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            // Map placeholder
            Text(
                "Map Placeholder",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Center)
            )

            // Drivers list overlay
            if (showDrivers) {
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Available Drivers", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))

                        drivers.forEach { driver ->
                            DriverRow(driver)
                            Divider()
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun RequestsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
        Text("My Requests Screen")
    }
}

@Composable
fun ProfileScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
        Text("Profile Screen")
    }
}

// Helper
@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen()
}
