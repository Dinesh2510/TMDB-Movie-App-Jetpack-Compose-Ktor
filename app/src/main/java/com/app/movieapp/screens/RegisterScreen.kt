/*
 * Copyright (c) 2026 Dinesh2510
 * File : RegisterScreen.kt
 * Project : TMDB Ktor
 * Module : TMDB_Ktor.app.main
 * Created on : 2026-08-22 15:27
 * Last modified: 2026-08-24 23:10
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

package com.app.movieapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.movieapp.data.viewmodel.AuthViewModel
import com.app.movieapp.screens.components.CinematicTextField
import com.app.movieapp.ui.theme.TmdbCinematicTheme
import com.app.movieapp.utlis.AppHaptic
import com.app.movieapp.utlis.rememberHapticController
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = koinViewModel(),
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit
) {
    val hapticController = rememberHapticController()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(TmdbCinematicTheme.AppBackgroundGradient)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Create Account",
                color = TmdbCinematicTheme.TextPrimary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Sign up to start streaming your favorite movies.",
                color = TmdbCinematicTheme.TextSecondary,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            CinematicTextField(
                value = name,
                onValueChange = {
                    name = it
                    errorMessage = null
                },
                label = "Full Name"
            )
            Spacer(modifier = Modifier.height(16.dp))
            CinematicTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = null
                },
                label = "Email Address"
            )
            Spacer(modifier = Modifier.height(16.dp))
            CinematicTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null
                },
                label = "Password",
                isPassword = true
            )

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = TmdbCinematicTheme.CoralAccent, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // SIGN UP BUTTON
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(TmdbCinematicTheme.PrimaryActionGradient)
                    .clickable {
                        hapticController.trigger(AppHaptic.Click)

                        if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                            viewModel.register(name, email, password) {
                                hapticController.trigger(AppHaptic.Confirm)
                                onRegisterSuccess()
                            }
                        } else {
                            hapticController.trigger(AppHaptic.Reject)
                            errorMessage = "Please fill in all fields."
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sign Up",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // BOTTOM LOGIN NAVIGATION
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    color = TmdbCinematicTheme.TextSecondary,
                    fontSize = 14.sp
                )
                Text(
                    text = "Login",
                    color = TmdbCinematicTheme.CoralAccent,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        hapticController.trigger(AppHaptic.Click)
                        onLoginClick()
                    }
                )
            }
        }
    }
}