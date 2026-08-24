/*
 * Copyright (c) 2026 Dinesh2510
 * File : NetworkModule.kt
 * Project : TMDB Ktor
 * Module : TMDB_Ktor.app.main
 * Created on : 2026-08-22 15:27
 * Last modified: 2026-08-22 15:09
 *
 * Author : Dinesh
 * GitHub : https://github.com/Dinesh2510
 * YouTube : https://www.youtube.com/@pixeldesigndeveloper
 * Website : https://pixeldev.in
 *
 * Copyright (c) 2026 Dinesh. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 */

package com.app.movieapp.di

import android.util.Log
import com.app.movieapp.BuildConfig
import com.app.movieapp.data.remote.ApiService
import com.app.movieapp.utlis.Constants
import com.app.movieapp.utlis.Constants.Companion.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {

    // 1. Provide Ktor HttpClient
    single {
        HttpClient(OkHttp) {

            defaultRequest {
                url(BASE_URL)
                contentType(ContentType.Application.Json)
              //  accept(ContentType.Application.Json)
              //  header("Authorization", "Bearer ${Constants.API_KEY}")
            }
            expectSuccess = true
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

            // ---- Full logging: URL, method, headers, request + response body ----
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        // Logcat truncates lines >4000 chars, so chunk long bodies
                        val chunkSize = 4000
                        if (message.length > chunkSize) {
                            var index = 0
                            while (index < message.length) {
                                val end = (index + chunkSize).coerceAtMost(message.length)
                                Log.d("Ktor", message.substring(index, end))
                                index = end
                            }
                        } else {
                            Log.d("Ktor", message)
                        }
                    }
                }
                level = if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE
                sanitizeHeader { header -> header == "Authorization" }
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 40_000
                connectTimeoutMillis = 40_000
                socketTimeoutMillis = 40_000
            }

            // Ensures logging can see the fully-negotiated response body,
            // not a partially-consumed stream
            expectSuccess = false
        }
    }

    single { ApiService(client = get()) }
}