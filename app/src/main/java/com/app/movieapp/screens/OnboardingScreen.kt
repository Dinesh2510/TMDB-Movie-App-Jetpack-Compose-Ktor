/*
 * Copyright (c) 2026 Dinesh2510
 * File : OnboardingScreen.kt
 * Project : TMDB Ktor
 * Module : TMDB_Ktor.app.main
 * Created on : 2026-08-22 15:27
 * Last modified: 2026-08-24 23:25
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

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.movieapp.R
import com.app.movieapp.data.viewmodel.AuthViewModel
import com.app.movieapp.ui.theme.TmdbCinematicTheme
import com.app.movieapp.utlis.AppHaptic
import com.app.movieapp.utlis.rememberHapticController
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardingScreen(
    viewModel: AuthViewModel = koinViewModel(),
    onCreateAccountClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    val hapticController = rememberHapticController()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TmdbCinematicTheme.AppBackgroundGradient)
            .statusBarsPadding()
            .navigationBarsPadding() // Prevents overlapping the system navigation bar
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // TOP IMAGE CARD
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(320.dp)
                    .shadow(
                        elevation = 28.dp,
                        shape = RoundedCornerShape(32.dp),
                        ambientColor = Color.Black.copy(alpha = 0.6f),
                        spotColor = Color.Black.copy(alpha = 0.8f)
                    )
                    .border(1.5.dp, TmdbCinematicTheme.GlassBorderGradient, RoundedCornerShape(32.dp)),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = TmdbCinematicTheme.GlassSurface)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.onboard),
                        contentDescription = "Onboard Illustration",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // TITLE & DESCRIPTION
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Stream your favorite\nmovies anytime,\nanywhere.",
                    color = TmdbCinematicTheme.TextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Create a personalized watchlist, browse through World's Top 10, and discover blockbuster hits.",
                    color = TmdbCinematicTheme.TextSecondary,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // BOTTOM ACTIONS
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(TmdbCinematicTheme.PrimaryActionGradient)
                        .clickable {
                            hapticController.trigger(AppHaptic.Confirm)
                            onCreateAccountClick()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Create Account",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
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
}

// --- COMPOSE PREVIEW ---
@Preview(showBackground = true, widthDp = 412, heightDp = 850)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen(
        onCreateAccountClick = {},
        onLoginClick = {}
    )
}