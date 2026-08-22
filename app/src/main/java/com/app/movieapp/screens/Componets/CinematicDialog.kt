/*
 * Copyright (c) 2026 Dinesh2510
 * File : CinematicDialog.kt
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

package com.app.movieapp.screens.Componets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.app.movieapp.ui.theme.TmdbCinematicTheme

@Composable
fun CinematicDialog(
    showDialog: Boolean,
    title: String,
    message: String,
    positiveButtonText: String = "Confirm",
    negativeButtonText: String? = "Cancel",
    icon: ImageVector = Icons.Default.Info,
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit = {},
    onDismissRequest: () -> Unit
) {
    if (!showDialog) return

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        AnimatedVisibility(
            visible = showDialog,
            enter = fadeIn() + scaleIn(initialScale = 0.85f),
            exit = fadeOut() + scaleOut(targetScale = 0.85f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 24.dp,
                            shape = RoundedCornerShape(28.dp),
                            spotColor = Color.Black.copy(alpha = 0.8f)
                        )
                        .clip(RoundedCornerShape(28.dp))
                        .border(
                            width = 1.2.dp,
                            brush = TmdbCinematicTheme.GlassBorderGradient,
                            shape = RoundedCornerShape(28.dp)
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = TmdbCinematicTheme.GlassSurface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 1. Top Decorative Icon Badge
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(TmdbCinematicTheme.CoralAccent.copy(alpha = 0.15f))
                                .border(
                                    width = 1.dp,
                                    color = TmdbCinematicTheme.CoralAccent.copy(alpha = 0.3f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = "Dialog Icon",
                                tint = TmdbCinematicTheme.CoralAccent,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 2. Title
                        Text(
                            text = title,
                            color = TmdbCinematicTheme.TextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // 3. Message Body
                        Text(
                            text = message,
                            color = TmdbCinematicTheme.TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // 4. Action Buttons (Positive & Negative)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Negative Button (Optional)
                            if (negativeButtonText != null) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.08f))
                                        .border(
                                            width = 1.dp,
                                            color = Color.White.copy(alpha = 0.15f),
                                            shape = CircleShape
                                        )
                                        .clickable {
                                            onNegativeClick()
                                            onDismissRequest()
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = negativeButtonText,
                                        color = TmdbCinematicTheme.TextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            // Positive Action Button
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .clip(CircleShape)
                                    .background(TmdbCinematicTheme.PrimaryActionGradient)
                                    .clickable {
                                        onPositiveClick()
                                        onDismissRequest()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = positiveButtonText,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun CinematicDialogPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0E17))
    ) {
        CinematicDialog(
            showDialog = true,
            title = "Clear Watchlist?",
            message = "Are you sure you want to remove all saved movies? This action cannot be undone.",
            positiveButtonText = "Clear All",
            negativeButtonText = "Cancel",
            icon = Icons.Default.DeleteSweep,
            onPositiveClick = {},
            onNegativeClick = {},
            onDismissRequest = {}
        )
    }
}
