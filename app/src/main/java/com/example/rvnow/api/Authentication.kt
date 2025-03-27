package com.example.rvnow.api

import com.google.firebase.auth.FirebaseAuth
import android.app.Application
import com.google.firebase.FirebaseApp

class Authentication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}

