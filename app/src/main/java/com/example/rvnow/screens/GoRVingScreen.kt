package com.example.rvnow.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.rvnow.model.RVDestination
import com.example.rvnow.model.RVTravelGuide
import com.example.rvnow.viewmodels.GoRVingViewModel
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults

// Define spacing constants consistent with HomeScreen
private val SECTION_SPACING = 32.dp
private val SECTION_SPACING_SMALL = 20.dp
private val HORIZONTAL_PADDING = 16.dp
private val CARD_CORNER_RADIUS = 12.dp
private val CARD_CONTENT_PADDING = 12.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoRVingScreen(
    navController: NavController,
    viewModel: GoRVingViewModel = viewModel()
) {
    // Define colors consistent with HomeScreen
    val primaryColor = Color(0xFFE27D5F)  // Terracotta orange
    val secondaryColor = Color(0xFF5D8AA8)  // Lake blue
    val tertiaryColor = Color(0xFF6B8E23)  // Moss green
    val neutralColor = Color(0xFFA78A7F)   // Light camel - used for featured destination background

    var searchQuery by remember { mutableStateOf("") }
    val destinations by viewModel.destinations.collectAsState()
    val featuredDestinations by viewModel.featuredDestinations.collectAsState()
    val travelGuides by viewModel.travelGuides.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDestinations()
        viewModel.loadFeaturedDestinations()
        viewModel.loadTravelGuides()
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            "Go RVing",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color.Black  // Changed to black as requested
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = Color.Black
                    )
                )

                // Added background banner for title as requested
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(primaryColor.copy(alpha = 0.2f))
                        .padding(vertical = 8.dp, horizontal = HORIZONTAL_PADDING)
                ) {
                    Text(
                        "Discover amazing RV destinations and travel guides",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }
            }
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = primaryColor)
            }
        } else if (error != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Error: $error",
                    color = Color.Red,
                    fontFamily = FontFamily.Default,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(bottom = SECTION_SPACING),
                verticalArrangement = Arrangement.spacedBy(SECTION_SPACING)
            ) {
                // Search bar
                item {
                    Spacer(modifier = Modifier.height(SECTION_SPACING_SMALL))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = HORIZONTAL_PADDING),
                        placeholder = { Text("Search destinations or travel guides", color = Color.Gray) },
                        trailingIcon = {
                            IconButton(onClick = {
                                if (searchQuery.isNotEmpty()) {
                                    viewModel.search(searchQuery)
                                    navController.navigate("search_results")
                                }
                            }) {
                                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Black)
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                if (searchQuery.isNotEmpty()) {
                                    viewModel.search(searchQuery)
                                    navController.navigate("search_results")
                                }
                            }
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(CARD_CORNER_RADIUS),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = primaryColor,
                            cursorColor = primaryColor,
                        )
                    )
                }

                // Featured destinations
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {
                        Text(
                            text = "Featured Destinations",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Default,
                            color = Color.Black,  // Changed to black as requested
                            modifier = Modifier.padding(horizontal = HORIZONTAL_PADDING)
                        )

                        Spacer(modifier = Modifier.height(SECTION_SPACING_SMALL))

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = HORIZONTAL_PADDING),
                            horizontalArrangement = Arrangement.spacedBy(SECTION_SPACING_SMALL)
                        ) {
                            items(featuredDestinations) { destination ->
                                FeaturedDestinationCard(
                                    destination = destination,
                                    onClick = {
                                        navController.navigate("destination_details/${destination.id}")
                                    },
                                    neutralColor = neutralColor
                                )
                            }
                        }
                    }
                }

                // Browse by country
                item {
                    Column {
                        Text(
                            text = "Browse by Country",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Default,
                            color = Color.Black,  // Changed to black as requested
                            modifier = Modifier.padding(horizontal = HORIZONTAL_PADDING)
                        )

                        Spacer(modifier = Modifier.height(SECTION_SPACING_SMALL))

                        // Get unique countries
                        val countries = destinations.map { it.country }.distinct()

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = HORIZONTAL_PADDING),
                            horizontalArrangement = Arrangement.spacedBy(SECTION_SPACING_SMALL)
                        ) {
                            items(countries) { country ->
                                CountryCard(
                                    country = country,
                                    onClick = {
                                        navController.navigate("country_destinations/$country")
                                    }
                                )
                            }
                        }
                    }
                }

                // Travel guides
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {
                        Text(
                            text = "Travel Guides",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Default,
                            color = Color.Black,  // Changed to black as requested
                            modifier = Modifier.padding(horizontal = HORIZONTAL_PADDING)
                        )

                        Spacer(modifier = Modifier.height(SECTION_SPACING_SMALL))

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = HORIZONTAL_PADDING),
                            horizontalArrangement = Arrangement.spacedBy(SECTION_SPACING_SMALL)
                        ) {
                            items(travelGuides) { guide ->
                                TravelGuideCard(
                                    travelGuide = guide,
                                    onClick = {
                                        navController.navigate("travel_guide_details/${guide.id}")
                                    }
                                )
                            }
                        }
                    }
                }

                // Popular destinations
                item {
                    Column {
                        Text(
                            text = "Popular Destinations",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Default,
                            color = Color.Black,  // Changed to black as requested
                            modifier = Modifier.padding(horizontal = HORIZONTAL_PADDING)
                        )

                        Spacer(modifier = Modifier.height(SECTION_SPACING_SMALL))

                        // Popular destinations list
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            destinations.take(10).forEach { destination ->
                                DestinationListItem(
                                    destination = destination,
                                    onClick = {
                                        navController.navigate("destination_details/${destination.id}")
                                    },
                                    modifier = Modifier.padding(
                                        horizontal = HORIZONTAL_PADDING
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FeaturedDestinationCard(
    destination: RVDestination,
    onClick: () -> Unit,
    neutralColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(280.dp)
            .height(200.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(CARD_CORNER_RADIUS),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box {
            AsyncImage(
                model = destination.imageUrl,
                contentDescription = destination.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Add semi-transparent overlay with 50% transparency as requested
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        neutralColor.copy(alpha = 0.5f)  // 50% transparency as requested
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(CARD_CONTENT_PADDING)
            ) {
                Text(
                    text = destination.name,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Default
                )

                Text(
                    text = "${destination.location}, ${destination.country}",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Default
                )
            }

            // Rating badge with standardized style
            if (destination.rating > 0) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.4f),  // 50% transparency as requested
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFC107),  // Yellow star
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${destination.rating}",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CountryCard(
    country: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Get flag image URL for the country
    val flagImageUrl = getFlagImageUrl(country)

    Card(
        modifier = modifier
            .width(160.dp)
            .height(100.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(CARD_CORNER_RADIUS),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(CARD_CORNER_RADIUS)
                )
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Flag image
                AsyncImage(
                    model = flagImageUrl,
                    contentDescription = "$country flag",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Country name
                Text(
                    text = country,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Default,
                    color = Color.Black,  // Changed to black as requested
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun TravelGuideCard(
    travelGuide: RVTravelGuide,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(280.dp)
            .height(180.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(CARD_CORNER_RADIUS),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                AsyncImage(
                    model = travelGuide.imageUrl,
                    contentDescription = travelGuide.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Add tag if available
                if (travelGuide.tags.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .background(
                                color = Color.Black.copy(alpha = 0.4f),  // 50% transparency as requested
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = travelGuide.tags.first(),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.padding(CARD_CONTENT_PADDING)
            ) {
                Text(
                    text = travelGuide.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Default,
                    color = Color.Black,  // Changed to black as requested
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "By RVNow | 2025-04-16",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Default,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun DestinationListItem(
    destination: RVDestination,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)  // Fixed height for all cards
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(CARD_CORNER_RADIUS),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = destination.imageUrl,
                contentDescription = destination.name,
                modifier = Modifier
                    .width(120.dp)
                    .fillMaxHeight()
                    .clip(
                        RoundedCornerShape(
                            topStart = CARD_CORNER_RADIUS,
                            bottomStart = CARD_CORNER_RADIUS
                        )
                    ),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(CARD_CONTENT_PADDING)
            ) {
                Text(
                    text = destination.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Default,
                    color = Color.Black  // Changed to black as requested
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${destination.location}, ${destination.country}",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Default,
                    color = Color.Gray
                )
            }

            // Standardized rating display with star icon
            if (destination.rating > 0) {
                Row(
                    modifier = Modifier
                        .padding(end = CARD_CONTENT_PADDING)
                        .background(
                            color = Color.Black.copy(alpha = 0.4f),  // 50% transparency as requested
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFC107),  // Yellow star
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${destination.rating}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// Helper function to get flag image URL for a country
fun getFlagImageUrl(country: String): String {
    // In a real app, you would use a proper flag image API or have these stored in your resources
    // For this example, we're using a public flag API
    return when (country) {
        "United States" -> "https://flagcdn.com/w320/us.png"
        "Canada" -> "https://flagcdn.com/w320/ca.png"
        "Australia" -> "https://flagcdn.com/w320/au.png"
        "New Zealand" -> "https://flagcdn.com/w320/nz.png"
        "United Kingdom" -> "https://flagcdn.com/w320/gb.png"
        "France" -> "https://flagcdn.com/w320/fr.png"
        "Germany" -> "https://flagcdn.com/w320/de.png"
        "Italy" -> "https://flagcdn.com/w320/it.png"
        "Spain" -> "https://flagcdn.com/w320/es.png"
        "Japan" -> "https://flagcdn.com/w320/jp.png"
        "China" -> "https://flagcdn.com/w320/cn.png"
        "Brazil" -> "https://flagcdn.com/w320/br.png"
        "Mexico" -> "https://flagcdn.com/w320/mx.png"
        "South Africa" -> "https://flagcdn.com/w320/za.png"
        else -> "https://flagcdn.com/w320/un.png"  // UN flag as default
    }
}
