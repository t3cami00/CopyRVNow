package com.example.rvnow

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.rvnow.model.RV
import com.example.rvnow.model.RVType
import com.example.rvnow.viewmodels.AuthViewModel
import com.example.rvnow.viewmodels.RVViewModel
import com.google.firebase.auth.FirebaseUser
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.LaunchedEffect
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter

@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    rvViewModel: RVViewModel = viewModel()
) {
    val rvList by rvViewModel.rvs.collectAsState()
    val currentUser by authViewModel.userInfo.observeAsState()
    val userInfo by authViewModel.userInfo.observeAsState()
    val fullName by authViewModel.fullName.observeAsState()

    val favoriteRVIds by rvViewModel.favoriteRVIds.collectAsState()
    val favoriteRVs = rvList.filter { it.id in favoriteRVIds }

    LaunchedEffect(currentUser) {
        currentUser?.uid?.let {
            rvViewModel.loadFavoriteRVIds(it)
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // 优化后的登出按钮（图标式）
        IconButton(
            onClick = {
                authViewModel.logout()
                navController.navigate("Signin|up") {
                    popUpTo("Signin|up") { inclusive = true }
                }
            },
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 16.dp, end = 4.dp)
        ) {

            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = "Logout",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.size(24.dp))
        }

        // 用户信息区域
        Spacer(modifier = Modifier.height(32.dp))
        UserInfoSection(authViewModel, userInfo, fullName)

        Spacer(modifier = Modifier.height(32.dp))

        // 收藏和发布区域
        Column(verticalArrangement = Arrangement.spacedBy(36.dp)) {
            // 收藏车辆部分
            if (favoriteRVs.isNotEmpty()) {
                FavoriteSection(
                    title = "My Favorites",
                    items = favoriteRVs,
                    navController = navController
                )
                CustomDivider()
            }

            // 租赁收藏
            FavoriteSection(
                title = "Rental Favorites",
                items = rvList.filter { it.type == RVType.Rental },
                navController = navController
            )

            CustomDivider()

//            // 购买收藏
//            FavoriteSection(
//                title = "Purchase Favorites",
//                items = rvList.filter { it.type == RVType.Sales }.take(2),
//                navController = navController
//            )

            CustomDivider()

            // 已发布车辆
            PublishedSection(
                rvs = rvList.filter { it.ownerId == userInfo?.uid }.take(2),
                navController = navController
            )
        }
    }
}

@Composable
private fun UserInfoSection(
    authViewModel: AuthViewModel,
    userInfo: FirebaseUser?,
    fullName: String?
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            val imageUrl = userInfo?.photoUrl?.toString()

            if (!imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Profile picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(160.dp).clip(CircleShape)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Default profile icon",
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        // 用户信息
        userInfo?.let {
            Text(
                text = fullName ?: it.displayName ?: "No Name",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = it.email ?: "No Email",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        } ?: run {
            Text(
                text = "No user information available",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        // 编辑按钮
        Button(
            onClick = { /* 处理编辑操作 */ },
            modifier = Modifier
                .width(200.dp)
                .height(48.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text("Edit", fontSize = 16.sp)
        }
    }
}

@Composable
private fun FavoriteSection(
    title: String,
    items: List<RV>,
    navController: NavController
) {
    Column {
        // 标题行
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${items.size} Favorites",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 横向滚动列表
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(items) { rv ->
                FavoriteRVCard(rv = rv, onClick = { navController.navigate("detail/${rv.id}") })
            }
        }
    }
}

@Composable
private fun PublishedSection(
    rvs: List<RV>,
    navController: NavController
) {
    Column {
        // 标题行
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Published RVs",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${rvs.size} Published",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 横向滚动列表
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(rvs) { rv ->
                FavoriteRVCard(rv = rv, onClick = { navController.navigate("detail/${rv.id}") })
            }
        }
    }
}

@Composable
private fun FavoriteRVCard(rv: RV, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
            ) {
                Image(
                    painter = rememberAsyncImagePainter(rv.imageUrl),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(modifier = Modifier.padding(12.dp)) {
                // 优化后的名称和评分布局
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = rv.name,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "%.1f".format(rv.averageRating),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = rv.place,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun CustomDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth(0.9f)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}