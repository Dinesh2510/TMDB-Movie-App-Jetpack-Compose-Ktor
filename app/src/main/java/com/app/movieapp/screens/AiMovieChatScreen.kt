/*
 * Copyright (c) 2026 Dinesh2510
 * File : AiMovieChatScreen.kt
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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.app.movieapp.data.viewmodel.AiChatViewModel
import com.app.movieapp.data.viewmodel.ChatMessage
import com.app.movieapp.screens.Componets.CinematicDialog
import com.app.movieapp.ui.theme.TmdbCinematicTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun AiMovieChatScreen(
    navController: NavHostController,
    viewModel: AiChatViewModel = koinViewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    val listState = rememberLazyListState()
    var inputText by remember { mutableStateOf("") }
    var showClearDialog by remember { mutableStateOf(false) }

    // Auto-scroll on new message
    LaunchedEffect(messages.size, isGenerating) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    // Clear Chat Dialog
    CinematicDialog(
        showDialog = showClearDialog,
        title = "Clear Conversation?",
        message = "Do you want to reset your movie recommendations chat?",
        positiveButtonText = "Clear",
        negativeButtonText = "Keep",
        icon = Icons.Default.DeleteSweep,
        onPositiveClick = {
            showClearDialog = false
            viewModel.clearChat()
        },
        onDismissRequest = {
            showClearDialog = false
        }
    )

    Scaffold(
        containerColor = Color.Transparent,
        modifier = Modifier
            .fillMaxSize()
            .background(TmdbCinematicTheme.AppBackgroundGradient)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
        topBar = {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(TmdbCinematicTheme.GlassSurface)
                            .border(1.dp, TmdbCinematicTheme.GlassBorderGradient, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = "Back",
                            tint = TmdbCinematicTheme.TextPrimary,
                            modifier = Modifier.size(16.dp).padding(start = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(TmdbCinematicTheme.PrimaryActionGradient),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "AI",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "CineBot AI",
                                color = TmdbCinematicTheme.TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = if (isGenerating) "Thinking..." else "Movie Recommendation AI",
                                color = if (isGenerating) TmdbCinematicTheme.CoralAccent else TmdbCinematicTheme.TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                IconButton(
                    onClick = { showClearDialog = true },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(TmdbCinematicTheme.GlassSurface)
                        .border(1.dp, TmdbCinematicTheme.GlassBorderGradient, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = "Clear",
                        tint = TmdbCinematicTheme.TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Chat Bubble List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp)
            ) {
                items(messages, key = { it.id }) { message ->
                    ChatBubble(message = message)
                }

                if (isGenerating) {
                    item {
                        AiTypingIndicator()
                    }
                }
            }

            // Quick Prompt Suggestions
            val quickPrompts = listOf(
                "Mind-bending sci-fi movies",
                "Best 90s thriller classics",
                "Lighthearted romantic comedies",
                "Movies like Interstellar"
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(quickPrompts) { prompt ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(TmdbCinematicTheme.GlassSurface)
                            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                            .clickable {
                                viewModel.sendMessage(prompt)
                            }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = prompt,
                            color = TmdbCinematicTheme.TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Bottom Input Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            text = "Ask CineBot about movies, actors, plots...",
                            color = TmdbCinematicTheme.TextSecondary,
                            fontSize = 13.sp
                        )
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = TmdbCinematicTheme.GlassSurface,
                        unfocusedContainerColor = TmdbCinematicTheme.GlassSurface,
                        focusedBorderColor = TmdbCinematicTheme.CoralAccent,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                        focusedTextColor = TmdbCinematicTheme.TextPrimary,
                        unfocusedTextColor = TmdbCinematicTheme.TextPrimary
                    ),
                    maxLines = 3,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(10.dp))

                // Send Button
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(
                            if (inputText.isNotBlank() && !isGenerating)
                                TmdbCinematicTheme.PrimaryActionGradient
                            else
                                Brush.horizontalGradient(listOf(Color.White.copy(alpha = 0.1f), Color.White.copy(alpha = 0.1f)))
                        )
                        .clickable(enabled = inputText.isNotBlank() && !isGenerating) {
                            viewModel.sendMessage(inputText)
                            inputText = ""
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = if (inputText.isNotBlank() && !isGenerating) Color.White else TmdbCinematicTheme.TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

// --- CHAT BUBBLE ---
@Composable
private fun ChatBubble(message: ChatMessage) {
    val isUser = message.isUser

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(TmdbCinematicTheme.PrimaryActionGradient)
                    .align(Alignment.Top),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(if (isUser) 0.8f else 0.85f)
                .shadow(
                    elevation = if (isUser) 8.dp else 12.dp,
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = if (isUser) 20.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 20.dp
                    )
                )
                .clip(
                    RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = if (isUser) 20.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 20.dp
                    )
                )
                .background(
                    if (isUser)
                        TmdbCinematicTheme.PrimaryActionGradient
                    else
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF1B192B).copy(alpha = 0.85f),
                                Color(0xFF141220).copy(alpha = 0.90f)
                            )
                        )
                )
                .border(
                    width = 1.dp,
                    brush = if (isUser) Brush.linearGradient(listOf(Color.White.copy(alpha = 0.3f), Color.Transparent))
                    else TmdbCinematicTheme.GlassBorderGradient,
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = if (isUser) 20.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 20.dp
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = message.text,
                color = Color.White,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = if (isUser) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

// --- TYPING INDICATOR ---
@Composable
private fun AiTypingIndicator() {
    Row(
        modifier = Modifier.padding(start = 36.dp, top = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            color = TmdbCinematicTheme.CoralAccent,
            modifier = Modifier.size(16.dp),
            strokeWidth = 2.dp
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "CineBot is thinking...",
            color = TmdbCinematicTheme.TextSecondary,
            fontSize = 12.sp
        )
    }
}