package com.example.rvnow.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rvnow.model.RVDestination
import com.example.rvnow.model.RVTravelGuide
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GoRVingViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    // 状态流
    private val _destinations = MutableStateFlow<List<RVDestination>>(emptyList())
    val destinations: StateFlow<List<RVDestination>> = _destinations

    private val _featuredDestinations = MutableStateFlow<List<RVDestination>>(emptyList())
    val featuredDestinations: StateFlow<List<RVDestination>> = _featuredDestinations

    private val _travelGuides = MutableStateFlow<List<RVTravelGuide>>(emptyList())
    val travelGuides: StateFlow<List<RVTravelGuide>> = _travelGuides

    private val _selectedDestination = MutableStateFlow<RVDestination?>(null)
    val selectedDestination: StateFlow<RVDestination?> = _selectedDestination

    private val _selectedTravelGuide = MutableStateFlow<RVTravelGuide?>(null)
    val selectedTravelGuide: StateFlow<RVTravelGuide?> = _selectedTravelGuide

    private val _searchResults = MutableStateFlow<List<Any>>(emptyList())
    val searchResults: StateFlow<List<Any>> = _searchResults

    private val _countryDestinations = MutableStateFlow<List<RVDestination>>(emptyList())
    val countryDestinations: StateFlow<List<RVDestination>> = _countryDestinations

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // 加载所有目的地
    fun loadDestinations() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val snapshot = db.collection("rv_destinations").get().await()
                val destinations = snapshot.documents.mapNotNull { doc ->
                    val destination = doc.toObject(RVDestination::class.java)
                    destination?.copy(id = doc.id)
                }
                _destinations.value = destinations
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load destinations: ${e.message}"
                _destinations.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 加载特色目的地
    fun loadFeaturedDestinations() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val snapshot = db.collection("rv_destinations")
                    .whereEqualTo("isFeatured", true)
                    .get()
                    .await()

                val featuredDestinations = snapshot.documents.mapNotNull { doc ->
                    val destination = doc.toObject(RVDestination::class.java)
                    destination?.copy(id = doc.id)
                }
                _featuredDestinations.value = featuredDestinations
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load featured destinations: ${e.message}"
                _featuredDestinations.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 加载旅游攻略
    fun loadTravelGuides() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val snapshot = db.collection("rv_travel_guides").get().await()
                val guides = snapshot.documents.mapNotNull { doc ->
                    val guide = doc.toObject(RVTravelGuide::class.java)
                    guide?.copy(id = doc.id)
                }
                _travelGuides.value = guides
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load travel guides: ${e.message}"
                _travelGuides.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 获取目的地详情
    fun getDestinationById(id: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val doc = db.collection("rv_destinations").document(id).get().await()
                val destination = doc.toObject(RVDestination::class.java)
                _selectedDestination.value = destination?.copy(id = doc.id)
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load destination details: ${e.message}"
                _selectedDestination.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 获取旅游攻略详情
    fun getTravelGuideById(id: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val doc = db.collection("rv_travel_guides").document(id).get().await()
                val guide = doc.toObject(RVTravelGuide::class.java)
                _selectedTravelGuide.value = guide?.copy(id = doc.id)
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load travel guide details: ${e.message}"
                _selectedTravelGuide.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 按国家获取目的地
    fun getDestinationsByCountry(country: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val snapshot = db.collection("rv_destinations")
                    .whereEqualTo("country", country)
                    .get()
                    .await()

                val destinations = snapshot.documents.mapNotNull { doc ->
                    val destination = doc.toObject(RVDestination::class.java)
                    destination?.copy(id = doc.id)
                }
                _countryDestinations.value = destinations
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load destinations for $country: ${e.message}"
                _countryDestinations.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 搜索功能
    fun search(query: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                // 搜索目的地
                val destinationsSnapshot = db.collection("rv_destinations")
                    .whereGreaterThanOrEqualTo("name", query)
                    .whereLessThanOrEqualTo("name", query + '\uf8ff')
                    .get()
                    .await()

                // 搜索旅游攻略
                val guidesSnapshot = db.collection("rv_travel_guides")
                    .whereGreaterThanOrEqualTo("title", query)
                    .whereLessThanOrEqualTo("title", query + '\uf8ff')
                    .get()
                    .await()

                val results = mutableListOf<Any>()

                // 添加目的地结果
                destinationsSnapshot.documents.forEach { doc ->
                    val destination = doc.toObject(RVDestination::class.java)
                    if (destination != null) {
                        results.add(destination.copy(id = doc.id))
                    }
                }

                // 添加旅游攻略结果
                guidesSnapshot.documents.forEach { doc ->
                    val guide = doc.toObject(RVTravelGuide::class.java)
                    if (guide != null) {
                        results.add(guide.copy(id = doc.id))
                    }
                }

                _searchResults.value = results
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Search failed: ${e.message}"
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 清除搜索结果
    fun clearSearch() {
        _searchResults.value = emptyList()
    }
}
