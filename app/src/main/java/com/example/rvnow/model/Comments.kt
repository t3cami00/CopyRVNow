package com.example.rvnow.model
import com.google.firebase.Timestamp
data class Comment(
    val id: String = "",
    val userId: String = "",
    val text:String = "",
//    val userName: String = "",
//    val userProfilePictureUrl: String? = null,
//    val content: String = "",
//    val rating: Float? = null,
    val createdat: Timestamp = Timestamp.now()
)