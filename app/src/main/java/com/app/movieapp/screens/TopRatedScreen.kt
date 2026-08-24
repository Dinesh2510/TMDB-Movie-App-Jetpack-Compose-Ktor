/*
 * Copyright (c) 2026 Dinesh2510
 * File : TopRatedScreen.kt
 * Project : TMDB Ktor
 * Module : TMDB_Ktor.app.main
 * Created on : 2026-08-22 15:27
 * Last modified: 2026-08-24 23:35
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

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.app.movieapp.R
import com.app.movieapp.data.viewmodel.ContentType
import com.app.movieapp.data.viewmodel.TopRatedViewModel
import com.app.movieapp.graph.MovieAppScreen
import com.app.movieapp.models.Movies
import com.app.movieapp.screens.components.CinematicErrorState
import com.app.movieapp.ui.theme.TmdbCinematicTheme
import com.app.movieapp.utlis.AppHaptic
import com.app.movieapp.utlis.CenteredCircularProgressIndicator
import com.app.movieapp.utlis.Constants.Companion.BASE_BACKDROP_IMAGE_URL
import com.app.movieapp.utlis.Constants.Companion.BASE_POSTER_IMAGE_URL
import com.app.movieapp.utlis.rememberHapticController
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.io.IOException
import java.net.UnknownHostException

@Composable
fun TopRatedScreen(
    navController: NavController,
    viewModel: TopRatedViewModel = koinViewModel()
) {
    val hapticController = rememberHapticController()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val tvPagingItems = viewModel.topRatedTvPagingFlow.collectAsLazyPagingItems()
    val moviePagingItems = viewModel.topRatedMoviesPagingFlow.collectAsLazyPagingItems()

    val currentItems = if (selectedTab == ContentType.TV_SHOWS) tvPagingItems else moviePagingItems
    val refreshState = currentItems.loadState.refresh
    val appendState = currentItems.loadState.append

    var hasConfirmedLoad by remember { mutableStateOf(false) }

    // Haptic on pagination refresh outcome
    LaunchedEffect(refreshState) {
        when (refreshState) {
            is LoadState.NotLoading -> {
                if (!hasConfirmedLoad && currentItems.itemCount > 0) {
                    hasConfirmedLoad = true
                    hapticController.trigger(AppHaptic.Confirm)
                }
            }
            is LoadState.Error -> {
                hapticController.trigger(AppHaptic.Reject)
            }
            else -> Unit
        }
    }

    // Haptic on pagination append error
    LaunchedEffect(appendState) {
        if (appendState is LoadState.Error) {
            hapticController.trigger(AppHaptic.Reject)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TmdbCinematicTheme.AppBackgroundGradient)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // 1. Top Toggle Pills Bar
            Spacer(modifier = Modifier.height(44.dp))
            SegmentedTabBar(
                selectedTab = selectedTab,
                onTabSelected = { newTab ->
                    if (selectedTab != newTab) {
                        hasConfirmedLoad = false
                        viewModel.selectTab(newTab)
                    }
                }
            )

            // 2. Main Content / Loader / Error State
            when {
                // Initial Loading State
                refreshState is LoadState.Loading && currentItems.itemCount == 0 -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CenteredCircularProgressIndicator()
                    }
                }

                // Initial Error State (e.g. Offline)
                refreshState is LoadState.Error -> {
                    val error = (refreshState as LoadState.Error).error
                    val errorRes = when (error) {
                        is UnknownHostException -> R.string.error_no_internet
                        is IOException -> R.string.error_network_communication
                        else -> R.string.error_unknown
                    }
                    CinematicErrorState(
                        errorMessage = stringResource(id = errorRes),
                        onRetryClick = {
                            hapticController.trigger(AppHaptic.Click)
                            currentItems.retry()
                        }
                    )
                }

                // Paginated Grid
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Hero Slider Carousel (Full Span)
                        if (currentItems.itemCount >= 5) {
                            item(span = { GridItemSpan(2) }) {
                                val heroMovies = (0..4).mapNotNull { currentItems[it] }
                                HostarHeroSlider(
                                    movies = heroMovies,
                                    onMovieClick = { id ->
                                        hapticController.trigger(AppHaptic.Click)
                                        navController.navigate("${MovieAppScreen.MOVIE_HOME_DETAILS.route}/$id")
                                    }
                                )
                            }
                        }

                        // Section Header Title (Full Span)
                        item(span = { GridItemSpan(2) }) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (selectedTab == ContentType.TV_SHOWS) "🔥 Popular TV Shows" else "🔥 Top Movies",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = "${currentItems.itemCount} Items",
                                    color = TmdbCinematicTheme.CoralAccent,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Grid Items
                        items(
                            count = currentItems.itemCount,
                            key = { index -> "${currentItems[index]?.id}_$index" }
                        ) { index ->
                            currentItems[index]?.let { movie ->
                                TopRatedGridCard(
                                    movie = movie,
                                    onMovieClick = {
                                        hapticController.trigger(AppHaptic.Click)
                                        navController.navigate("${MovieAppScreen.MOVIE_HOME_DETAILS.route}/${movie.id}")
                                    }
                                )
                            }
                        }

                        // Bottom Pagination Loader
                        if (currentItems.loadState.append is LoadState.Loading) {
                            item(span = { GridItemSpan(2) }) {
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
                    }
                }
            }
        }
    }
}

// --- SEGMENTED TAB TOGGLE BAR ---
@Composable
private fun SegmentedTabBar(
    selectedTab: ContentType,
    onTabSelected: (ContentType) -> Unit
) {
    val hapticController = rememberHapticController()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .width(280.dp)
                .height(48.dp)
                .clip(CircleShape)
                .background(TmdbCinematicTheme.GlassSurface)
                .border(1.dp, TmdbCinematicTheme.GlassBorderGradient, CircleShape)
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TV Shows Tab
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(
                        brush = if (selectedTab == ContentType.TV_SHOWS) {
                            TmdbCinematicTheme.PrimaryActionGradient
                        } else {
                            Brush.horizontalGradient(listOf(Color.Transparent, Color.Transparent))
                        }
                    )
                    .clickable {
                        if (selectedTab != ContentType.TV_SHOWS) {
                            hapticController.trigger(AppHaptic.SegmentTick)
                            onTabSelected(ContentType.TV_SHOWS)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Tv,
                        contentDescription = "TV",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "TV Shows",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Movies Tab
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(
                        brush = if (selectedTab == ContentType.MOVIES) {
                            TmdbCinematicTheme.PrimaryActionGradient
                        } else {
                            Brush.horizontalGradient(listOf(Color.Transparent, Color.Transparent))
                        }
                    )
                    .clickable {
                        if (selectedTab != ContentType.MOVIES) {
                            hapticController.trigger(AppHaptic.SegmentTick)
                            onTabSelected(ContentType.MOVIES)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Movie,
                        contentDescription = "Movies",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Movies",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// --- HERO SLIDER ---
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HostarHeroSlider(
    movies: List<Movies>,
    onMovieClick: (Int) -> Unit
) {
    if (movies.isEmpty()) return

    val hapticController = rememberHapticController()
    val pagerState = rememberPagerState(pageCount = { movies.size })
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    // Tactile tick when user drags and snaps to a new card
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect {
            if (isDragged) {
                hapticController.trigger(AppHaptic.SegmentTick)
            }
        }
    }

    // Smooth auto-scroll loop
    LaunchedEffect(isDragged, movies.size) {
        if (!isDragged && movies.size > 1) {
            while (true) {
                delay(3500L)
                val nextPage = (pagerState.currentPage + 1) % movies.size
                pagerState.animateScrollToPage(
                    page = nextPage,
                    animationSpec = tween(durationMillis = 800)
                )
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "For You",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black
            )

            // Dynamic Dot Indicators
            if (movies.size > 1) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(movies.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .size(
                                    width = if (isSelected) 16.dp else 6.dp,
                                    height = 6.dp
                                )
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) TmdbCinematicTheme.CoralAccent else Color.White.copy(alpha = 0.3f)
                                )
                        )
                    }
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            pageSpacing = 12.dp,
            contentPadding = PaddingValues(end = 32.dp)
        ) { page ->
            val movie = movies[page]
            val backdropUrl = "$BASE_BACKDROP_IMAGE_URL${movie.backdropPath}"

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clickable { onMovieClick(movie.id) },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = TmdbCinematicTheme.GlassSurface)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = backdropUrl,
                        contentDescription = movie.displayTitle,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Scrim Gradient
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.3f),
                                        Color.Black.copy(alpha = 0.95f)
                                    )
                                )
                            )
                    )

                    // Top Status Badge
                    Box(
                        modifier = Modifier
                            .padding(14.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black.copy(alpha = 0.6f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "New Release",
                            color = TmdbCinematicTheme.CoralAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Floating Action Buttons (Right)
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f))
                                .clickable {
                                    hapticController.trigger(AppHaptic.ToggleOn)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                        }

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(TmdbCinematicTheme.PrimaryActionGradient)
                                .clickable {
                                    hapticController.trigger(AppHaptic.Confirm)
                                    onMovieClick(movie.id)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.White)
                        }
                    }

                    // Bottom Movie Meta Details
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth(0.7f)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = movie.displayTitle,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${movie.displayReleaseDate.take(4)} • English • Rating ★ ${String.format("%.1f", movie.voteAverage)}",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

// --- TWO-COLUMN GRID POSTER CARD ---
@Composable
private fun TopRatedGridCard(
    movie: Movies,
    onMovieClick: () -> Unit
) {
    val posterUrl = "$BASE_POSTER_IMAGE_URL${movie.posterPath}"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onMovieClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = TmdbCinematicTheme.GlassSurface)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.7f)
            ) {
                AsyncImage(
                    model = posterUrl,
                    contentDescription = movie.displayTitle,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Top Left Year Pill
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(TmdbCinematicTheme.CoralAccent)
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = movie.displayReleaseDate.take(4).ifEmpty { "N/A" },
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Top Right Rating Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black.copy(alpha = 0.75f))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = String.format("%.1f", movie.voteAverage),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Title Label
            Text(
                text = movie.displayTitle,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}