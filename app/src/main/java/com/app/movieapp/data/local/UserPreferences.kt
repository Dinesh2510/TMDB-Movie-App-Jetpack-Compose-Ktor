/*
 * Copyright (c) 2026 Dinesh2510
 * File : UserPreferences.kt
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

package com.app.movieapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferences(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val IS_ONBOARDING_COMPLETED = booleanPreferencesKey("is_onboarding_completed")
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val REGISTERED_NAME = stringPreferencesKey("registered_name")
        private val REGISTERED_EMAIL = stringPreferencesKey("registered_email")
        private val REGISTERED_PASSWORD = stringPreferencesKey("registered_password")

        // ── Haptic Toggle Preference ──────────────────────────────
        private val IS_HAPTICS_ENABLED = booleanPreferencesKey("is_haptics_enabled")
    }

    val isOnboardingCompleted: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[IS_ONBOARDING_COMPLETED] ?: false
    }

    suspend fun setOnboardingCompleted() {
        dataStore.edit { prefs -> prefs[IS_ONBOARDING_COMPLETED] = true }
    }

    val isLoggedIn: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[IS_LOGGED_IN] ?: false
    }

    val userName: Flow<String> = dataStore.data.map { prefs ->
        prefs[REGISTERED_NAME] ?: ""
    }

    // ── Haptic Preference Flow & Setter (Enabled by default) ──
    val isHapticsEnabled: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[IS_HAPTICS_ENABLED] ?: true
    }

    suspend fun setHapticsEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[IS_HAPTICS_ENABLED] = enabled
        }
    }

    suspend fun registerUser(name: String, email: String, pass: String) {
        dataStore.edit { prefs ->
            prefs[REGISTERED_NAME] = name
            prefs[REGISTERED_EMAIL] = email
            prefs[REGISTERED_PASSWORD] = pass
            prefs[IS_LOGGED_IN] = true
        }
    }

    suspend fun loginUser(email: String, pass: String): Boolean {
        val prefs = dataStore.data.first()
        val savedEmail = prefs[REGISTERED_EMAIL] ?: ""
        val savedPass = prefs[REGISTERED_PASSWORD] ?: ""

        return if (email == savedEmail && pass == savedPass && email.isNotEmpty()) {
            dataStore.edit { it[IS_LOGGED_IN] = true }
            true
        } else {
            false
        }
    }

    suspend fun logoutUser() {
        dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = false
        }
    }
}