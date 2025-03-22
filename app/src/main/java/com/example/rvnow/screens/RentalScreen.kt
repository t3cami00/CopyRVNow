package com.example.rvnow

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.rvnow.model.RV
import java.util.Calendar

//import androidx.compose.ui.Alignment
@Composable
fun RentalScreen(
    rvs: List<RV>,
    navController: NavController
) {
    var drivingType by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    val context = LocalContext.current

    val calendar = Calendar.getInstance()

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

    Column(modifier = Modifier.padding(10.dp)) {

        // Start Date Field
        Row(modifier = Modifier.padding(10.dp)){
            Box(
                modifier = Modifier
                    .weight(1f)
//                    .width(250.dp)
//                    .fillMaxWidth()
                    .clickable { startDatePickerDialog.show() } // Apply clickable to the Box
            ) {
                OutlinedTextField(
                    value = startDate,
                    onValueChange = { },
                    label = { Text("Start Date") },
                    readOnly = true, // Prevent manual input
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            // End Date Field
            Box(
                modifier = Modifier.weight(1f)
//                .fillMaxWidth()
//                .clickable { endDatePickerDialog.show() }
            ) {
                OutlinedTextField(
                    value = endDate,
                    onValueChange = { },
                    label = { Text("End Date") },
                    readOnly = true, // Prevent manual input
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }
//        Box(
//            modifier = Modifier.width(250.dp)
//                .fillMaxWidth()
//                .clickable { startDatePickerDialog.show() } // Apply clickable to the Box
//        ) {
//            OutlinedTextField(
//                value = startDate,
//                onValueChange = { },
//                label = { Text("Start Date") },
//                readOnly = true, // Prevent manual input
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
//
//        Spacer(modifier = Modifier.height(10.dp))

        // End Date Field
//        Box(
//            modifier = Modifier.width(250.dp)
////                .fillMaxWidth()
////                .clickable { endDatePickerDialog.show() }
//        ) {
//            OutlinedTextField(
//                value = endDate,
//                onValueChange = { },
//                label = { Text("End Date") },
//                readOnly = true, // Prevent manual input
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
//
        Spacer(modifier = Modifier.height(10.dp))

        Row (){
            Box(
                modifier = Modifier
//                    .fillMaxWidth()
                    .weight(1f)
//                    .height(45.dp)
//                .clickable { startDatePickerDialog.show() } // Apply clickable to the Box
            ) {
                OutlinedTextField(
                    value = startDate,
                    onValueChange = { },
                    label = { Text("Driving Type") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 2.sp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Box(
                modifier = Modifier
//                    .fillMaxWidth()
                    .weight(1f)
//                    .height(45.dp)
//                .clickable { startDatePickerDialog.show() } // Apply clickable to the Box
            ) {
                OutlinedTextField(
                    value = startDate,
                    onValueChange = { },
                    label = { Text("Place") },
                    readOnly = true, // Prevent manual input
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 2.sp)
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
                onClick = { /* Perform search action here */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Search")
            }
        }

        // Filtered List of RVs
        val filteredRVs = rvs.filter {
            it.name.contains(drivingType, ignoreCase = true)
        }

        LazyColumn {
            items(filteredRVs) { rv ->
                RVItem(rv = rv)
            }
        }
    }
}




@Composable
fun RVItem(rv: RV) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column {
            rv.imageUrl?.let { imagePath ->
                Image(
                    painter = rememberImagePainter(data = rv.imageUrl),
                    contentDescription = rv.name ?: "Unknown Title",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
//                Image(
//                    painter = rememberImagePainter(
//                        data = "./images/Pho Bo(Beef Pho)1.jpg",
//                    ),
//                    contentDescription = rv.title ?: movie.name,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(200.dp),
//                    contentScale = ContentScale.Crop
//                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = rv.name ?: "Unknown Title",
                modifier = Modifier.padding(8.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = rv.description,
                modifier = Modifier.padding(8.dp),
                fontSize = 14.sp,
                maxLines = 3
            )

//            rv.release_date?.let { releaseDate ->
//                Spacer(modifier = Modifier.height(4.dp))
//                Text(
//                    text = "Release Date: $releaseDate",
//                    modifier = Modifier.padding(8.dp),
//                    fontSize = 12.sp,
//                    color = Color.Gray
//                )
//            }
        }
    }
}
