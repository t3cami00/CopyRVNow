// Add a new DetailScreen composable for displaying detailed information of the RV
package com.example.rvnow
//import androidx.compose.ui.Modifier.clip
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.rvnow.model.RV


@Composable
fun RVDetailScreen(rvId: Int, rvs: List<RV>, navController: NavController) {
    // Find the RV by id
    val rv = rvs.firstOrNull { it.id == rvId }

    rv?.let {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = it.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = rememberImagePainter(data = it.imageUrl),
                contentDescription = it.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Description: ${it.description}",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Price: \$${it.price}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    } ?: Text(text = "RV not found")
}