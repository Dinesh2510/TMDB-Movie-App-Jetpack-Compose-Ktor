/*
 * Copyright (c) 2026 Dinesh2510
 * File : AuthViewModel.kt
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

package com.app.movieapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.movieapp.data.local.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean> = userPreferences.isLoggedIn.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val isOnboardingCompleted: StateFlow<Boolean> = userPreferences.isOnboardingCompleted.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    fun completeOnboarding() {
        viewModelScope.launch {
            userPreferences.setOnboardingCompleted()
        }
    }

    fun register(name: String, email: String, pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            userPreferences.registerUser(name, email, pass)
            onSuccess()
        }
    }

    fun login(email: String, pass: String, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            val success = userPreferences.loginUser(email, pass)
            if (success) onSuccess() else onError()
        }
    }
    val userName: StateFlow<String> = userPreferences.userName.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "Alex"
    )
    fun logout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            userPreferences.logoutUser()
            onLogoutComplete()
        }
    }
}