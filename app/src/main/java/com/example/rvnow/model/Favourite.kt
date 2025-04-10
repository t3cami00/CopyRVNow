package com.example.rvnow.model

import com.google.firebase.Timestamp

data class Favourite(
    val rvId: String = "",
    val name: String = "",
    val imageUrl:String = "",
    val isForRental: Boolean = false,
    val isForSale: Boolean = false,
    val createdat: Timestamp = Timestamp.now()
)