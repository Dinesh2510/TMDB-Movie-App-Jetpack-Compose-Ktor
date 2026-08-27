/*
 * Copyright (c) 2026 Dinesh2510
 * File : ProfileScreen.kt
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Vibration
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.movieapp.data.viewmodel.AuthViewModel
import com.app.movieapp.data.viewmodel.ContinueWatchingViewModel
import com.app.movieapp.data.viewmodel.WatchListViewModel
import com.app.movieapp.screens.components.CinematicDialog
import com.app.movieapp.ui.theme.TmdbCinematicTheme
import com.app.movieapp.utlis.AppHaptic
import com.app.movieapp.utlis.rememberHapticController
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel = koinViewModel(),
    watchListViewModel: WatchListViewModel = koinViewModel(),
    continueWatchingViewModel: ContinueWatchingViewModel = koinViewModel(),
    onWatchlistClick: () -> Unit = {},
    onLogoutClick: () -> Unit
) {
    val hapticController = rememberHapticController()
    val userName by authViewModel.userName.collectAsState()
    val watchlistData by watchListViewModel.myMovieData.value.collectAsState(initial = emptyList())
    val continueWatchingData by continueWatchingViewModel.continueWatchingList.collectAsState()
    val isHapticsEnabled by authViewModel.isHapticsEnabled.collectAsState(initial = true)

    var showLogoutDialog by remember { mutableStateOf(false) }
    var activeSection by remember { mutableStateOf<ProfileSection?>(null) }

    activeSection?.let { section ->
        ProfileSectionScreens(
            section = section,
            onBackClick = { activeSection = null },
            viewModel = authViewModel
        )
        return
    }

    CinematicDialog(
        showDialog = showLogoutDialog,
        title = "Log Out?",
        message = "Are you sure you want to log out of your account?",
        positiveButtonText = "Log Out",
        negativeButtonText = "Cancel",
        icon = Icons.AutoMirrored.Filled.Logout,
        onPositiveClick = {
            showLogoutDialog = false
            authViewModel.logout {
                onLogoutClick()
            }
        },
        onDismissRequest = {
            showLogoutDialog = false
        }
    )

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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Profile",
                color = TmdbCinematicTheme.TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // USER AVATAR CARD
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(28.dp),
                        ambientColor = Color.Black.copy(alpha = 0.5f),
                        spotColor = Color.Black.copy(alpha = 0.7f)
                    )
                    .clip(RoundedCornerShape(28.dp))
                    .border(1.dp, TmdbCinematicTheme.GlassBorderGradient, RoundedCornerShape(28.dp)),
                colors = CardDefaults.cardColors(containerColor = TmdbCinematicTheme.GlassSurface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Box(
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape)
                                .background(TmdbCinematicTheme.PrimaryActionGradient)
                                .border(2.dp, Color.White.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = userName.take(1).uppercase().ifEmpty { "A" },
                                color = Color.White,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(TmdbCinematicTheme.CoralAccent)
                                .border(2.dp, Color(0xFF0F0E17), CircleShape)
                                .clickable {
                                    hapticController.trigger(AppHaptic.Click)
                                    activeSection = ProfileSection.PERSONAL_DETAILS
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Edit Profile",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = userName.ifEmpty { "Alex" },
                        color = TmdbCinematicTheme.TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Premium Member",
                        color = TmdbCinematicTheme.CoralAccent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // STATS ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProfileStatCard(
                    title = "Watchlist",
                    value = watchlistData.size.toString(),
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            hapticController.trigger(AppHaptic.Click)
                            onWatchlistClick()
                        }
                )
                ProfileStatCard(
                    title = "Watching",
                    value = continueWatchingData.size.toString(),
                    modifier = Modifier.weight(1f)
                )
                ProfileStatCard(
                    title = "Reviews",
                    value = "0",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ACCOUNT SETTINGS
            Text(
                text = "ACCOUNT SETTINGS",
                color = TmdbCinematicTheme.TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 4.dp, bottom = 10.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, TmdbCinematicTheme.GlassBorderGradient, RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = TmdbCinematicTheme.GlassSurface)
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    // Personal Details
                    ProfileOptionTile(
                        icon = Icons.Filled.PersonOutline,
                        title = "Personal Details",
                        onClick = {
                            hapticController.trigger(AppHaptic.Click)
                            activeSection = ProfileSection.PERSONAL_DETAILS
                        }
                    )
                    ProfileDivider()

                    // Watchlist
                    ProfileOptionTile(
                        icon = Icons.Filled.BookmarkBorder,
                        title = "My Watchlist",
                        onClick = {
                            hapticController.trigger(AppHaptic.Click)
                            onWatchlistClick()
                        }
                    )
                    ProfileDivider()

                    // Haptic Feedback Global Switch Tile
                    ProfileSwitchTile(
                        icon = Icons.Filled.Vibration,
                        title = "Haptic Feedback",
                        checked = isHapticsEnabled,
                        onCheckedChange = { enabled ->
                            authViewModel.setHapticsEnabled(enabled)
                            if (enabled) {
                                hapticController.triggerForced(AppHaptic.ToggleOn)
                            }
                        }
                    )
                    ProfileDivider()

                    // Notifications
                    ProfileOptionTile(
                        icon = Icons.Filled.NotificationsNone,
                        title = "Notifications",
                        onClick = {
                            hapticController.trigger(AppHaptic.Click)
                            activeSection = ProfileSection.NOTIFICATIONS
                        }
                    )
                    ProfileDivider()

                    // Security & Privacy
                    ProfileOptionTile(
                        icon = Icons.Filled.Security,
                        title = "Security & Privacy",
                        onClick = {
                            hapticController.trigger(AppHaptic.Click)
                            activeSection = ProfileSection.SECURITY_PRIVACY
                        }
                    )
                    ProfileDivider()

                    // About App
                    ProfileOptionTile(
                        icon = Icons.Filled.Info,
                        title = "About App",
                        onClick = {
                            hapticController.trigger(AppHaptic.Click)
                            activeSection = ProfileSection.ABOUT_APP
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // LOGOUT BUTTON
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF2C151B))
                    .border(1.dp, TmdbCinematicTheme.CoralAccent.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .clickable {
                        hapticController.trigger(AppHaptic.Click)
                        showLogoutDialog = true
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Logout",
                        tint = TmdbCinematicTheme.CoralAccent,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Log Out",
                        color = TmdbCinematicTheme.CoralAccent,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ── PROFILE SWITCH TILE COMPOSABLE ─────────────────────────────────
@Composable
fun ProfileSwitchTile(
    icon: ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = TmdbCinematicTheme.TextPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = title,
                color = TmdbCinematicTheme.TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = TmdbCinematicTheme.CoralAccent,
                uncheckedThumbColor = TmdbCinematicTheme.TextSecondary,
                uncheckedTrackColor = Color.White.copy(alpha = 0.1f)
            )
        )
    }
}

@Composable
fun ProfileStatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(72.dp)
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, TmdbCinematicTheme.GlassBorderGradient, RoundedCornerShape(18.dp)),
        colors = CardDefaults.cardColors(containerColor = TmdbCinematicTheme.GlassSurface)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                color = TmdbCinematicTheme.TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                color = TmdbCinematicTheme.TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ProfileOptionTile(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 14.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = TmdbCinematicTheme.TextPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = title,
                color = TmdbCinematicTheme.TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = "Navigate",
            tint = TmdbCinematicTheme.TextSecondary,
            modifier = Modifier.size(14.dp)
        )
    }
}

@Composable
fun ProfileDivider() {
    HorizontalDivider(
        color = Color.White.copy(alpha = 0.06f),
        thickness = 1.dp
    )
}

@Preview(showBackground = true, widthDp = 412, heightDp = 850)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(onLogoutClick = {})
}