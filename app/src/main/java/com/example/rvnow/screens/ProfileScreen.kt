package com.example.rvnow
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.rvnow.model.RV

@Composable
fun ProfileScreen(
    rvs: List<RV>,  // Pass the RV list directly
    navController: NavController
) {
    var searchQuery by remember { mutableStateOf("") } // Search state

    Column(modifier = Modifier.padding(16.dp)) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search RVs in profile") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Filtered List of RVs
        val filteredRVs = rvs.filter {
            it.name.contains(searchQuery, ignoreCase = true)
        }

        LazyColumn {
            items(filteredRVs) { rv ->
                RVItem(rv = rv)
            }
        }
    }
}




@Composable
fun RVItem3(rv: RV) {
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
