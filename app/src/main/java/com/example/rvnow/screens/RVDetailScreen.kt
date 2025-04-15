// Add a new DetailScreen composable for displaying detailed information of the RV
package com.example.rvnow

import android.util.Log
import android.widget.RatingBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.rvnow.model.RV
import com.example.rvnow.viewmodels.RVViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.*

import androidx.compose.material3.Icon

import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup

import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.livedata.observeAsState

import androidx.compose.runtime.setValue

import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import com.example.rvnow.model.Comment
import com.example.rvnow.model.Rating

import java.util.*
import com.example.rvnow.viewmodels.AuthViewModel
import androidx.compose.foundation.layout.Column as Column
import android.widget.Toast
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FlipToBack
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.ui.platform.LocalContext



@Composable
fun StarRatingBar(
    rating: Float,
    averageRating: Float,
    onRatingChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
    starCount: Int = 1
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        // Display stars
        Row {
            for (i in 1..starCount) {
                val starValue = i.toFloat()
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Star",
                    tint = if (rating >= starValue) Color.Yellow
                    else if (averageRating >= starValue) Color.Yellow.copy(alpha = 0.1f)
                    else Color.Gray,
                    modifier = Modifier
                        .size(25.dp)

                )
            }
        }

        // Display average rating text
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "%.1f/5".format(averageRating),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun StarRatingBar1(
    maxStars: Int = 5,
    rating: Float,
    onRatingChanged: (Float) -> Unit
) {
    val density = LocalDensity.current.density
    val starSize = (12f * density).dp
    val starSpacing = (0.5f * density).dp

    Row(
        modifier = Modifier.selectableGroup(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxStars) {
            val isSelected = i <= rating
            val icon = if (isSelected) Icons.Filled.Star else Icons.Default.Star
            val iconTintColor = if (isSelected) Color(0xFFFFC700) else Color(0x20FFFFFF)
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTintColor,
                modifier = Modifier
                    .selectable(
                        selected = isSelected,
                        onClick = { onRatingChanged(i.toFloat()) }
                    )
                    .width(starSize)
                    .height(starSize)
            )
            Spacer(modifier = Modifier.width(starSpacing))
        }
    }
}



