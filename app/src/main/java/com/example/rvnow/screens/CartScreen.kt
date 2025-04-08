//package com.example.rvnow
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.painter.Painter
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import coil.compose.rememberAsyncImagePainter
//import com.example.rvnow.model.RV
//import com.example.rvnow.viewmodels.RVViewModel
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.livedata.observeAsState
//import androidx.compose.runtime.setValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontFamily
//import com.example.rvnow.viewmodels.AuthViewModel
//
//
//@Composable
//fun CartScreen(
//    navController: NavController,
//    rvViewModel: RVViewModel,
//    authViewModel: AuthViewModel
//) {
//    val cartItems by rvViewModel.cartItems.collectAsState()
//    val currentUser by authViewModel.userInfo.observeAsState()
//    val context = LocalContext.current
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        Text("Your Cart", style = MaterialTheme.typography.h4)
//
//        if (cartItems.isEmpty()) {
//            Text("Your cart is empty", modifier = Modifier.padding(16.dp))
//        } else {
//            LazyColumn {
//                items(cartItems) { item ->
//                    CartItemCard(item, rvViewModel)
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Checkout Button
//            Button(
//                onClick = {
//                    currentUser?.uid?.let { userId ->
//                        rvViewModel.checkout(userId) { success ->
//                            if (success) {
//                                Toast.makeText(context, "Checkout successful!", Toast.LENGTH_SHORT).show()
//                                navController.navigate("home")
//                            } else {
//                                Toast.makeText(context, "Checkout failed", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    } ?: run {
//                        Toast.makeText(context, "Please login to checkout", Toast.LENGTH_SHORT).show()
//                    }
//                },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Proceed to Checkout")
//            }
//        }
//    }
//}
