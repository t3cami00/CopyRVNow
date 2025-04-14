
package com.example.rvnow.viewmodels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore


class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _isLoggedIn = MutableLiveData(auth.currentUser != null)
    val isLoggedIn: LiveData<Boolean> get() = _isLoggedIn

    private val _userInfo = MutableLiveData<FirebaseUser?>()
    val userInfo: LiveData<FirebaseUser?> get() = _userInfo

    private val _fullName = MutableLiveData<String?>()
    val fullName: LiveData<String?> get() = _fullName

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _isLoggedIn.value = firebaseAuth.currentUser != null
            _userInfo.value = firebaseAuth.currentUser
            firebaseAuth.currentUser?.let { user ->
                // Fetch full name from Firestore or Realtime Database
                fetchFullName(user.uid)
            }
        }
    }

    private fun fetchFullName(uid: String) {

        firestore.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                val fullName = document.getString("fullName")
                _fullName.value = fullName
            }
            .addOnFailureListener {
                _fullName.value = null
            }
    }

    // Sign-out function
    fun logout() {
        FirebaseAuth.getInstance().signOut()
        _isLoggedIn.value = false // Update the login status
    }
}


