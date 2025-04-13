package com.example.rvnow.model

data class RVTravelGuide(
    val id: String = "",
    val title: String = "",
    val summary: String = "",
    val content: String = "",
    val imageUrl: String = "",
    val date: String = "",
    val author: String = "",
    val tags: List<String> = listOf()
)
