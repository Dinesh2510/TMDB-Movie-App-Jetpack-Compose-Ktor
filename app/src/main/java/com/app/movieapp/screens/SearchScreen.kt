/*
 * Copyright (c) 2026 Dinesh2510
 * File : SearchScreen.kt
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.app.movieapp.R
import com.app.movieapp.data.viewmodel.SearchViewModel
import com.app.movieapp.graph.MovieAppScreen
import com.app.movieapp.screens.componets.CinematicErrorState
import com.app.movieapp.screens.componets.ErrorStrip
import com.app.movieapp.screens.componets.SearchMovieCard
import com.app.movieapp.ui.theme.TmdbCinematicTheme
import com.app.movieapp.utlis.CenteredCircularProgressIndicator
import com.app.movieapp.utlis.Constants.Companion.BASE_POSTER_IMAGE_URL
import org.koin.androidx.compose.koinViewModel
import java.io.IOException
import java.net.UnknownHostException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = koinViewModel(),
    navController: NavHostController,
) {
    val searchResult = searchViewModel.searchPagingFlow.collectAsLazyPagingItems()
    val activeQuery by searchViewModel.searchQuery.collectAsState()
    var queryText by rememberSaveable { mutableStateOf(activeQuery) }
    val focusManager = LocalFocusManager.current

    val refreshState = searchResult.loadState.refresh

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 12.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(start = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(22.dp))

                        Column {
                            Text(
                                text = "Explore & Search",
                                color = TmdbCinematicTheme.TextPrimary,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "Find your favorite movies, actors, or genres",
                                color = TmdbCinematicTheme.TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Search Input Field
                    OutlinedTextField(
                        value = queryText,
                        onValueChange = { queryText = it },
                        placeholder = {
                            Text(
                                text = "Search movies, series...",
                                color = TmdbCinematicTheme.TextSecondary,
                                fontSize = 14.sp
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                if (queryText.isNotBlank()) {
                                    searchViewModel.onSearchQueryChanged(queryText)
                                    focusManager.clearFocus()
                                }
                            }
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Icon",
                                tint = TmdbCinematicTheme.CoralAccent
                            )
                        },
                        trailingIcon = {
                            if (queryText.isNotEmpty()) {
                                IconButton(onClick = { queryText = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear Text",
                                        tint = TmdbCinematicTheme.TextSecondary
                                    )
                                }
                            }
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = TmdbCinematicTheme.GlassSurface,
                            unfocusedContainerColor = TmdbCinematicTheme.GlassSurface,
                            focusedBorderColor = TmdbCinematicTheme.CoralAccent,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                            focusedTextColor = TmdbCinematicTheme.TextPrimary,
                            unfocusedTextColor = TmdbCinematicTheme.TextPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Suggestion Chips
                    val suggestions = listOf("Jack Reacher", "Dune", "Spider Man", "TopGun Maverick", "Marvel")
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(suggestions) { tag ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(TmdbCinematicTheme.GlassSurface)
                                    .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                                    .clickable {
                                        queryText = tag
                                        searchViewModel.onSearchQueryChanged(tag)
                                        focusManager.clearFocus()
                                    }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = tag,
                                    color = TmdbCinematicTheme.TextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(TmdbCinematicTheme.AppBackgroundGradient)
                .padding(paddingValues)
        ) {
            when {
                // Fullscreen error on initial load (Offline / No connection)
                refreshState is LoadState.Error -> {
                    val errorThrowable = refreshState.error
                    val errorRes = when (errorThrowable) {
                        is UnknownHostException -> R.string.error_no_internet
                        is IOException -> R.string.error_network_communication
                        else -> R.string.error_unknown
                    }

                    CinematicErrorState(
                        errorMessage = stringResource(id = errorRes),
                        onRetryClick = { searchResult.retry() }
                    )
                }

                // Fullscreen loader on initial load
                refreshState is LoadState.Loading && searchResult.itemCount == 0 -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CenteredCircularProgressIndicator()
                    }
                }

                // Search Results List
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        if (queryText != activeQuery && queryText.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(TmdbCinematicTheme.PrimaryActionGradient)
                                    .clickable {
                                        searchViewModel.onSearchQueryChanged(queryText)
                                        focusManager.clearFocus()
                                    }
                                    .padding(horizontal = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Search for \"$queryText\"",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        if (searchResult.itemCount > 0) {
                            Text(
                                text = "SEARCH RESULTS (${searchResult.itemCount})",
                                color = TmdbCinematicTheme.TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            contentPadding = PaddingValues(bottom = 100.dp)
                        ) {
                            items(
                                count = searchResult.itemCount,
                                key = { index -> searchResult[index]?.id ?: index }
                            ) { index ->
                                val movie = searchResult[index]
                                val imageUrl = movie?.posterPath?.let { BASE_POSTER_IMAGE_URL + it } ?: ""

                                SearchMovieCard(
                                    imageUrl = imageUrl,
                                    title = movie?.title ?: movie?.originalName ?: "Untitled",
                                    overview = movie?.overview ?: "No description available."
                                ) {
                                    movie?.id?.let { id ->
                                        navController.navigate("${MovieAppScreen.MOVIE_HOME_DETAILS.route}/$id")
                                    }
                                }
                            }

                            // Append pagination loader & error handling
                            searchResult.apply {
                                when {
                                    loadState.append is LoadState.Loading -> {
                                        item {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                CenteredCircularProgressIndicator()
                                            }
                                        }
                                    }
                                    loadState.append is LoadState.Error -> {
                                        val error = (loadState.append as LoadState.Error).error
                                        val errorResId = when (error) {
                                            is UnknownHostException -> R.string.error_no_internet
                                            is IOException -> R.string.error_network_communication
                                            else -> R.string.error_unknown
                                        }
                                        item {
                                            ErrorStrip(message = stringResource(errorResId))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}