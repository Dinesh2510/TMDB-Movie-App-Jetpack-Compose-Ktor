/*
 * Copyright (c) 2026 Dinesh2510
 * File : MovieDetailsScreen.kt
 * Project : TMDB Ktor
 * Module : TMDB_Ktor.app.main
 * Created on : 2026-08-22 15:27
 * Last modified: 2026-08-29 12:00
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

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.app.movieapp.R
import com.app.movieapp.data.local.WatchListModel
import com.app.movieapp.data.remote.response.MovieDetailsDTO
import com.app.movieapp.data.remote.response.MovieResponse
import com.app.movieapp.data.viewmodel.ContinueWatchingViewModel
import com.app.movieapp.data.viewmodel.MovieDetailsUIState
import com.app.movieapp.data.viewmodel.MovieDetailsViewModel
import com.app.movieapp.data.viewmodel.WatchListViewModel
import com.app.movieapp.graph.MovieAppScreen
import com.app.movieapp.models.Cast
import com.app.movieapp.models.CountryWatchProvidersDTO
import com.app.movieapp.models.ProviderItemDTO
import com.app.movieapp.screens.components.CinematicErrorState
import com.app.movieapp.screens.components.HomeSmallThumb
import com.app.movieapp.screens.components.VideoSelectionDialog
import com.app.movieapp.ui.theme.TmdbCinematicTheme
import com.app.movieapp.utlis.AppHaptic
import com.app.movieapp.utlis.CenteredCircularProgressIndicator
import com.app.movieapp.utlis.Constants
import com.app.movieapp.utlis.Constants.Companion.BASE_POSTER_IMAGE_URL
import com.app.movieapp.utlis.MovieState
import com.app.movieapp.utlis.rememberHapticController
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun MovieDetailsScreen(
    navController: NavHostController,
    movieId: String,
    viewModel: MovieDetailsViewModel = koinViewModel(),
    watchListViewModel: WatchListViewModel = koinViewModel(),
    continueWatchingViewModel: ContinueWatchingViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val watchProvidersState by viewModel.watchProvidersState.collectAsState()

    LaunchedEffect(movieId) {
        val parsedId = movieId.toIntOrNull() ?: 1
        viewModel.fetchAllMovieDetails(movieId)
        viewModel.fetchMovieWatchProviders(parsedId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TmdbCinematicTheme.AppBackgroundGradient)
    ) {
        when (val state = uiState) {
            is MovieDetailsUIState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CenteredCircularProgressIndicator()
                }
            }

            is MovieDetailsUIState.Error -> {
                CinematicErrorState(
                    errorMessage = stringResource(id = state.messageRes),
                    onRetryClick = {
                        val parsedId = movieId.toIntOrNull() ?: 1
                        viewModel.fetchAllMovieDetails(movieId)
                        viewModel.fetchMovieWatchProviders(parsedId)
                    }
                )
            }

            is MovieDetailsUIState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 60.dp)
                ) {
                    DisplayMovieData(
                        moviesInfo = state.movieDetails,
                        navController = navController,
                        watchListViewModel = watchListViewModel,
                        continueWatchingViewModel = continueWatchingViewModel
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .shadow(
                                elevation = 24.dp,
                                shape = RoundedCornerShape(28.dp),
                                ambientColor = Color.Black.copy(alpha = 0.6f),
                                spotColor = Color.Black.copy(alpha = 0.8f)
                            )
                            .clip(RoundedCornerShape(28.dp))
                            .border(
                                1.dp,
                                TmdbCinematicTheme.GlassBorderGradient,
                                RoundedCornerShape(28.dp)
                            ),
                        colors = CardDefaults.cardColors(containerColor = TmdbCinematicTheme.GlassSurface)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            // 1. Expandable Overview Section
                            state.movieDetails.overview?.let { overviewText ->
                                if (overviewText.isNotBlank()) {
                                    ExpandableOverviewSection(overview = overviewText)
                                    Spacer(modifier = Modifier.height(20.dp))
                                }
                            }

                            // 2. Watch Providers (Streaming Availability) — Only render space when data exists!
                            if (watchProvidersState is MovieState.Success) {
                                val providers = (watchProvidersState as MovieState.Success).data
                                val hasProviders = !providers?.flatrate.isNullOrEmpty() ||
                                        !providers?.rent.isNullOrEmpty() ||
                                        !providers?.buy.isNullOrEmpty()

                                if (hasProviders) {
                                    WatchProvidersSection(watchProvidersState = watchProvidersState)
                                    Spacer(modifier = Modifier.height(20.dp))
                                }
                            } else if (watchProvidersState is MovieState.Loading) {
                                // Show loader and space only while actively loading
                                WatchProvidersSection(watchProvidersState = watchProvidersState)
                                Spacer(modifier = Modifier.height(20.dp))
                            }

                            // 3. Cast Section
                            if (state.castList.isNotEmpty()) {
                                CastMediaSection(state.castList)
                                Spacer(modifier = Modifier.height(20.dp))
                            }

                            // 4. Similar Movies Section
                            if (state.similarMovies != null && state.similarMovies.results.isNotEmpty()) {
                                SimilarMediaSection(state.similarMovies, navController)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── EXPANDABLE OVERVIEW COMPOSABLE ──
@Composable
fun ExpandableOverviewSection(overview: String) {
    val hapticController = rememberHapticController()
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessLow))
    ) {
        Text(
            text = "STORYLINE",
            color = TmdbCinematicTheme.TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = overview,
            color = TmdbCinematicTheme.TextSecondary,
            fontSize = 14.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight.Normal,
            maxLines = if (isExpanded) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis
        )

        //Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    hapticController.trigger(AppHaptic.SegmentTick)
                    isExpanded = !isExpanded
                }
                .padding(vertical = 4.dp, horizontal = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isExpanded) "Show Less" else "Read More",
                color = TmdbCinematicTheme.CoralAccent,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Expand Text",
                tint = TmdbCinematicTheme.CoralAccent,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}@Composable
fun WatchProvidersSection(
    watchProvidersState: MovieState<CountryWatchProvidersDTO>
) {
    val context = LocalContext.current
    val hapticController = rememberHapticController()

    when (watchProvidersState) {
        is MovieState.Loading -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = TmdbCinematicTheme.CoralAccent,
                    strokeWidth = 2.dp
                )
                Text(
                    text = "Loading streaming availability...",
                    color = TmdbCinematicTheme.TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        is MovieState.Error -> {
            // Silently hide if no providers available
        }

        is MovieState.Success -> {
            val countryProviders = watchProvidersState.data
            val webLink = countryProviders?.link

            val streamList = countryProviders?.flatrate ?: emptyList()
            val rentList = countryProviders?.rent ?: emptyList()
            val buyList = countryProviders?.buy ?: emptyList()

            if (streamList.isNotEmpty() || rentList.isNotEmpty() || buyList.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF0F1523).copy(alpha = 0.6f))
                        .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "WHERE TO WATCH",
                        color = TmdbCinematicTheme.TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.2.sp
                    )

                    // Helper launcher function for deep links
                    val openStreamingPage: () -> Unit = {
                        webLink?.let { url ->
                            hapticController.trigger(AppHaptic.Click)
                            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                            context.startActivity(intent)
                        }
                    }

                    // 1. STREAM SECTION
                    if (streamList.isNotEmpty()) {
                        ProviderCategoryGroup(
                            title = "Stream",
                            providers = streamList,
                            onProviderClick = openStreamingPage
                        )
                    }

                    // 2. RENT SECTION
                    if (rentList.isNotEmpty()) {
                        ProviderCategoryGroup(
                            title = "Rent",
                            providers = rentList,
                            onProviderClick = openStreamingPage
                        )
                    }

                    // 3. BUY SECTION
                    if (buyList.isNotEmpty()) {
                        ProviderCategoryGroup(
                            title = "Buy",
                            providers = buyList,
                            onProviderClick = openStreamingPage
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProviderCategoryGroup(
    title: String,
    providers: List<ProviderItemDTO>,
    onProviderClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(providers, key = { it.providerId }) { provider ->
                ProviderLogoCard(
                    provider = provider,
                    onClick = onProviderClick
                )
            }
        }
    }
}

@Composable
private fun ProviderLogoCard(
    provider: ProviderItemDTO,
    onClick: () -> Unit
) {
    val logoUrl = provider.logoPath?.let { BASE_POSTER_IMAGE_URL + it } ?: ""

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(52.dp)
            .clickable { onClick() }
    ) {
        Card(
            modifier = Modifier
                .size(52.dp)
                .shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(14.dp),
                    spotColor = Color.Black.copy(alpha = 0.5f)
                ),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2638)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(14.dp)
                    )
            ) {
                AsyncImage(
                    model = logoUrl,
                    contentDescription = provider.providerName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(14.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = provider.providerName,
            color = TmdbCinematicTheme.TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/*ENDED Provider Code */
