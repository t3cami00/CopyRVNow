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
import android.util.Log

class GoRVingViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val TAG = "GoRVingViewModel"

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
                Log.d(TAG, "Loaded ${destinations.size} destinations")
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load destinations: ${e.message}"
                Log.e(TAG, "Failed to load destinations", e)
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
                Log.d(TAG, "Loaded ${featuredDestinations.size} featured destinations")
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load featured destinations: ${e.message}"
                Log.e(TAG, "Failed to load featured destinations", e)
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
                Log.d(TAG, "Loaded ${guides.size} travel guides")
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load travel guides: ${e.message}"
                Log.e(TAG, "Failed to load travel guides", e)
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
                Log.d(TAG, "Loaded destination details for ID: $id")
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load destination details: ${e.message}"
                Log.e(TAG, "Failed to load destination details for ID: $id", e)
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
                Log.d(TAG, "Loaded travel guide details for ID: $id")
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load travel guide details: ${e.message}"
                Log.e(TAG, "Failed to load travel guide details for ID: $id", e)
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
                Log.d(TAG, "Loaded ${destinations.size} destinations for country: $country")
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load destinations for $country: ${e.message}"
                Log.e(TAG, "Failed to load destinations for country: $country", e)
                _countryDestinations.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 改进的搜索功能
    fun search(query: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                // 清空之前的搜索结果
                _searchResults.value = emptyList()

                val queryLowerCase = query.lowercase().trim()
                Log.d(TAG, "Searching for: '$queryLowerCase'")

                // 直接尝试使用Firestore查询
                try {
                    // 尝试按名称搜索目的地
                    val nameSnapshot = db.collection("rv_destinations")
                        .orderBy("name")
                        .startAt(queryLowerCase)
                        .endAt(queryLowerCase + "\uf8ff")
                        .get()
                        .await()

                    Log.d(TAG, "Name query returned ${nameSnapshot.size()} results")

                    // 尝试按国家搜索目的地
                    val countrySnapshot = db.collection("rv_destinations")
                        .orderBy("country")
                        .startAt(queryLowerCase)
                        .endAt(queryLowerCase + "\uf8ff")
                        .get()
                        .await()

                    Log.d(TAG, "Country query returned ${countrySnapshot.size()} results")

                    // 尝试按位置搜索目的地
                    val locationSnapshot = db.collection("rv_destinations")
                        .orderBy("location")
                        .startAt(queryLowerCase)
                        .endAt(queryLowerCase + "\uf8ff")
                        .get()
                        .await()

                    Log.d(TAG, "Location query returned ${locationSnapshot.size()} results")

                    // 合并结果
                    val directResults = mutableListOf<RVDestination>()

                    nameSnapshot.documents.forEach { doc ->
                        val destination = doc.toObject(RVDestination::class.java)
                        if (destination != null) {
                            directResults.add(destination.copy(id = doc.id))
                        }
                    }

                    countrySnapshot.documents.forEach { doc ->
                        val destination = doc.toObject(RVDestination::class.java)
                        if (destination != null && !directResults.any { it.id == doc.id }) {
                            directResults.add(destination.copy(id = doc.id))
                        }
                    }

                    locationSnapshot.documents.forEach { doc ->
                        val destination = doc.toObject(RVDestination::class.java)
                        if (destination != null && !directResults.any { it.id == doc.id }) {
                            directResults.add(destination.copy(id = doc.id))
                        }
                    }

                    Log.d(TAG, "Direct Firestore queries found ${directResults.size} results")

                    if (directResults.isNotEmpty()) {
                        _searchResults.value = directResults
                        return@launch
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Direct Firestore queries failed, falling back to client-side filtering", e)
                }

                // 如果直接查询没有结果，回退到获取所有数据并在客户端过滤
                Log.d(TAG, "Falling back to client-side filtering")

                // 获取所有目的地
                val destinationsSnapshot = db.collection("rv_destinations").get().await()
                val allDestinations = destinationsSnapshot.documents.mapNotNull { doc ->
                    val destination = doc.toObject(RVDestination::class.java)
                    destination?.copy(id = doc.id)
                }

                Log.d(TAG, "Retrieved ${allDestinations.size} destinations for filtering")

                // 获取所有旅游攻略
                val guidesSnapshot = db.collection("rv_travel_guides").get().await()
                val allGuides = guidesSnapshot.documents.mapNotNull { doc ->
                    val guide = doc.toObject(RVTravelGuide::class.java)
                    guide?.copy(id = doc.id)
                }

                Log.d(TAG, "Retrieved ${allGuides.size} travel guides for filtering")

                val results = mutableListOf<Any>()

                // 过滤目的地 - 检查name、country、location和description字段
                val filteredDestinations = allDestinations.filter { destination ->
                    val nameMatch = destination.name.lowercase().contains(queryLowerCase)
                    val countryMatch = destination.country.lowercase().contains(queryLowerCase)
                    val locationMatch = destination.location.lowercase().contains(queryLowerCase)
                    val descriptionMatch = destination.description.lowercase().contains(queryLowerCase)

                    val matches = nameMatch || countryMatch || locationMatch || descriptionMatch
                    if (matches) {
                        Log.d(TAG, "Matched destination: ${destination.name} (name: $nameMatch, country: $countryMatch, location: $locationMatch, description: $descriptionMatch)")
                    }
                    matches
                }

                Log.d(TAG, "Filtered destinations: ${filteredDestinations.size}")
                results.addAll(filteredDestinations)

                // 过滤旅游攻略 - 检查title、summary、content和tags字段
                val filteredGuides = allGuides.filter { guide ->
                    val titleMatch = guide.title.lowercase().contains(queryLowerCase)
                    val summaryMatch = guide.summary.lowercase().contains(queryLowerCase)
                    val contentMatch = guide.content.lowercase().contains(queryLowerCase)
                    val tagsMatch = guide.tags.any { it.lowercase().contains(queryLowerCase) }

                    val matches = titleMatch || summaryMatch || contentMatch || tagsMatch
                    if (matches) {
                        Log.d(TAG, "Matched guide: ${guide.title} (title: $titleMatch, summary: $summaryMatch, content: $contentMatch, tags: $tagsMatch)")
                    }
                    matches
                }

                Log.d(TAG, "Filtered guides: ${filteredGuides.size}")
                results.addAll(filteredGuides)

                Log.d(TAG, "Total search results: ${results.size}")
                _searchResults.value = results
            } catch (e: Exception) {
                _error.value = "Search failed: ${e.message}"
                Log.e(TAG, "Search failed", e)
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 清除搜索结果
    fun clearSearch() {
        _searchResults.value = emptyList()
        _error.value = null
        Log.d(TAG, "Search results cleared")
    }
}
