package com.example.rvnow.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class RV(
    val id: String = "",
    @get:PropertyName("isForSale") val isForSale: Boolean = true,
    val ownerId: String = "",
    val name: String = "",
    val type: RVType = RVType.Sales,
    val description: String = "",
    val pricePerDay: Double = 0.0,
    val imageUrl: String = "",
    val place: String = "",
    val additionalImages: List<String> = listOf(),
    val insurance: Map<String, String> = emptyMap(),
    val driverLicenceRequired: String? = null,
    val kilometerLimitation: Int? = null,
//    val isForRental: Boolean = false,
    @get:PropertyName("isForRental") @set:PropertyName("isForRental")
    var isForRental: Boolean = false,

//    @get:PropertyName("isForSale") @set:PropertyName("isForSale")
//    var isForSale: Boolean = false,
    @get:PropertyName("isPopular") val isPopular: Boolean = true,
    val status: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val bookedDates: List<Map<String, Timestamp>> = listOf(),

    // 新增字段
    val averageRating: Double = 0.0,  // 硬编码评分，后续从API获取
    var isFavorite: Boolean = false   // 本地收藏状态
)



