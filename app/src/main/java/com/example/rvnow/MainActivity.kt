package com.example.rvnow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.rvnow.model.RV
import com.example.rvnow.model.RVType
import com.example.rvnow.screens.*
import com.example.rvnow.viewmodels.AuthViewModel
import com.example.rvnow.viewmodels.RVViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val authViewModel: AuthViewModel = viewModel()
            val rvViewModel: RVViewModel = viewModel()
            RVNowApp(authViewModel = authViewModel, rvViewModel = rvViewModel)
        }
    }
}

@Composable
fun RVNowApp(authViewModel: AuthViewModel, rvViewModel: RVViewModel) {
    val navController = rememberNavController()
    val image1 = rememberAsyncImagePainter("file:///android_asset/images/11.jpeg")
    val startDestination = "home"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(bottom = 16.dp)
            .background(color = Color.White)
    ) {
        Image(
            painter = image1,
            contentDescription = "RV Image",
            modifier = Modifier.fillMaxSize()
        )

        Text(
            text = "Welcome to RVNow",
            color = Color.Black,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center),
        )
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController, authViewModel) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen(navController = navController, authViewModel = authViewModel)
            }
            composable("cart") {
                CartScreen(
                    navController = navController,
                    authViewModel = authViewModel,
                    rvViewModel = rvViewModel
                )
            }
            composable("owner") { OwnerScreen(navController = navController) }
            composable("signup") {
                SignupScreen(navController = navController, authViewModel = authViewModel)
            }
            composable("Signin|up") {
                LoginScreen(navController = navController, authViewModel = authViewModel)
            }
            composable("rental") { RentalScreen(navController = navController) }
            composable("sales") { SalesScreen(navController = navController) }
            composable("profile") {
                ProfileScreen(
                    navController = navController,
                    authViewModel = authViewModel,
                    rvViewModel = rvViewModel
                )
            }
            composable("go_rving") { GoRVingScreen(navController = navController) }

            composable("travel_guide_details/{guideId}") { backStackEntry ->
                val guideId = backStackEntry.arguments?.getString("guideId") ?: ""
                TravelGuideDetailsScreen(navController = navController, guideId = guideId)
            }

            // 目的地详情页面路由
            composable(
                route = "destination_details/{destinationId}",
                arguments = listOf(navArgument("destinationId") { type = NavType.StringType })
            ) { backStackEntry ->
                val destinationId = backStackEntry.arguments?.getString("destinationId") ?: ""
                DestinationDetailsScreen(navController, destinationId)
            }

            // 国家目的地列表路由
            composable(
                route = "country_destinations/{country}",
                arguments = listOf(navArgument("country") { type = NavType.StringType })
            ) { backStackEntry ->
                val country = backStackEntry.arguments?.getString("country") ?: ""
                CountryDestinationsScreen(navController, country)
            }

            // 搜索结果页面路由
            composable("search_results") {
                SearchResultsScreen(navController)
            }

            composable("detail/{rvId}?sourcePage={sourcePage}") { backStackEntry ->
                val rvId = backStackEntry.arguments?.getString("rvId") ?: ""
                val sourcePage = backStackEntry.arguments?.getString("sourcePage") ?: "home"
                RVDetailScreen(
                    rvId = rvId,
                    rvViewModel = rvViewModel,
                    authViewModel = authViewModel,
                    navController = navController,
                    sourcePage = sourcePage
                )
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController, authViewModel: AuthViewModel) {
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState(false)

    val items = listOf(
        NavItem("Home", Icons.Default.Home, "home"),
        NavItem("Rent", Icons.Default.DirectionsCar, "rental"),
        NavItem("Buy", Icons.Default.ShoppingCart, "sales"),
        NavItem("Owner", Icons.Default.Key, "owner"),
        NavItem("Cart", Icons.Default.Casino, "cart"),
        NavItem(
            if (isLoggedIn) "Profile" else "Login",
            if (isLoggedIn) Icons.Default.Person else Icons.Default.Login,
            if (isLoggedIn) "profile" else "Signin|up"
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class NavItem(val label: String, val icon: ImageVector, val route: String)

fun getSampleRVs(): List<RV> {
    return listOf(
        RV(
            name = "Luxury RV",
            id = "1",
            ownerId = "23",
            type = RVType.Rental,
            pricePerDay = 100000.1,
            description = "Spacious and comfortable",
            place = "Helsinki",
            driverLicenceRequired = "B",
            insurance = mapOf(
                "provider" to "more on the way",
                "coverage" to "fully"
            ),
            kilometerLimitation = 320,
            imageUrl = "file:///android_asset/images/1.jpg"
        ),
        RV(
            name = "Off-Road RV",
            id = "2",
            ownerId = "24",
            type = RVType.Rental,
            pricePerDay = 150000.1,
            description = "Built for adventure",
            place = "Kempele",
            additionalImages = listOf(
                "file:///android_asset/images/3.jpg",
                "file:///android_asset/images/1.jpg",
                "file:///android_asset/images/2.jpg"
            ),
            driverLicenceRequired = "B",
            insurance = mapOf(
                "provider" to "more on the way",
                "coverage" to "fully"
            ),
            kilometerLimitation = 320,
            imageUrl = "file:///android_asset/images/2.jpg"
        ),
        RV(
            name = "Camper Van",
            id = "3",
            ownerId = "26",
            type = RVType.Sales,
            pricePerDay = 300000.1,
            description = "Compact yet cozy",
            place = "Oulu",
            driverLicenceRequired = "B",
            additionalImages = listOf(
                "file:///android_asset/images/3.jpg",
                "file:///android_asset/images/1.jpg",
                "file:///android_asset/images/2.jpg"
            ),
            insurance = mapOf(
                "provider" to "more on the way",
                "coverage" to "fully"
            ),
            kilometerLimitation = 320,
            imageUrl = "file:///android_asset/images/3.jpg"
        )
    )
}