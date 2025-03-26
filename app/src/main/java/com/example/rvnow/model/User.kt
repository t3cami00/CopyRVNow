package com.example.rvnow.model
import com.google.firebase.firestore.PropertyName
import com.google.firebase.Timestamp



data class User(
    val id: String = "",
    val email: String = "",
    val fullName: String = "",
    val passwordHash: String = "",
    val role: UserRole = UserRole.Customer,
    val profilePictureUrl: String? = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQSdz0utA21KB1xCbObh4vipxTQ8Bw9XMRoyyU8HTWeukL-Cic4PUjf3gE&usqp=CAE&s",
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now(),
    val status: String = "Active"
)

enum class UserRole {
    Admin,
    Owner,
    Customer
}