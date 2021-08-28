package com.example.mealrecipees.koin

import android.util.Log
import com.example.mealrecipees.repository.Repository
import com.example.mealrecipees.viewModels.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { provideJson() }
    single { provideKtorClient(get()) }
    single { Repository(get(), FirebaseAuth.getInstance(), FirebaseFirestore.getInstance()) }
    viewModel { LoginViewModel(get()) }
}

fun provideKtorClient(json: Json) = HttpClient(CIO) {
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                Log.d("Ktor", "log -> $message")
            }
        }
    }
    install(JsonFeature) {
        serializer = KotlinxSerializer(json)
    }
}

fun provideJson() = Json {
    isLenient = true
    ignoreUnknownKeys = true
}