@Composable
fun DisplayMovieData(
    moviesInfo: MovieDetailsDTO,
    navController: NavHostController,
    watchListViewModel: WatchListViewModel = koinViewModel(),
    continueWatchingViewModel: ContinueWatchingViewModel = koinViewModel(),
    movieDetailsViewModel: MovieDetailsViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val hapticController = rememberHapticController()
    val date = remember { SimpleDateFormat.getDateInstance().format(Date()) }

    LaunchedEffect(moviesInfo.id) {
        watchListViewModel.exist(moviesInfo.id)
    }
    val exist = watchListViewModel.exist.value

    val videoState by movieDetailsViewModel.videoResponses.collectAsState()
    var showTrailerDialog by remember { mutableStateOf(false) }

    val formattedGenres = remember(moviesInfo.genres) {
        moviesInfo.genres.joinToString(", ") { it.name }
    }

    val myListMovie = remember(moviesInfo, date, formattedGenres) {
        WatchListModel(
            mediaId = moviesInfo.id,
            title = moviesInfo.title,
            posterPath = moviesInfo.posterPath,
            backdropPath = moviesInfo.backdropPath,
            releaseDate = moviesInfo.releaseDate,
            rating = moviesInfo.voteAverage,
            runtime = moviesInfo.runtime,
            overview = moviesInfo.overview.orEmpty(),
            genres = formattedGenres,
            originalLanguage = moviesInfo.spokenLanguages.firstOrNull()?.name ?: "English",
            mediaType = "movie",
            addedOn = date
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(480.dp)
    ) {
        AsyncImage(
            model = Constants.BASE_BACKDROP_IMAGE_URL + moviesInfo.backdropPath,
            contentDescription = "Backdrop",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.4f),
                            Color.Transparent,
                            Color(0xFF0F0E17).copy(alpha = 0.85f),
                            Color(0xFF0F0E17)
                        )
                    )
                )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 44.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    hapticController.trigger(AppHaptic.Click)
                    navController.popBackStack()
                },
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.4f))
                    .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier
                        .size(16.dp)
                        .padding(start = 4.dp)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                IconButton(
                    onClick = {
                        if (exist != 0) {
                            hapticController.trigger(AppHaptic.ToggleOff)
                            watchListViewModel.removeFromWatchList(mediaId = moviesInfo.id)
                            Toast.makeText(context, "Removed from Watchlist", Toast.LENGTH_SHORT).show()
                        } else {
                            hapticController.trigger(AppHaptic.ToggleOn)
                            watchListViewModel.addToWatchList(myListMovie)
                            Toast.makeText(context, "Added to Watchlist", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.4f))
                        .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (exist != 0) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        tint = if (exist != 0) TmdbCinematicTheme.CoralAccent else Color.White,
                        contentDescription = "Watchlist",
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = {
                        hapticController.trigger(AppHaptic.Click)
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Check out ${moviesInfo.title} on TMDB App!"
                            )
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Movie"))
                    },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.4f))
                        .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        tint = Color.White,
                        contentDescription = "Share",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = moviesInfo.title.uppercase(),
                color = TmdbCinematicTheme.TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (moviesInfo.genres.isNotEmpty()) {
                Text(
                    text = moviesInfo.genres.joinToString(" | ") { it.name },
                    color = TmdbCinematicTheme.TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${String.format("%.1f", moviesInfo.voteAverage)}/10",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "${moviesInfo.runtime ?: 0} min",
                    color = TmdbCinematicTheme.TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.White.copy(alpha = 0.15f))
                        .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "PG-13",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFF00C6FF), Color(0xFF0072FF))
                        )
                    )
                    .clickable {
                        hapticController.trigger(AppHaptic.Confirm)

                        val runtimeMs = (moviesInfo.runtime ?: 120) * 60 * 1000L
                        continueWatchingViewModel.saveProgress(
                            mediaId = moviesInfo.id,
                            title = moviesInfo.title,
                            posterPath = moviesInfo.posterPath,
                            backdropPath = moviesInfo.backdropPath,
                            currentPositionMs = (runtimeMs * 0.25).toLong(),
                            totalDurationMs = runtimeMs,
                            releaseDate = moviesInfo.releaseDate,
                            rating = moviesInfo.voteAverage,
                            mediaType = "movie"
                        )

                        movieDetailsViewModel.fetchMovieVideos(moviesInfo.id)
                        showTrailerDialog = true
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "WATCH NOW",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }

    if (showTrailerDialog) {
        VideoSelectionDialog(
            videoState = videoState,
            onDismiss = { showTrailerDialog = false },
            onVideoSelected = { video ->
                hapticController.trigger(AppHaptic.Click)
                showTrailerDialog = false
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    "https://www.youtube.com/watch?v=${video.key}".toUri()
                )
                context.startActivity(intent)
            }
        )
    }
}

