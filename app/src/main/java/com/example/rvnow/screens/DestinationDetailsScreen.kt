package com.example.rvnow.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.rvnow.viewmodels.GoRVingViewModel

@Composable
fun DestinationDetailsScreen(
    navController: NavController,
    destinationId: String
) {
    val viewModel = viewModel<GoRVingViewModel>()

    LaunchedEffect(destinationId) {
        viewModel.getDestinationById(destinationId)
    }

    val selectedDestination by viewModel.selectedDestination.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(selectedDestination?.name ?: "Destination Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (error != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Error: $error", color = Color.Red)
            }
        } else {
            selectedDestination?.let { destination ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Main image
                    item {
                        AsyncImage(
                            model = destination.imageUrl,
                            contentDescription = destination.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Basic information
                    item {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = destination.name,
                                style = MaterialTheme.typography.h5,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "${destination.location}, ${destination.country}",
                                style = MaterialTheme.typography.subtitle1
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Rating: ${destination.rating ?: "Not rated"}",
                                style = MaterialTheme.typography.body2
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Price Range: ${destination.priceRange ?: "Not specified"}",
                                style = MaterialTheme.typography.body2
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Description",
                                style = MaterialTheme.typography.h6
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = destination.description ?: "No description available",
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }

                    // Facilities section
                    item {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = "Facilities",
                                style = MaterialTheme.typography.h6
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            if (!destination.facilities.isNullOrEmpty()) {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(destination.facilities) { facility ->
                                        Surface(
                                            modifier = Modifier.padding(vertical = 4.dp),
                                            shape = RoundedCornerShape(16.dp),
                                            color = MaterialTheme.colors.primary.copy(alpha = 0.1f),
                                            elevation = 0.dp
                                        ) {
                                            Text(
                                                text = facility,
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                                style = MaterialTheme.typography.body2
                                            )
                                        }
                                    }
                                }
                            } else {
                                Text(
                                    text = "No facilities listed",
                                    style = MaterialTheme.typography.body2,
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    // Best time to visit
                    item {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Best Time to Visit",
                                style = MaterialTheme.typography.h6
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = destination.bestTimeToVisit ?: "No information available",
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }

                    // Parking spots
                    item {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Parking Spots",
                                style = MaterialTheme.typography.h6
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            if (!destination.parkingSpots.isNullOrEmpty()) {
                                destination.parkingSpots.forEach { spot ->
                                    Text(
                                        text = "• $spot",
                                        style = MaterialTheme.typography.body1,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }
                            } else {
                                Text(
                                    text = "No parking spots listed",
                                    style = MaterialTheme.typography.body1,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            } ?: run {
                // This handles the case where selectedDestination is null
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Destination information not available", color = Color.Gray)
                }
            }
        }
    }
}