@Composable
fun RVDetailScreen(
    rvId: String,
    rvViewModel: RVViewModel,
    authViewModel: AuthViewModel,
    navController: NavController,
    sourcePage: String
) {
    // Collect the list of RVs from the ViewModel
    val rvList by rvViewModel.rvs.collectAsState()
    // Find the RV that matches the provided rvId
    val rv = rvList.firstOrNull { it.id == rvId }
    val name = rv?.name ?: ""

    val isForRental = rv?.isForRental ?: false
    val imageUrl = rv?.imageUrl ?: ""

    val isForSale = rv?.isForSale ?: false

    val comments by rvViewModel.comments.collectAsState(emptyList())
    val ratings by rvViewModel.ratings.collectAsState(emptyList())
    val context = LocalContext.current
    val image1 = rememberAsyncImagePainter("file:///android_asset/images/11.png")

    // State for rating, favorite status, comment text, etc.
    var rating by remember { mutableStateOf(8.5f) }
    var isFavorite by remember { mutableStateOf(false) }
    var commentText by remember { mutableStateOf("") }
    val isLoggedIn by authViewModel.isLoggedIn.observeAsState(initial = false)
    var showWarningDialog by remember { mutableStateOf(false) }
    val currentUser by authViewModel.userInfo.observeAsState()
    var isProcessingFavorite by remember { mutableStateOf(false) }
    var isAddingToCart by remember { mutableStateOf(false) }
    var isAddedToCart by remember { mutableStateOf(false) }
//    val averageRating by rvViewModel.averageRating.collectAsState()
    val averageRating = rvViewModel.averageRatings.collectAsState().value[rvId] ?: 0f

//    val currentUser by authViewModel.userInfo.observeAsState()
//    var isFavorite by remember { mutableStateOf(false) }

    // Check favorite status when screen loads or user changes
    LaunchedEffect(rvId, currentUser?.id) {
        currentUser?.id?.let { userId ->
            rvViewModel.checkFavoriteStatus(userId, rvId) { isFav ->
                isFavorite = isFav
            }
        } ?: run {
            // User not logged in, set to false
            isFavorite = false
        }
    }

    LaunchedEffect(rvId) {
        rvViewModel.loadAverageRating(rvId)
        Log.d("AverageRating", "Loading Average Rating for RV: ${rvId}")
    }

    LaunchedEffect(rvId) {
        rvViewModel.loadComments(rvId, onComplete = {})
//        rvViewModel.loadAverageRating(rvId)
        Log.d("AverageRating", "Loading Average Rating for RV: ${rvId}")
    }

//    LaunchedEffect(rv.id) {
//        rvViewModel.loadAverageRating(rv.id)
//        Log.d("AverageRating", "Loading Average Rating for RV: ${rv.id}")
//    }


    // UI content
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(bottom = 16.dp)
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


        Spacer(modifier = Modifier.height(8.dp))
//for navigate back to homepage
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = {
                    // Go back to the Home screen
                    navController.navigate(sourcePage) {
                        // Optional: clear backstack to avoid going back again
                        popUpTo(sourcePage) { inclusive = true }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back to Home",
                    modifier = Modifier.size(44.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.padding(10.dp)) {


            rv?.let {
                item {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Box to hold the image, rating, and add to cart button
                        Text(
                            text = it.name,
                            fontSize = 26.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(Alignment.CenterVertically),
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
//                                .padding(16.dp)
                                .background(color = Color.LightGray)
//                                .RoundedCornerShape(19.dp)
//                                .border(
//                                    1.dp,
//                                    Color.Gray,
//                                    RoundedCornerShape(19.dp)
//                                ) // Border to visually separate the content
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                // Product Image
                                Image(
                                    painter = rememberAsyncImagePainter(model = it.imageUrl),
                                    contentDescription = it.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(300.dp),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.height(12.dp))



                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    // Display stars and average rating
                                    StarRatingBar(
                                        rating = rating,
                                        averageRating = averageRating, // Show average behind the stars
                                        onRatingChanged = { newRating ->
                                            rating = newRating
                                        }
                                    )
                                }


                                Spacer(modifier = Modifier.height(8.dp))
                                // Row for rating and add to cart button
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,

                                    ) {


//for the favourites


                                    IconButton(
                                        onClick = {
                                            if (isProcessingFavorite) return@IconButton
                                            // Optimistic UI update
                                            isFavorite = !isFavorite

                                            currentUser?.id?.let { userId ->
                                                isProcessingFavorite = true
                                                rvViewModel.toggleFavorite(
                                                    userId = userId,
                                                    rvId = rvId,
                                                    name = name,
                                                    isForRental = isForRental,
                                                    imageUrl = imageUrl,
                                                    isForSale = isForSale
                                                ) { success ->
                                                    isProcessingFavorite = false
                                                    if (success) {
                                                        // Success - state is already updated
                                                        Toast.makeText(
                                                            context,
                                                            if (isFavorite) "Added to favorites" else "Removed from favorite",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    } else {
                                                        // Show error and revert UI state
                                                        isFavorite = !isFavorite
                                                        Toast.makeText(
                                                            context,
                                                            "Failed to update favorites",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                            } ?: run {
                                                showWarningDialog = true
                                            }
                                        },
                                        enabled = !isProcessingFavorite
                                    ) {
                                        if (isProcessingFavorite) {
                                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                        } else {
                                            Icon(
                                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                                contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                                                tint = if (isFavorite) Color.Red else Color.Gray,
                                                modifier = Modifier.size(44.dp)
                                            )
                                        }
                                    }



                                    IconButton(
                                        onClick = {
                                            if (isAddingToCart) return@IconButton

                                            currentUser?.id?.let { userId ->
                                                isAddingToCart = true
                                                rvViewModel.addToCart(userId, it) {
//                                                    success ->  // Using 'it' here
//                                                    isAddingToCart = false
                                                        success ->
                                                    isAddingToCart = false
                                                    if (success) {
                                                        isAddedToCart = true
                                                    }
                                                    Toast.makeText(
                                                        context,
                                                        if (success) "Added to cart"
                                                        else "Failed to add to cart",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            } ?: run { showWarningDialog = true }
                                        }
                                    ) {
                                        if (isAddingToCart) {
                                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                        } else {
                                            Icon(
                                                imageVector = Icons.Default.ShoppingCart,
                                                contentDescription = "Add to cart",
                                                modifier = Modifier.size(44.dp),
                                                tint = if (isAddedToCart) Color(0xFF4CAF50) else Color.Black
                                            )
                                        }
                                    }
                                }
                            }
                        }




                        // Product Description
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Description: ${it.description}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Departure: ${it.place}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Availability: ${it.status}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))
//                        Text(
//                            text = if (sourcePage == "rental") {
//                                "Price Per Day: \$${it.pricePerDay}"
//                            } else {
//                                "Sales Price: \$${it.price ?: "N/A"}"
//                            },
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Bold
//                        )
                        Text(
                            text = when (sourcePage.lowercase()) {
                                "rental" -> "Price Per Day: \$${it.pricePerDay}"
                                "sales" -> "Sales Price: \$${it.price ?: "N/A"}"
                                "home" -> {
                                    if (!it.isForSale && it.isPopular) {
                                        "Price Per Day: \$${it.pricePerDay}"
                                    } else if (it.isForSale && it.isPopular) {
                                        "Sales Price: \$${it.price }"
                                    } else {
                                        "no Price: N/A"
                                    }
                                }
                                else -> "Price: N/A"
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )


//                         it.isForSale && it.isPopular


                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Driver Licence Required: ${it.driverLicenceRequired}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Additional Images:",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(it.additionalImages) { imageUrl ->
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = "Image",
                                    modifier = Modifier
                                        .size(150.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Spacer(modifier = Modifier.height(16.dp))
//                        leave a rating:
                        // Comment Input
                        Text(
                            text = "Leave a rating:",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            // Display stars and average rating
                            StarRatingBar1(
                                rating = rating,
//                                averageRating = averageRating, // Show average behind the stars
                                onRatingChanged = { newRating ->
                                    rating = newRating
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))


                        Button(
                            onClick = {
                                if (isLoggedIn) {
                                    // Ensure the rating is within a valid range, e.g., 0.0 to 5.0
                                    if (rating in 0.0..5.0) {
                                        currentUser?.let { user ->
                                            val newRating = Rating(
                                                rating = rating,  // Use the rating value
                                                userId = user.id  // User's UID
                                            )
                                            // Submit the rating
                                            rvViewModel.addRating(rvId, newRating) {
                                                // Clear comment and reset rating after successful submission
                                                rating = 5f // Reset the rating to initial state

                                                // Show success message using Toast
                                                Toast.makeText(
                                                    context,
                                                    "Rating submitted successfully!",
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                                rvViewModel.loadAverageRating(rvId)

                                            }
                                        }
                                    } else {
                                        // Handle the case where the rating is out of the expected range
                                        showWarningDialog = true
                                    }
                                } else {
                                    showWarningDialog = true
                                }
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Submit Rating")
                        }


                        // Comment Input
                        Text(
                            text = "Leave a Comment:",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        OutlinedTextField(
                            value = commentText,
                            onValueChange = { commentText = it },
                            label = { Text("Write a comment...") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Submit Comment Button
                        Button(
                            onClick = {
                                if (isLoggedIn) {
                                    if (commentText.isNotBlank()) {
                                        currentUser?.let { user ->
                                            val newComment = Comment(
                                                text = commentText,
                                                userId = user.id,
                                                email = user.email ?: "unknown@example.com"
                                            )
                                            rvViewModel.addComment(rvId, newComment)
                                            commentText = ""
                                        }
                                    }
                                } else {
                                    showWarningDialog = true
                                }
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Submit Comment")
                        }

                        // Warning Dialog if the user is not logged in
                        if (!isLoggedIn && showWarningDialog) {
                            AlertDialog(
                                onDismissRequest = { showWarningDialog = false },
                                title = { Text("Not Logged In") },
                                text = { Text("You need to be logged in to submit a comment.") },
                                confirmButton = {
                                    Button(onClick = { showWarningDialog = false }) {
                                        Text("OK")
                                    }
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (comments.isNotEmpty()) {
                            Text(
                                text = "Comments:",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Column {
                                comments.forEach { comment ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(8.dp)) {
                                            Text(
                                                text = comment.text,
                                                fontSize = 14.sp
                                            )
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.End
                                            ) {
                                                Text(
                                                    text = comment.email ?: "",
                                                    fontSize = 12.sp,
                                                    color = Color.Gray
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            Text(
                                text = "RV comments not found.",
                                modifier = Modifier.fillMaxWidth(),
                                color = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}