@Composable
fun CastMediaSection(castList: List<Cast>) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "CAST",
                color = TmdbCinematicTheme.TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(castList) { cast ->
                CastMemberAvatarItem(cast)
            }
        }
    }
}

@Composable
fun CastMemberAvatarItem(cast: Cast) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Box(
            modifier = Modifier
                .size(75.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.05f))
                .border(2.dp, MaterialTheme.colorScheme.primary.copy(0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (!cast.profilePath.isNullOrBlank()) {
                Image(
                    painter = rememberAsyncImagePainter(BASE_POSTER_IMAGE_URL + cast.profilePath),
                    contentDescription = cast.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.user),
                    contentDescription = "Cast Placeholder",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = cast.name,
            color = TmdbCinematicTheme.TextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        cast.character?.let { character ->
            Text(
                text = "as $character",
                color = TmdbCinematicTheme.TextSecondary,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun SimilarMediaSection(
    media: MovieResponse,
    navController: NavHostController
) {
    val hapticController = rememberHapticController()
    val mediaList = media.results

    Column {
        Text(
            text = "SIMILAR MOVIES",
            color = TmdbCinematicTheme.TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(mediaList.size) { index ->
                HomeSmallThumb(
                    BASE_POSTER_IMAGE_URL + mediaList[index].posterPath
                ) {
                    hapticController.trigger(AppHaptic.Click)
                    navController.navigate("${MovieAppScreen.MOVIE_HOME_DETAILS.route}/${mediaList[index].id}/movie")
                }
            }
        }
    }
}