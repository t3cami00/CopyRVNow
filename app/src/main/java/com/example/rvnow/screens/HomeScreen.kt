package com.example.rvnow

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
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
            horizontalPadding = horizontalPadding
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
            horizontalPadding = horizontalPadding
        )

        // 添加底部间距，使滚动时底部有足够空间
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

            // First row
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

            // Second row
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
    horizontalPadding: Dp
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
                    horizontalPadding = horizontalPadding
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
    horizontalPadding: Dp
) {
    var currentImageIndex by remember { mutableStateOf(0) }
    val allImages = listOfNotNull(rv.imageUrl) + (rv.additionalImages ?: emptyList())
    val visibleImages = allImages.take(6)
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    // 根据屏幕宽度和水平内边距调整卡片宽度
    val cardWidth = (screenWidth - horizontalPadding * 2 - CARD_SPACING) * 0.8f

    Card(
        modifier = Modifier
            .width(cardWidth)
            .clickable { navController.navigate("detail/${rv.id}") },
        shape = RoundedCornerShape(CARD_CORNER_RADIUS)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(192.dp)
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = CARD_CONTENT_PADDING, vertical = CARD_CONTENT_PADDING)
                    .height(56.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = features,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
