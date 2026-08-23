/*
 * Copyright (c) 2026 Dinesh2510
 * File : ProfileSection.kt
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

package com.app.movieapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.movieapp.data.viewmodel.AuthViewModel
import com.app.movieapp.ui.theme.TmdbCinematicTheme
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import com.app.movieapp.utlis.AppHaptic
import com.app.movieapp.utlis.rememberHapticController
import com.app.movieapp.screens.components.CinematicTextField

// --- SECTIONS ENUM FOR NAVIGATION ---
enum class ProfileSection {
    PERSONAL_DETAILS,
    NOTIFICATIONS,
    SECURITY_PRIVACY,
    ABOUT_APP
}

// --- MASTER WRAPPER SCREEN ---
@Composable
fun ProfileSectionScreens(
    section: ProfileSection,
    onBackClick: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(TmdbCinematicTheme.AppBackgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding( bottom = 24.dp)
        ) {
            // Top Navigation Bar
            SectionHeaderBar(
                title = when (section) {
                    ProfileSection.PERSONAL_DETAILS -> "Personal Details"
                    ProfileSection.NOTIFICATIONS -> "Notifications"
                    ProfileSection.SECURITY_PRIVACY -> "Security & Privacy"
                    ProfileSection.ABOUT_APP -> "About App"
                },
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Sub-Screen Route Switcher
            when (section) {
                ProfileSection.PERSONAL_DETAILS -> PersonalDetailsContent(viewModel)
                ProfileSection.NOTIFICATIONS -> NotificationsContent()
                ProfileSection.SECURITY_PRIVACY -> SecurityPrivacyContent()
                ProfileSection.ABOUT_APP -> AboutAppContent()
            }
        }
    }
}

// ==========================================
// 1. PERSONAL DETAILS SCREEN
// ==========================================
@Composable
private fun PersonalDetailsContent(viewModel: AuthViewModel) {
    val currentName by viewModel.userName.collectAsState()
    var name by remember(currentName) { mutableStateOf(currentName) }
    var email by remember { mutableStateOf("alex.tmdb@example.com") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Glassmorphic Card Container
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, TmdbCinematicTheme.GlassBorderGradient, RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = TmdbCinematicTheme.GlassSurface)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Avatar Header
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(TmdbCinematicTheme.PrimaryActionGradient)
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = name.take(1).uppercase().ifEmpty { "A" },
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                CinematicTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Full Name"
                )

                Spacer(modifier = Modifier.height(16.dp))

                CinematicTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email Address"
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Save Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(TmdbCinematicTheme.PrimaryActionGradient)
                .clickable {
                    // Triggers local DataStore save logic via AuthViewModel
                    viewModel.register(name, email, "saved_pass") {}
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Save Changes",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ==========================================
// 2. NOTIFICATIONS SCREEN
// ==========================================
@Composable
private fun NotificationsContent() {
    var pushEnabled by remember { mutableStateOf(true) }
    var newReleases by remember { mutableStateOf(true) }
    var watchlistAlerts by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, TmdbCinematicTheme.GlassBorderGradient, RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = TmdbCinematicTheme.GlassSurface)
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                ToggleSettingRow(
                    title = "Push Notifications",
                    subtitle = "Receive push alerts for activity",
                    checked = pushEnabled,
                    onCheckedChange = { pushEnabled = it }
                )
                ProfileDivider()
                ToggleSettingRow(
                    title = "New Movie Releases",
                    subtitle = "Get notified when Top 10 drops",
                    checked = newReleases,
                    onCheckedChange = { newReleases = it }
                )
                ProfileDivider()
                ToggleSettingRow(
                    title = "Watchlist Updates",
                    subtitle = "Alerts when saved movies stream",
                    checked = watchlistAlerts,
                    onCheckedChange = { watchlistAlerts = it }
                )
            }
        }
    }
}

// ==========================================
// 3. SECURITY & PRIVACY SCREEN
// ==========================================
@Composable
private fun SecurityPrivacyContent() {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var biometricLogin by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "CHANGE PASSWORD",
            color = TmdbCinematicTheme.TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 10.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, TmdbCinematicTheme.GlassBorderGradient, RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = TmdbCinematicTheme.GlassSurface)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                CinematicTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = "Current Password",
                    isPassword = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                CinematicTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = "New Password",
                    isPassword = true
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "SECURITY PREFERENCES",
            color = TmdbCinematicTheme.TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 10.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, TmdbCinematicTheme.GlassBorderGradient, RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = TmdbCinematicTheme.GlassSurface)
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                ToggleSettingRow(
                    title = "Biometric Lock",
                    subtitle = "Use Fingerprint / FaceID to unlock",
                    checked = biometricLogin,
                    onCheckedChange = { biometricLogin = it }
                )
            }
        }
    }
}

// ==========================================
// 4. ABOUT APP SCREEN
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AboutAppContent() {
    val uriHandler = LocalUriHandler.current
    val haptics = rememberHapticController()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // App Logo Icon Frame
        Card(
            modifier = Modifier
                .size(110.dp)
                .shadow(elevation = 20.dp, shape = RoundedCornerShape(28.dp))
                .border(1.dp, TmdbCinematicTheme.GlassBorderGradient, RoundedCornerShape(28.dp)),
            colors = CardDefaults.cardColors(containerColor = TmdbCinematicTheme.GlassSurface)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "TMDB", color = TmdbCinematicTheme.CoralAccent, fontSize = 28.sp, fontWeight = FontWeight.Black)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "The TMDB Movie App", color = TmdbCinematicTheme.TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = "Version 1.0.0 (Build 2026)", color = TmdbCinematicTheme.TextSecondary, fontSize = 12.sp)

        Spacer(modifier = Modifier.height(32.dp))

        // ── About ─────────────────────────────────────────────
        InfoCard(title = "About") {
            Text(
                text = "The TMDB Movie App provides high-fidelity discovery, trailers, and real-time movie trends powered by Ktor 3.x, Jetpack Compose, and Koin DI.",
                color = TmdbCinematicTheme.TextSecondary,
                fontSize = 13.sp,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── What's Inside (Features) ────────────────────────────
        InfoCard(title = "What's Inside") {
            FeatureRow(Icons.Default.Home, "Home", "Featured slider, continue watching, popular & trending movies")
            FeatureRow(Icons.Default.Movie, "Movies", "Top-rated films and shows, curated collections")
            FeatureRow(Icons.Default.Bookmark, "Wishlist", "Save movies to watch later, synced locally with Room")
            FeatureRow(Icons.Default.Search, "Search", "Real-time search across the full TMDB catalog")
            FeatureRow(Icons.Default.Person, "Profile", "Preferences, theme, and account settings", isLast = true)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Built With (Tech Stack) ──────────────────────────────
        InfoCard(title = "Built With") {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val stack = listOf(
                    "Jetpack Compose", "Kotlin", "MVVM", "Ktor 3.x", "Koin DI",
                    "Paging 3", "Room", "DataStore", "Coil 3", "Haptic Feedback"
                )
                stack.forEach { TechChip(it) }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Developer ─────────────────────────────────────────
        InfoCard(title = "Developer") {
            Text(
                text = "Dinesh",
                color = TmdbCinematicTheme.TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Android Developer · Content Creator",
                color = TmdbCinematicTheme.TextSecondary,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            SocialLinkRow(
                icon = Icons.Default.Code,
                label = "GitHub",
                value = "github.com/Dinesh2510",
                onClick = {
                    haptics.trigger(AppHaptic.Click)
                    uriHandler.openUri("https://github.com/Dinesh2510")
                }
            )
            SocialLinkRow(
                icon = Icons.Default.PlayCircle,
                label = "YouTube",
                value = "@pixeldesigndeveloper",
                onClick = {
                    haptics.trigger(AppHaptic.Click)
                    uriHandler.openUri("https://www.youtube.com/@pixeldesigndeveloper")
                }
            )
            SocialLinkRow(
                icon = Icons.Default.Language,
                label = "Website",
                value = "pixeldev.in",
                onClick = {
                    haptics.trigger(AppHaptic.Click)
                    uriHandler.openUri("https://pixeldev.in")
                },
                isLast = true
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ── Copyright / License ──────────────────────────────────
        Text(
            text = "Copyright © 2026 Dinesh. All Rights Reserved.",
            color = TmdbCinematicTheme.TextSecondary,
            fontSize = 11.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Licensed under the Apache License, Version 2.0",
            color = TmdbCinematicTheme.TextSecondary.copy(alpha = 0.7f),
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable {
                haptics.trigger(AppHaptic.Click)
                uriHandler.openUri("https://www.apache.org/licenses/LICENSE-2.0")
            }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// ── Reusable pieces ─────────────────────────────────────────────

@Composable
private fun InfoCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, TmdbCinematicTheme.GlassBorderGradient, RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = TmdbCinematicTheme.GlassSurface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = title, color = TmdbCinematicTheme.CoralAccent, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
private fun FeatureRow(
    icon: ImageVector,
    title: String,
    description: String,
    isLast: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TmdbCinematicTheme.CoralAccent,
            modifier = Modifier.size(18.dp).padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = title, color = TmdbCinematicTheme.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = description, color = TmdbCinematicTheme.TextSecondary, fontSize = 12.sp, lineHeight = 16.sp)
        }
    }
    if (!isLast) Spacer(modifier = Modifier.height(14.dp))
}

@Composable
private fun TechChip(label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(TmdbCinematicTheme.GlassSurface)
            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Text(
            text = label,
            color = TmdbCinematicTheme.TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun SocialLinkRow(
    icon: ImageVector,
    label: String,
    value: String,
    onClick: () -> Unit,
    isLast: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(TmdbCinematicTheme.GlassSurface)
                .border(1.dp, Color.White.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = label, tint = TmdbCinematicTheme.CoralAccent, modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, color = TmdbCinematicTheme.TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Text(text = value, color = TmdbCinematicTheme.TextSecondary, fontSize = 11.sp)
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = TmdbCinematicTheme.TextSecondary,
            modifier = Modifier.size(12.dp)
        )
    }
    if (!isLast) HorizontalDivider(color = Color.White.copy(alpha = 0.08f), modifier = Modifier.padding(start = 46.dp))
}

// --- REUSABLE UI COMPONENTS ---

@Composable
private fun SectionHeaderBar(title: String, onBackClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.08f))
                .clickable { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                contentDescription = "Back",
                tint = TmdbCinematicTheme.TextPrimary,
                modifier = Modifier
                    .size(16.dp)
                    .padding(start = 4.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            color = TmdbCinematicTheme.TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ToggleSettingRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = TmdbCinematicTheme.TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Text(text = subtitle, color = TmdbCinematicTheme.TextSecondary, fontSize = 12.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = TmdbCinematicTheme.CoralAccent,
                uncheckedThumbColor = TmdbCinematicTheme.TextSecondary,
                uncheckedTrackColor = Color.White.copy(alpha = 0.10f)
            )
        )
    }
}

// --- COMPOSE PREVIEWS ---

@Preview(showBackground = true, widthDp = 412, heightDp = 850)
@Composable
fun PersonalDetailsPreview() {
    ProfileSectionScreens(section = ProfileSection.PERSONAL_DETAILS, onBackClick = {})
}

@Preview(showBackground = true, widthDp = 412, heightDp = 850)
@Composable
fun NotificationsPreview() {
    ProfileSectionScreens(section = ProfileSection.NOTIFICATIONS, onBackClick = {})
}

@Preview(showBackground = true, widthDp = 412, heightDp = 850)
@Composable
fun SecurityPrivacyPreview() {
    ProfileSectionScreens(section = ProfileSection.SECURITY_PRIVACY, onBackClick = {})
}

@Preview(showBackground = true, widthDp = 412, heightDp = 850)
@Composable
fun AboutAppPreview() {
    ProfileSectionScreens(section = ProfileSection.ABOUT_APP, onBackClick = {})
}