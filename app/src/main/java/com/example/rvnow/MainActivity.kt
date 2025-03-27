package com.example.rvnow

import LoginScreen
import SignupScreen
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.rvnow.model.RV
import com.example.rvnow.model.RVType
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rvnow.viewmodels.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import coil.compose.rememberAsyncImagePainter
import com.example.rvnow.screens.OwnerScreen
import com.example.rvnow.viewmodels.RVViewModel

//import androidx.compose.runtime.observeAsState
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            RVNowApp()
//        }
//    }
//}




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val authViewModel: AuthViewModel = viewModel()
            RVNowApp(authViewModel = authViewModel)
        }
    }
}

@Composable
fun RVNowApp(authViewModel: AuthViewModel) {
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
//            textStyle = TextStyle(fontSize = 34.sp, color = Color.White, fontWeight = FontWeight.Bold),
        )
    }
    Scaffold(
        bottomBar = { BottomNavBar(navController, authViewModel) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,  // Dynamic start destination based on login state
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen(navController = navController) }
            composable("owner") { OwnerScreen(navController = navController) }
            composable("signup") { SignupScreen(navController = navController) }
//            composable("login") { LoginScreen(navController = navController) }
            composable("Signin|up") { LoginScreen(navController = navController, authViewModel = authViewModel) }
            composable("rental") { RentalScreen(navController = navController) }
            composable("sales") { SalesScreen(rvs = getSampleRVs(), navController = navController) }
            composable("profile") { ProfileScreen(
                navController = navController,
                authViewModel = AuthViewModel(),
                rvViewModel = RVViewModel()
            ) }

            composable("detail/{rvId}") { backStackEntry ->
                val rvId = backStackEntry.arguments?.getString("rvId") ?: ""
                val rvViewModel: RVViewModel = viewModel()
                RVDetailScreen(rvId = rvId, rvViewModel = rvViewModel, navController = navController)
            }

        }
    }
}

@Composable
fun BottomNavBar(navController: NavController, authViewModel: AuthViewModel) {
    // Observe authentication state from the AuthViewModel
    val isLoggedIn by authViewModel.isLoggedIn.observeAsState(false)
    val items = listOf("Home", "Rental", "Sales", "Owner", if (isLoggedIn) "Profile" else "Signin|up")
    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.DirectionsCar,
        Icons.Filled.DirectionsCar,
        Icons.Default.Person,
        if (isLoggedIn) Icons.Default.Person else Icons.Default.Login
    )

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




//@Composable
//fun RVNowApp() {
//    val navController = rememberNavController()
//    val image1 = rememberImagePainter("file:///android_asset/images/11.jpeg")
//    val auth = FirebaseAuth.getInstance()
//    val currentUser = auth.currentUser
//
//    val startDestination = if (currentUser != null) "profile" else "Signin|up"
//    // Debugging log
////    println("RVNowApp is being recomposed!")
////
////    Box(
////        modifier = Modifier
////            .fillMaxWidth()
////            .height(150.dp)
////            .padding(bottom = 16.dp)
////            .background(color = Color.White)
////    ) {
////        Image(
////            painter = image1,
////            contentDescription = "RV Image",
////            modifier = Modifier.fillMaxSize()
////        )
////
////        Text(
////            text = "Welcome to RVNow",
////            color = Color.Black,
////            fontSize = 34.sp,
////            fontWeight = FontWeight.Bold,
////            modifier = Modifier.align(Alignment.Center),
//////            textStyle = TextStyle(fontSize = 34.sp, color = Color.White, fontWeight = FontWeight.Bold),
////        )
////    }
//
//
//    Scaffold(
//        bottomBar = {  BottomNavBar(navController) }
//    ) { innerPadding ->
//        NavHost(
//            navController = navController,
//            startDestination = "home",
//            modifier = Modifier.padding(innerPadding)
//        ) {
//            composable("home") { HomeScreen( navController = navController) }
//            composable("signup") { SignupScreen( navController = navController) }
//            composable("Signin|up") { LoginScreen( navController = navController) }
//            composable("rental") { RentalScreen(navController = navController) }
//            composable("sales") { SalesScreen(rvs = getSampleRVs(), navController = navController) }
//            composable("profile") { ProfileScreen(rvs = getSampleRVs(), navController = navController) }
////            composable("detail/{rvId}") {
////                backStackEntry ->
////                val rvId = backStackEntry.arguments?.getString("rvId")?.toInt() ?: 0
////                RVDetailScreen(rvId = rvId, rvs = getSampleRVs(), navController = navController)
////            }
//            composable("detail/{rvId}") { backStackEntry ->
//                val rvId = backStackEntry.arguments?.getString("rvId") ?: ""
//                RVDetailScreen(rvId = rvId, rvs = getSampleRVs(), navController = navController)
//            }
//        }
//    }
//}
//

//@Composable
//fun BottomNavBar(navController: NavController) {
//    val items = listOf("Home", "Rental", "Sales","Owner", "Signin|up")
//    val icons = listOf(
//        Icons.Default.Home,
//        Icons.Default.DirectionsCar,
//        Icons.Filled.DirectionsCar,
//        Icons.Default.Person,
//        Icons.Default.Login)
//
//    NavigationBar {
//        items.forEachIndexed { index, screen ->
//            NavigationBarItem(
//                icon = { Icon(icons[index], contentDescription = screen) },
//                label = { Text(screen) },
//                selected = false, // Change based on state
//                onClick = {
//                    navController.navigate(screen.lowercase()) // Navigate to the screen
//                }
//            )
//        }
//    }
//}
//
//// Sample RV data
fun getSampleRVs(): List<RV> {
    return listOf(
        RV(
            name = "Luxury RV",
            id = "1",
            ownerId ="23",
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
//            isForSale = true,
            imageUrl = "file:///android_asset/images/1.jpg"
        ),
        RV(
            name = "Off-Road RV",
            id = "2",
            ownerId ="24",
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
//            isForSale = true,
            imageUrl = "file:///android_asset/images/2.jpg"
        ),
        RV(
            name = "Camper Van",
            id = "3",
            ownerId ="26",
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
//            isForSale = false,
            imageUrl = "file:///android_asset/images/3.jpg"
        )
    )
}


