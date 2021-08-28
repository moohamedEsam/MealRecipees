package com.example.mealrecipees.utils

import android.app.Application
import com.example.mealrecipees.koin.appModule
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ApplicationClass: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ApplicationClass)
            modules(listOf(appModule))
        }
    }

}