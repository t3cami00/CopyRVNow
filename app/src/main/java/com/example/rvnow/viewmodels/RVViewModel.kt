package com.example.rvnow.viewmodels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rvnow.api.RVInformation
import com.example.rvnow.model.RV
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rvnow.model.Comment
import kotlinx.coroutines.Job
//import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rvnow.model.CartItem
import com.example.rvnow.model.Rating
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RVViewModel : ViewModel() {
    private val rvApiService = RVInformation()
    private val _rvs = MutableStateFlow<List<RV>>(emptyList())
    val rvs: StateFlow<List<RV>> = _rvs

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _commentStatus = MutableStateFlow<String?>(null)
    val commentStatus: StateFlow<String?> = _commentStatus

    private var commentListenerJob: Job? = null
    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments

    private var ratingListenerJob: Job? = null
    private val _ratings = MutableStateFlow<List<Comment>>(emptyList())
    val ratings: StateFlow<List<Comment>> = _ratings

    private val _averageRating = MutableLiveData<Float>()
    val averageRating: LiveData<Float> = _averageRating

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    //    private val _averageRating = MutableLiveData<Float>()
//    val averageRating: LiveData<Float> = _averageRating
    // 本地收藏状态管理
    private val _favorites = mutableStateMapOf<String, Boolean>()

    init {
        fetchRVs()
    }

    fun fetchRVs() {
        viewModelScope.launch {
            _loading.value = true
            try {
                var fetchedRVs = rvApiService.fetchAllRVs()

                // 添加硬编码评分和收藏状态
                fetchedRVs = fetchedRVs.map { rv ->
                    rv.copy(
                        averageRating = when (rv.id) {
                            "1" -> 4.5
                            "2" -> 3.8
                            else -> 3.5
                        },
                        isFavorite = _favorites[rv.id] ?: false
                    )
                }

                _rvs.value = fetchedRVs
            } catch (e: Exception) {
                _error.value = "Failed to fetch RVs: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }


    fun addComment(rvId: String, comment: Comment, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                rvApiService.addComment(rvId, comment)
                _commentStatus.value = "Comment submitted successfully"
                loadComments(rvId)
                onComplete()
            } catch (e: Exception) {
                _commentStatus.value = "Failed to submit comment: ${e.message}"
            }
        }
    }

    fun addRating(rvId: String, rating: Rating, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                rvApiService.addRating(rvId, rating)
                _commentStatus.value = "Rating submitted successfully"
//                loadCRatings(rvId)
                onComplete()
            } catch (e: Exception) {
                _commentStatus.value = "Failed to submit rating: ${e.message}"
            }
        }
    }

//    fun addFavourite(rvId: String, rating: Rating, onComplete: () -> Unit = {}) {
//        viewModelScope.launch {
//            try {
//                rvApiService.addFavourite(rvId, rating)
//                _commentStatus.value = "Favourite submitted successfully"
////                loadCRatings(rvId)
//                onComplete()
//            } catch (e: Exception) {
//                _commentStatus.value = "Failed to submit addFavourite: ${e.message}"
//            }
//        }
//    }


    fun loadAverageRating(rvId: String) {
        viewModelScope.launch {
            try {
                rvApiService.getAverageRating(rvId) { averageRating ->
                    _averageRating.value = averageRating
                }
            }catch (e: Exception) {
                // Handle the exception (e.g., log it, show a user-friendly message)
            }
        }
    }


    fun loadComments(rvId: String, onComplete: () -> Unit = {}) {
        commentListenerJob?.cancel() // Cancel previous listener if any

        commentListenerJob = viewModelScope.launch {
            Log.d("RVViewModel", "Loading comments for RV ID: $rvId")

            // Call the fetchComments API and handle the result via the callback
            rvApiService.fetchComments(rvId) { commentList ->
                Log.d("RVViewModel", "Comments loaded: $commentList")
                _comments.value = commentList
                onComplete() // Invoke the callback after loading comments
            }
        }
    }

    // Add these functions
