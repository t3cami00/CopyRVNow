package com.example.rvnow.screens

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.rvnow.model.RV
//import com.example.rvnow.api.RVInformation.addallRV
import java.util.Calendar
import com.example.rvnow.viewmodels.RVViewModel
import java.util.*
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
//import androidx.compose.material3.Checkbox

@Composable
fun OwnerScreen(navController: NavController, rvViewModel: RVViewModel = viewModel()) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var place by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var isForSale by remember { mutableStateOf(false) }
    var isPopular by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val image1 = rememberAsyncImagePainter("file:///android_asset/images/11.png")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(color = Color.White)
        ) {
            Image(
                painter = image1,
                contentDescription = "RV Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Text(
                text = "Welcome to RVNow",
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Make the "Publish Your RV" title fixed at the top
        Text(text = "Publish Your RV", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        // Add Scrollable area for the inputs
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Add scroll functionality here
                .padding(top = 10.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("RV Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price Per Day") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = place,
                onValueChange = { place = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("Image URL") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isForSale,
                        onCheckedChange = { isForSale = it }
                    )
                    Text(text = "For Sale")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isPopular,
                        onCheckedChange = { isPopular = it }
                    )
                    Text(text = "For Rental")
                }
            }

            Button(
                onClick = {
                    if (name.isNotEmpty() && description.isNotEmpty() && price.isNotEmpty() && place.isNotEmpty()) {
                        val newRV = RV(
                            id = UUID.randomUUID().toString(),
                            name = name,
                            description = description,
                            pricePerDay = price.toDouble(),
                            place = place,
                            imageUrl = imageUrl,
                            ownerId = userId,
                            isForSale = isForSale,
                            isPopular = isPopular
                        )

                        // Call ViewModel function to add RV
                        rvViewModel.addRV(newRV)

                    } else {
                        Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit RV")
            }
        }
    }
}

