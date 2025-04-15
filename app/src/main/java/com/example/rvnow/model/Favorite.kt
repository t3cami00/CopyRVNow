package com.example.rvnow.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName



data class Favorite(
    val rvId: String = "",
    val name: String = "",
    val imageUrl: String = "",

    @get:PropertyName("isForRental") @set:PropertyName("isForRental")
    var isForRental: Boolean = false,

    @get:PropertyName("isForSale") @set:PropertyName("isForSale")
    var isForSale: Boolean = false,

    val createdat: Timestamp = Timestamp.now()
)
