package com.example.rvnow.viewmodels

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

class RVViewModel : ViewModel() {
    private val rvApiService = RVInformation()
    private val _rvs = MutableStateFlow<List<RV>>(emptyList())
    val rvs: StateFlow<List<RV>> = _rvs

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

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

    // 收藏/取消收藏
    fun toggleFavorite(rvId: String) {
        _favorites[rvId] = !(_favorites[rvId] ?: false)
        updateLocalFavoriteStatus(rvId)
    }

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
