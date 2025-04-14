package com.example.rvnow.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.rvnow.model.RVDestination
import com.example.rvnow.model.RVTravelGuide
import com.example.rvnow.viewmodels.GoRVingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsScreen(
    navController: NavController
) {
    val viewModel = viewModel<GoRVingViewModel>()

    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearSearch()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Results") },
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
                Text("Error: $error")
            }
        } else if (searchResults.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No results found")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(searchResults) { result ->
                    when (result) {
                        is RVDestination -> {
                            SearchDestinationItem(
                                destination = result,
                                onClick = {
                                    navController.navigate("destination_details/${result.id}")
                                }
                            )
                        }
                        is RVTravelGuide -> {
                            SearchTravelGuideItem(
                                travelGuide = result,
                                onClick = {
                                    navController.navigate("travel_guide_details/${result.id}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchDestinationItem(destination: RVDestination, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = destination.imageUrl,
                contentDescription = destination.name,
                modifier = Modifier
                    .width(120.dp)
                    .height(100.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = destination.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${destination.location}, ${destination.country}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Rating: ${destination.rating}",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
fun SearchTravelGuideItem(travelGuide: RVTravelGuide, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = travelGuide.imageUrl,
                contentDescription = travelGuide.title,
                modifier = Modifier
                    .width(120.dp)
                    .height(100.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = travelGuide.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = travelGuide.summary,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Published: ${travelGuide.date}",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
