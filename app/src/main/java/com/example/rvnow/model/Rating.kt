package com.example.rvnow.model

import com.google.firebase.Timestamp

data class Rating(

    val userId: String = "",
    val rating: Float = 0f,
    val createdat: Timestamp = Timestamp.now()
)