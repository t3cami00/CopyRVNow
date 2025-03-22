package com.example.rvnow

import LoginScreen
import SignupScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rvnow.model.RV
import com.example.rvnow.model.RVType


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RVNowApp()
        }
    }
}

@Composable
fun RVNowApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {  BottomNavBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen(rvs = getSampleRVs(), navController = navController) }
            composable("signup") { SignupScreen( navController = navController) }
            composable("Signin|up") { LoginScreen( navController = navController) }
            composable("rental") { RentalScreen(rvs = getSampleRVs(), navController = navController) }
            composable("sales") { SalesScreen(rvs = getSampleRVs(), navController = navController) }
            composable("profile") { ProfileScreen(rvs = getSampleRVs(), navController = navController) }
            composable("detail/{rvId}") {
                backStackEntry ->
                val rvId = backStackEntry.arguments?.getString("rvId")?.toInt() ?: 0
                RVDetailScreen(rvId = rvId, rvs = getSampleRVs(), navController = navController)
            }
        }
    }
}


@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf("Home", "Rental", "Sales","Owner", "Signin|up")
    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.DirectionsCar,
        Icons.Filled.DirectionsCar,
        Icons.Default.Person,
        Icons.Default.Login)

    NavigationBar {
        items.forEachIndexed { index, screen ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = screen) },
                label = { Text(screen) },
                selected = false, // Change based on state
                onClick = {
                    navController.navigate(screen.lowercase()) // Navigate to the screen
                }
            )
        }
    }
}

// Sample RV data
fun getSampleRVs(): List<RV> {
    return listOf(
        RV(
            name = "Luxury RV",
            id = 1,
            type = RVType.Rental,
            price = 100000.1,
            description = "Spacious and comfortable",
            place = "Helsinki",
            driverLicence = "B",
            insuranceInfo = "more on the way",
            kilometerLimitation = 320,
            imageUrl = "file:///android_asset/images/1.jpg"
        ),
        RV(
            name = "Off-Road RV",
            id = 2,
            type = RVType.Rental,
            price = 150000.1,
            description = "Built for adventure",
            place = "Kempele",
            additionalImages = listOf(
                "file:///android_asset/images/3.jpg",
                "file:///android_asset/images/1.jpg",
                "file:///android_asset/images/2.jpg"
            ),
            driverLicence = "B",
            insuranceInfo = "more on the way",
            kilometerLimitation = 320,
            imageUrl = "file:///android_asset/images/2.jpg"
        ),
        RV(
            name = "Camper Van",
            id = 3,
            type = RVType.Sales,
            price = 300000.1,
            description = "Compact yet cozy",
            place = "Oulu",
            driverLicence = "B",
            additionalImages = listOf(
                "file:///android_asset/images/3.jpg",
                "file:///android_asset/images/1.jpg",
                "file:///android_asset/images/2.jpg"
            ),
            insuranceInfo = "more on the way",
            kilometerLimitation = 320,
            imageUrl = "file:///android_asset/images/3.jpg"
        )
    )
}


