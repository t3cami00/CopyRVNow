package com.example.rvnow

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.rvnow.model.RV
import java.util.Calendar
import com.example.rvnow.viewmodels.RVViewModel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*
//import androidx.compose.ui.Alignment
@Composable
fun SalesScreen(
    rvViewModel: RVViewModel = viewModel(),
    navController: NavController
) {
    var drivingType by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var place by remember { mutableStateOf("") }
    val context = LocalContext.current
    val rvList by rvViewModel.rvs.collectAsState()
    val calendar = Calendar.getInstance()
    var isSearchPerformed by remember { mutableStateOf(false) }

    // Date Picker for Start Date
    val startDatePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            startDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Date Picker for End Date
    val endDatePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            endDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    val image1 = rememberAsyncImagePainter("file:///android_asset/images/11.png")
    Column() {

        Box(
            modifier = Modifier
//
//                .padding(10.dp)
                .fillMaxWidth()
                .height(250.dp)
                .padding(bottom = 16.dp)
                .background(color = Color.White)
        ) {

            // Loading image from drawable or assets
            Image(
                painter = image1,
                contentDescription = "RV Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
//                modifier = Modifier.width(30000.dp)

            )

            Text(
                text = "Welcome to RVNow",
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        // Start Date Field
        Row(modifier = Modifier.padding(10.dp)) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable { startDatePickerDialog.show() }
//                    .pointerInput(Unit) {
//                        detectTapGestures(onTap = {
//                            startDatePickerDialog.show()
//                        })
//                    }// Entire box is clickable
            ) {
                OutlinedTextField(
                    value = startDate,
                    onValueChange = { newValue ->
                        startDate = newValue},
                    readOnly = true,  // Prevent manual input
                    label = { Text("Start Date") },
                    modifier = Modifier.fillMaxWidth().focusable(false)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            OutlinedTextField(
                value = endDate,
                onValueChange = {newValue ->
                    endDate = newValue },
                readOnly = true,  // Prevent typing
                label = { Text("End Date") },
                modifier = Modifier
                    .weight(1f)
                    .clickable { endDatePickerDialog.show() } // Open Date Picker
            )

        }



        Row (modifier = Modifier.padding(10.dp)){
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                OutlinedTextField(
                    value = drivingType,
                    onValueChange = { newValue ->
                        drivingType = newValue},
                    label = { Text("Driving Type") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 20.sp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Box(
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = place,
                    onValueChange = {
                            newValue -> place = newValue },
                    label = { Text("Place") },
//                    readOnly = true, // Prevent manual input
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 20.sp)
                )
            }
        }


        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier.
            fillMaxWidth()
//                .width(100.dp) .align(Alignment.Center)
                .clickable { startDatePickerDialog.show() } // Apply clickable to the Box
        ) {
            Button(
                onClick = {
                    isSearchPerformed = true
                    drivingType = ""
                    startDate = ""
                    endDate = ""
                    place = ""},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Search")
            }

        }
        Spacer(modifier = Modifier.height(10.dp))

        val filteredRVs1 = rvList.filter {
            !it.isForSale
        }



        val filteredRVs2 = if (isSearchPerformed) {
            // Parse the user input dates using the same format as the DatePicker output.
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            // Ensure that parsed user dates are in UTC (Firebase stores in UTC)
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")

            val userSearchStart: Date? = if (startDate.isNotEmpty()) dateFormat.parse(startDate) else null
            val userSearchEnd: Date? = if (endDate.isNotEmpty()) dateFormat.parse(endDate) else null

            // Define a gap in milliseconds (2 days = 2 * 24 * 60 * 60 * 1000)
            val gapMillis = 2L * 24 * 60 * 60 * 1000

            rvList.filter { rv ->
                val dateValid = if (userSearchStart != null && userSearchEnd != null && rv.bookedDates.isNotEmpty()) {
                    val overlaps = rv.bookedDates.none { bookedDate ->
                        // Convert Firebase timestamp (bookedDate["startDate"]) to Date in UTC
                        val bookedStart = (bookedDate["startDate"] as? Timestamp)?.toDate() // Firebase stores in UTC
                        val bookedEnd = (bookedDate["endDate"] as? Timestamp)?.toDate() // Firebase stores in UTC

                        if (bookedStart != null && bookedEnd != null) {
                            // Convert bookedStart and bookedEnd from UTC to UTC+2 (Finnish time)
                            val timeZone = TimeZone.getTimeZone("GMT+02:00")
                            val calendarStart = Calendar.getInstance(timeZone).apply { time = bookedStart }
                            val calendarEnd = Calendar.getInstance(timeZone).apply { time = bookedEnd }

                            // Now you have the start and end dates in Finnish time (UTC+2)
                            val finnishStart = calendarStart.time
                            val finnishEnd = calendarEnd.time

                            // Check if the user search dates overlap with booked dates
                            val condition = userSearchEnd.time > finnishStart.time - gapMillis &&
                                    userSearchStart.time < finnishEnd.time + gapMillis

                            Log.d("FilterCheck", "Checking RV: ${rv.id}, Start: $finnishStart, End: $finnishEnd, Overlaps: $condition")

                            condition // should return a Boolean value here
                        } else {
                            false
                        }
                    }
                    Log.d("FilterCheck", "RV: ${rv.id} is valid: $overlaps")

                    overlaps // This should return a Boolean here

                } else {
                    true
                }

                // Ensure that `dateValid` is a Boolean (condition should be true or false)
                dateValid

            }
        } else {
            // If no search is performed, display all RVs for sale.
            rvList.filter { it.isForSale }
        }




        LazyColumn {
            items(filteredRVs2) { rv ->
                RVItem2(rv = rv,navController = navController)
                Log.d("RV", "isForSale: ${rv.isForSale}")

            }
        }
    }
}




@Composable
fun RVItem2(rv: RV, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate("detail/${rv.id}?sourcePage=rental") },
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(), // Ensure column fills width
            horizontalAlignment = Alignment.CenterHorizontally // Center children horizontally
        ) {
            rv.imageUrl?.let { imagePath ->
                Image(
                    painter = rememberImagePainter(data = rv.imageUrl),
                    contentDescription = rv.name ?: "Unknown Title",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = rv.name ?: "Unknown Title",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = rv.description,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                fontSize = 14.sp,
                maxLines = 3,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}