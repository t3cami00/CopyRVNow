package com.example.rvnow

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.rvnow.model.RV
import com.example.rvnow.viewmodels.RVViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

private val SECTION_SPACING_LARGE = 32.dp
private val SECTION_SPACING = 24.dp
private val SECTION_SPACING_SMALL = 16.dp

private val BUTTONS_TO_RENTAL_SPACING_LARGE = 48.dp
private val BUTTONS_TO_RENTAL_SPACING = 40.dp
private val BUTTONS_TO_RENTAL_SPACING_SMALL = 32.dp

private val BETWEEN_RVS_SPACING_LARGE = 48.dp
private val BETWEEN_RVS_SPACING = 36.dp
private val BETWEEN_RVS_SPACING_SMALL = 24.dp

private val HORIZONTAL_PADDING_LARGE = 24.dp
private val HORIZONTAL_PADDING = 16.dp
private val HORIZONTAL_PADDING_SMALL = 12.dp
private val SECTION_TITLE_PADDING_START = 8.dp
private val SECTION_TITLE_PADDING_BOTTOM = 16.dp
private val CARD_SPACING = 12.dp
private val BUTTON_SPACING = 16.dp
private val BUTTON_SPACING_SMALL = 8.dp
private val BUTTON_HEIGHT = 48.dp
private val BUTTON_CORNER_RADIUS = 8.dp
private val CARD_CORNER_RADIUS = 12.dp
private val CARD_CONTENT_PADDING = 12.dp

@Composable
fun HomeScreen(
    rvViewModel: RVViewModel = viewModel(),
    navController: NavController
) {
    val rvList by rvViewModel.rvs.collectAsState()
    val heroImage = rememberAsyncImagePainter("file:///android_asset/images/11.jpeg")

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val isSmallScreen = screenWidth < 360.dp
    val isMediumScreen = screenWidth >= 360.dp && screenWidth < 600.dp
    val isLargeScreen = screenWidth >= 600.dp

    val horizontalPadding = when {
        isLargeScreen -> HORIZONTAL_PADDING_LARGE
        isSmallScreen -> HORIZONTAL_PADDING_SMALL
        else -> HORIZONTAL_PADDING
    }

    val sectionSpacing = when {
        isLargeScreen -> SECTION_SPACING_LARGE
        isSmallScreen -> SECTION_SPACING_SMALL
        else -> SECTION_SPACING
    }

    val buttonsToRentalSpacing = when {
        isLargeScreen -> BUTTONS_TO_RENTAL_SPACING_LARGE
        isSmallScreen -> BUTTONS_TO_RENTAL_SPACING_SMALL
        else -> BUTTONS_TO_RENTAL_SPACING
    }

    val betweenRVsSpacing = when {
        isLargeScreen -> BETWEEN_RVS_SPACING_LARGE
        isSmallScreen -> BETWEEN_RVS_SPACING_SMALL
        else -> BETWEEN_RVS_SPACING
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        HeroSection(heroImage = heroImage)

        Spacer(modifier = Modifier.height(sectionSpacing))

        ActionButtonsSection(
            navController = navController,
            horizontalPadding = horizontalPadding,
            isSmallScreen = isSmallScreen
        )

        Spacer(modifier = Modifier.height(buttonsToRentalSpacing))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = horizontalPadding)
                .background(Color.LightGray.copy(alpha = 0.5f))
        )

        Spacer(modifier = Modifier.height(sectionSpacing))

        PopularRVsSection(
            title = "Popular Rental RVs",
            rvs = rvList.filter { !it.isForSale && it.isPopular },
            navController = navController,
            horizontalPadding = horizontalPadding,
            viewModel = rvViewModel
        )

        Spacer(modifier = Modifier.height(betweenRVsSpacing))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = horizontalPadding)
                .background(Color.LightGray.copy(alpha = 0.3f))
        )

        Spacer(modifier = Modifier.height(sectionSpacing))

        PopularRVsSection(
            title = "Popular Listed RVs",
            rvs = rvList.filter { it.isForSale && it.isPopular },
            navController = navController,
            horizontalPadding = horizontalPadding,
            viewModel = rvViewModel
        )

        Spacer(modifier = Modifier.height(sectionSpacing))
    }
}