//    fun addToFavorites(userId: String, rvId: String, onComplete: (Boolean) -> Unit = { _ -> }) {
//        viewModelScope.launch {
//            try {
//                val success = rvApiService.addToFavorites(userId, rvId)
//                if (success) {
//                    _favorites[rvId] = true
//                    updateLocalFavoriteStatus(rvId)
//                }
//                onComplete(success)
//            } catch (e: Exception) {
//                onComplete(false)
//            }
//        }
//    }
//
//    fun removeFromFavorites(userId: String, rvId: String, onComplete: (Boolean) -> Unit = { _ -> }) {
//        viewModelScope.launch {
//            try {
//                val success = rvApiService.removeFromFavorites(userId, rvId)
//                if (success) {
//                    _favorites[rvId] = false
//                    updateLocalFavoriteStatus(rvId)
//                }
//                onComplete(success)
//            } catch (e: Exception) {
//                onComplete(false)
//            }
//        }
//    }

    fun checkFavoriteStatus(userId: String, rvId: String, onComplete: (Boolean) -> Unit = { _ -> }) {
        viewModelScope.launch {
            try {
                val isFavorite = rvApiService.checkIfFavorite(userId, rvId)
                _favorites[rvId] = isFavorite
                updateLocalFavoriteStatus(rvId)
                onComplete(isFavorite)
            } catch (e: Exception) {
                onComplete(false)
            }
        }
    }
    // In RVViewModel
    fun toggleFavorite(userId: String, rvId: String, onComplete: (Boolean) -> Unit = { _ -> }) {
        val currentlyFavorite = _favorites[rvId] ?: false
        // Optimistic update - change UI immediately
        _favorites[rvId] = !currentlyFavorite
        updateLocalFavoriteStatus(rvId)

        viewModelScope.launch {
            try {
                val success = if (currentlyFavorite) {
                    rvApiService.removeFromFavorites(userId, rvId)
                } else {
                    rvApiService.addToFavorites(userId, rvId)
                }

                if (success) {
                    _favorites[rvId] = !currentlyFavorite
                    updateLocalFavoriteStatus(rvId)
                }
                onComplete(success)
            } catch (e: Exception) {
                onComplete(false)
            }
        }
    }

    // In RVViewModel


    fun addToCart(userId: String, rv: RV, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val cartItemData = mapOf(
                    "rvId" to rv.id,
                    "name" to rv.name,
                    "imageUrl" to rv.imageUrl,
                    "pricePerDay" to rv.pricePerDay,
                    "quantity" to 1
                )

                val success = rvApiService.addToCart(userId, rv.id, cartItemData)
                if (success) {
                    // Update local state
                    _cartItems.value += CartItem(
                        rvId = rv.id,
                        name = rv.name,
                        imageUrl = rv.imageUrl,
                        pricePerDay = rv.pricePerDay
                    )
                }
                callback(success)
            } catch (e: Exception) {
                // Call the callback with false if there's an error
                callback(false)
            }
        }
    }

    // 收藏/取消收藏
//    fun toggleFavorite(rvId: String) {
//        _favorites[rvId] = !(_favorites[rvId] ?: false)
//        updateLocalFavoriteStatus(rvId)
//    }

    private fun updateLocalFavoriteStatus(rvId: String) {
        _rvs.value = _rvs.value.map { rv ->
            if (rv.id == rvId) rv.copy(isFavorite = _favorites[rv.id] ?: false) else rv
        }
    }

    // 保持原有方法不变
    fun fetchLastRVId() {
        viewModelScope.launch {
            try {
                val lastId = rvApiService.fetchLastRVId()
                println("DEBUG: Last RV ID = $lastId")
            } catch (e: Exception) {
                println("DEBUG: Error fetching last RV ID - ${e.message}")
            }
        }
    }


    fun addRV(rv: RV) {
        viewModelScope.launch {
            _loading.value = true
            try {
                rvApiService.addAllRV(rv)  // Make sure this function only accepts a single RV
                fetchRVs()  // Refresh the list after adding
//                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                _error.value = "Failed to add RV: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}
