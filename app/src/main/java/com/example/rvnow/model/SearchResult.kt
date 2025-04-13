package com.example.rvnow.model

sealed class SearchResult {
    data class Destination(val destination: RVDestination) : SearchResult()
    data class TravelGuide(val guide: RVTravelGuide) : SearchResult()
}