@Composable
private fun HeroSection(heroImage: Painter) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        Image(
            painter = heroImage,
            contentDescription = "RV with aurora background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = HORIZONTAL_PADDING + SECTION_TITLE_PADDING_START, bottom = 24.dp)
        ) {
            Text(
                text = "Journey boldly",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "feel at home wherever you go",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ActionButtonsSection(
    navController: NavController,
    horizontalPadding: Dp,
    isSmallScreen: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding)
    ) {
        val iconWidth = 24.dp
        val buttonSpacing = if (isSmallScreen) BUTTON_SPACING_SMALL else BUTTON_SPACING

        if (isSmallScreen) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CustomActionButton(
                    text = "Rent an RV",
                    icon = Icons.Default.DirectionsCar,
                    onClick = { navController.navigate("rental") },
                    color = Color(0xFF607D8B),
                    iconWidth = iconWidth,
                    isSmallScreen = true
                )

                CustomActionButton(
                    text = "Buy an RV",
                    icon = Icons.Default.ShoppingCart,
                    onClick = { navController.navigate("sales") },
                    color = Color(0xFF795548),
                    iconWidth = iconWidth,
                    isSmallScreen = true
                )

                CustomActionButton(
                    text = "RV Owner",
                    icon = Icons.Default.Key,
                    onClick = { navController.navigate("owner") },
                    color = Color(0xFF5D4037),
                    iconWidth = iconWidth,
                    isSmallScreen = true
                )

                CustomActionButton(
                    text = "Go RVing",
                    icon = Icons.Default.Public,
                    onClick = { /* Add travel navigation */ },
                    color = Color(0xFF455A64),
                    iconWidth = iconWidth,
                    isSmallScreen = true
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CustomActionButton(
                    text = "Rent an RV",
                    icon = Icons.Default.DirectionsCar,
                    onClick = { navController.navigate("rental") },
                    color = Color(0xFF607D8B),
                    iconWidth = iconWidth,
                    isSmallScreen = false,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(buttonSpacing))
                CustomActionButton(
                    text = "Buy an RV",
                    icon = Icons.Default.ShoppingCart,
                    onClick = { navController.navigate("sales") },
                    color = Color(0xFF795548),
                    iconWidth = iconWidth,
                    isSmallScreen = false,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(buttonSpacing))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CustomActionButton(
                    text = "RV Owner",
                    icon = Icons.Default.Key,
                    onClick = { navController.navigate("owner") },
                    color = Color(0xFF5D4037),
                    iconWidth = iconWidth,
                    isSmallScreen = false,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(buttonSpacing))
                CustomActionButton(
                    text = "Go RVing",
                    icon = Icons.Default.Public,
                    onClick = { /* Add travel navigation */ },
                    color = Color(0xFF455A64),
                    iconWidth = iconWidth,
                    isSmallScreen = false,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun CustomActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    color: Color,
    iconWidth: Dp,
    isSmallScreen: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(BUTTON_CORNER_RADIUS),
        colors = ButtonDefaults.buttonColors(
            containerColor = color.copy(alpha = 0.1f),
            contentColor = color
        ),
        modifier = modifier
            .height(BUTTON_HEIGHT)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (isSmallScreen) Arrangement.Start else Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.width(iconWidth)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = if (isSmallScreen) TextAlign.Start else TextAlign.Center
            )
        }
    }
}

@Composable
private fun PopularRVsSection(
    title: String,
    rvs: List<RV>,
    navController: NavController,
    horizontalPadding: Dp,
    viewModel: RVViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding)
    ) {
        Text(
            text = title,
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                start = SECTION_TITLE_PADDING_START,
                bottom = SECTION_TITLE_PADDING_BOTTOM
            )
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(CARD_SPACING),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(rvs) { rv ->
                RVCard(
                    rv = rv,
                    title = rv.name ?: if (title.contains("Rental")) "Luxury RV" else "Premium RV",
                    features = rv.description ?: if (title.contains("Rental"))
                        "Family | Amazing Features | Comfort..."
                    else "Spacious | Luxury | All Included",
                    navController = navController,
                    horizontalPadding = horizontalPadding,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
private fun RVCard(
    rv: RV,
    title: String,
    features: String,
    navController: NavController,
    horizontalPadding: Dp,
    viewModel: RVViewModel,
) {
    var currentImageIndex by remember { mutableStateOf(0) }
    val allImages = listOfNotNull(rv.imageUrl) + (rv.additionalImages ?: emptyList())
    val visibleImages = allImages.take(6)
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cardWidth = (screenWidth - horizontalPadding * 2 - CARD_SPACING) * 0.8f

    // Fixed height for the card
    val cardHeight = 280.dp
    // Fixed height for the image section
    val imageHeight = 192.dp
    // Fixed height for the text section (card height - image height - padding)
    val textSectionHeight = cardHeight - imageHeight - CARD_CONTENT_PADDING * 2

    Card(
        modifier = Modifier
            .width(cardWidth)
            .height(cardHeight)
            .clickable { navController.navigate("detail/${rv.id}") },
        shape = RoundedCornerShape(CARD_CORNER_RADIUS)
    ) {
        Column {
            // Image section with fixed height
            Box(
                modifier = Modifier
                    .height(imageHeight)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(
                        topStart = CARD_CORNER_RADIUS,
                        topEnd = CARD_CORNER_RADIUS,
                        bottomStart = CARD_CORNER_RADIUS / 1.5f,
                        bottomEnd = CARD_CORNER_RADIUS / 1.5f
                    ))
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = visibleImages.getOrNull(currentImageIndex)),
                    contentDescription = "RV Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                if (visibleImages.size > 1) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = {
                                currentImageIndex = (currentImageIndex - 1 + visibleImages.size) % visibleImages.size
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Previous",
                                tint = Color.White
                            )
                        }

                        IconButton(
                            onClick = {
                                currentImageIndex = (currentImageIndex + 1) % visibleImages.size
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Next",
                                tint = Color.White
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(visibleImages.size) { index ->
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(
                                        color = if (index == currentImageIndex) Color.White else Color.White.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(50)
                                    )
                                    .padding(1.dp)
                            )
                            if (index < visibleImages.size - 1) Spacer(modifier = Modifier.width(4.dp))
                        }
                    }
                }
            }

            // Text section with fixed height
            Column(
                modifier = Modifier
//                    .height(textSectionHeight)
                    .fillMaxWidth()
                    .padding(horizontal = CARD_CONTENT_PADDING, vertical = CARD_CONTENT_PADDING)
            ) {
                // Title row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 12.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "%.1f".format(rv.averageRating),
                            fontSize = 14.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }

                    IconButton(
                        onClick = { viewModel.toggleFavorite(rv.id) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (rv.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (rv.isFavorite) Color.Red else Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Description with exactly 2 lines
                Text(
                    text = features,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                // Spacer to push content up if there's extra space
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}