package com.app.movieapp.di

import com.app.movieapp.data.remote.ApiService
import com.app.movieapp.utlis.Constants.Companion.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {

    // 1. Provide Ktor HttpClient
    single {
        HttpClient(OkHttp) {
            // Base URL and default headers
            defaultRequest {
                url(BASE_URL)
                contentType(ContentType.Application.Json)
            }

            // JSON Content Negotiation (Replaces GsonConverterFactory)
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                        coerceInputValues = true
                    }
                )
            }

            // Logging (Replaces HttpLoggingInterceptor)
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.BODY
            }

            // Timeouts (Replaces OkHttpClient timeouts)
            install(HttpTimeout) {
                requestTimeoutMillis = 40_000
                connectTimeoutMillis = 40_000
                socketTimeoutMillis = 40_000
            }
        }
    }

    // 2. Provide ApiService (Ktor Api Service implementation)
    single { ApiService(client = get()) }
}