

package com.example.rvnow.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rvnow.api.RVInformation
import com.example.rvnow.model.RV
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RVViewModel : ViewModel() {

    private val rvApiService = RVInformation()
    private val _rvs = MutableStateFlow<List<RV>>(emptyList())
    val rvs: StateFlow<List<RV>> = _rvs
    private val _loading = MutableStateFlow(false) // Loading state
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null) // Error state
    val error: StateFlow<String?> = _error
    init {
        fetchRVs()
    }

    fun fetchRVs() {
        viewModelScope.launch {
            val fetchedRVs = rvApiService.fetchAllRVs()
            println("DEBUG: Fetched RVs - $fetchedRVs")
            _rvs.value = fetchedRVs
        }
    }


    fun addRV(rv: RV) {
        viewModelScope.launch {
            _loading.value = true
            try {
                // Call addallRV and wait for all data to be added
                rvApiService.addallRV(rv)
                fetchRVs()  // Refresh the list after adding
            } catch (e: Exception) {
                _error.value = "Failed to add RV: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

}
