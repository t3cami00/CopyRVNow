package com.example.rvnow

//import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.rvnow.model.RV

//import androidx.compose.ui.Alignment
//import androidx.compose.ui.graphics.Color


@Composable
fun HomeScreen(
    rvs: List<RV>,
    navController: NavController
) {
    var searchQuery by remember { mutableStateOf("") } // Search state
    var expanded by remember { mutableStateOf(false) } // Fix: Define expanded state

    val image1 = rememberImagePainter("file:///android_asset/images/11.jpeg")
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(bottom = 16.dp)
            .background(color = Color.White)
    ) {

        // Loading image from drawable or assets
        Image(
            painter = image1,
            contentDescription = "RV Image",
            modifier = Modifier.fillMaxSize()

        )

        Text(
            text = "Welcome to RVNow",
            color = Color.White,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )

//        Button(
//            onClick = {
//                navController.navigate("signup")
//            },
//            shape = RoundedCornerShape(16.dp),
//            modifier = Modifier
//                .align(Alignment.BottomCenter) // Positioning the button at the bottom center
//                .height(30.dp)
//                .padding(8.dp)
//        ) {
//            Icon(
//                imageVector = Icons.Default.Person,
//                contentDescription = "Car Icon",
//                tint = Color.White,
//                modifier = Modifier.height(20.dp).align(Alignment.CenterVertically)
//            )
//
//            Text(
//                text = "Singup",
//                color = Color.White,
//                fontSize = 18.sp,
//                fontWeight = FontWeight.Bold
//            )
//        }
    }




//        Box(modifier = Modifier.align(Alignment.TopEnd)) {
//            IconButton(onClick = { expanded = true }) {
//                Icon(Icons.Filled.Menu, contentDescription = "Menu") // Fix: Use Icons.Filled.Menu
//            }
//            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
//                DropdownMenuItem(text = { Text("Login") }, onClick = {
//                    navController.navigate("login")
//                    expanded = false
//                })
//                DropdownMenuItem(text = { Text("Signup") }, onClick = {
//                    navController.navigate("signup")
//                    expanded = false
//                })
//                DropdownMenuItem(text = { Text("Shopping Cart") }, onClick = {
//                    navController.navigate("cart")
//                    expanded = false
//                })
//            }
//
//
//
//        }
//    }

    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(120.dp))

        // Search Bar
//        OutlinedTextField(
//            value = searchQuery,
//            onValueChange = { searchQuery = it },
//            label = { Text("Search RVs") },
//            modifier = Modifier.fillMaxWidth(),
//            singleLine = true
//        )

        Spacer(modifier = Modifier.height(16.dp))

        // Filtered List of RVs
        val filteredRVs = rvs.filter {
            it.name.contains(searchQuery, ignoreCase = true)
        }




        LazyColumn(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.spacedBy(16.dp) // Adds spacing between items
        ) {

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            navController.navigate("rental")
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(120.dp)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = "Car Icon",
                            tint = Color.White,
                            modifier = Modifier.height(40.dp)
                        )
                        Text(
                            text = "Rent a RV",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = {
                            navController.navigate("sales")
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(120.dp)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = "Car Icon",
                            tint = Color.White,
                            modifier = Modifier.height(40.dp)
                        )
                        Text(
                            text = "Buy a RV",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }



                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            navController.navigate("owner")
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(120.dp)
                            .padding(8.dp)

                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Car Icon",
                            tint = Color.White,
                            modifier = Modifier.height(40.dp)
                        )

                        Text(
                            text = "RV Owner",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(

                        onClick = {
                            navController.navigate("travel")
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(120.dp)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = "Car Icon",
                            tint = Color.White,
                            modifier = Modifier.height(40.dp)
                        )

//                Spacer(modifier = Modifier.height(120.dp))/**/
//
                        Text(
                            text = "Travel",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "Popular Rental RVs",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }
            }

            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredRVs) { rv ->
                        RVItem1(rv = rv, navController = navController)
                    }
                }
            }


            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "Popular Rental RVs",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }
            }

            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredRVs) { rv ->
                        RVItem1(rv = rv, navController = navController)
                    }
                }
            }
        }


//        LazyColumn {
//
//        }
//        item{
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(100.dp)
//                    .padding(bottom = 16.dp)
////                .background(color = Color.Gray)
//            ) {
//                Text(
//                    text = "Popular Rental RVs",
//                    color = Color.Black,
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.align(Alignment.CenterStart)
//                )
//            }
//
//        },
//        item {
//            LazyRow(
//                modifier = Modifier.fillMaxWidth(), // Full width row
//                contentPadding = PaddingValues(horizontal = 8.dp), // Padding for better spacing
//                horizontalArrangement = Arrangement.spacedBy(8.dp) // Spacing between items
//            ) {
//                items(filteredRVs) { rv ->
//                    RVItem1(rv = rv, navController = navController)
//                }
//            }
//        }



    }
}

//fun item(function: () -> Unit) {
//
//}

@Composable
fun RVItem1(
    rv: RV,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .width(250.dp) // Fixed width for each item (adjust as needed)
            .padding(8.dp)
            .clickable {
                navController.navigate("detail/${rv.id}")
            },
        shape = RoundedCornerShape(8.dp),
//        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            rv.imageUrl?.let { imagePath ->
                Image(
                    painter = rememberImagePainter(data = rv.imageUrl),
                    contentDescription = rv.name ?: "Unknown Title",
                    modifier = Modifier
                        .width(220.dp) // Adjust width so it fits within the card
                        .height(150.dp) // Adjust height to match the card
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = rv.name ?: "Unknown Title",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                text = rv.description,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

//@Composable
//fun RVItem1(
//    rv: RV,
//    navController: NavController
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//            .clickable {
//                navController.navigate("detail/${rv.id}")
//            },
//        shape = RoundedCornerShape(8.dp)
//    ) {
//        Row {
//            rv.imageUrl?.let { imagePath ->
//                Image(
//                    painter = rememberImagePainter(data = rv.imageUrl),
//                    contentDescription = rv.name ?: "Unknown Title",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(200.dp),
//                    contentScale = ContentScale.Crop
//                )
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Text(
//                text = rv.name ?: "Unknown Title",
//                modifier = Modifier.padding(8.dp),
//                fontSize = 18.sp,
//                fontWeight = FontWeight.Bold
//            )
//
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(
//                text = rv.description,
//                modifier = Modifier.padding(8.dp),
//                fontSize = 14.sp,
//                maxLines = 3
//            )
//        }
//    }
//}

@Composable
fun PopularRVs(
    rv: RV,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate("detail/${rv.id}")
            },
        shape = RoundedCornerShape(8.dp)
    ) {
        Row {
            rv.imageUrl?.let { imagePath ->
                Image(
                    painter = rememberImagePainter(data = rv.imageUrl),
                    contentDescription = rv.name ?: "Unknown Title",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
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
        }
    }
}
