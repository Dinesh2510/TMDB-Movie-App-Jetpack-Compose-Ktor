/*
 * Copyright (c) 2026 Dinesh2510
 * File : TvDetailsScreen.kt
 * Project : TMDB Ktor
 * Module : TMDB_Ktor.app.main
 * Created on : 2026-08-27 22:34
 * Last modified: 2026-08-29 14:15
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.app.movieapp.R
import com.app.movieapp.data.local.WatchListModel
import com.app.movieapp.data.remote.response.VideoResponse
import com.app.movieapp.data.viewmodel.ContinueWatchingViewModel
import com.app.movieapp.data.viewmodel.TvDetailsUIState
import com.app.movieapp.data.viewmodel.TvDetailsViewModel
import com.app.movieapp.data.viewmodel.WatchListViewModel
import com.app.movieapp.graph.MovieAppScreen
import com.app.movieapp.models.Cast
import com.app.movieapp.models.CountryWatchProvidersDTO
import com.app.movieapp.models.EpisodeDTO
import com.app.movieapp.models.GenreDTO
import com.app.movieapp.models.ProviderItemDTO
import com.app.movieapp.models.SeasonSummaryDTO
import com.app.movieapp.models.TvShowDetailsDTO
import com.app.movieapp.screens.components.CinematicErrorState
import com.app.movieapp.screens.components.HomeSmallThumb
import com.app.movieapp.screens.components.VideoSelectionDialog
import com.app.movieapp.ui.theme.TmdbCinematicTheme
import com.app.movieapp.utlis.AppHaptic
import com.app.movieapp.utlis.Constants.Companion.BASE_BACKDROP_IMAGE_URL
import com.app.movieapp.utlis.Constants.Companion.BASE_POSTER_IMAGE_URL
import com.app.movieapp.utlis.MovieState
import com.app.movieapp.utlis.rememberHapticController
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date

enum class TvDetailsTab {
    DETAILS, EPISODES, RELATED
}

@Composable
fun TvDetailsScreen(
    tvId: Int,
    navController: NavHostController,
    viewModel: TvDetailsViewModel = koinViewModel(),
    watchListViewModel: WatchListViewModel = koinViewModel(),
    continueWatchingViewModel: ContinueWatchingViewModel = koinViewModel()
) {
    val hapticController = rememberHapticController()

    LaunchedEffect(tvId) {
        viewModel.loadTvShowDetails(tvId)
        viewModel.fetchTvWatchProviders(tvId)
        watchListViewModel.exist(tvId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val selectedSeasonNumber by viewModel.selectedSeasonNumber.collectAsState()
    val episodesState by viewModel.episodesState.collectAsState()
    val videoState by viewModel.videoResponses.collectAsState()
    val similarTvState by viewModel.similarTvState.collectAsState()
    val watchProvidersState by viewModel.watchProvidersState.collectAsState()
    val exist = watchListViewModel.exist.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TmdbCinematicTheme.AppBackgroundGradient)
    ) {
        when (val state = uiState) {
            is TvDetailsUIState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = TmdbCinematicTheme.AccentCoral)
                }
            }

            is TvDetailsUIState.Error -> {
                CinematicErrorState(
                    errorMessage = stringResource(id = state.messageRes),
                    onRetryClick = {
                        hapticController.trigger(AppHaptic.Click)
                        viewModel.loadTvShowDetails(tvId)
                        viewModel.fetchTvWatchProviders(tvId)
                    }
                )
            }

            is TvDetailsUIState.Success -> {
                TvDetailsContent(
                    tvDetails = state.tvDetails,
                    availableSeasons = state.availableSeasons,
                    selectedSeasonNumber = selectedSeasonNumber,
                    episodesState = episodesState,
                    similarTvState = similarTvState,
                    watchProvidersState = watchProvidersState,
                    videoState = videoState,
                    navController = navController,
                    isWatchlisted = exist != 0,
                    onWatchlistToggle = { item ->
                        if (exist != 0) {
                            hapticController.trigger(AppHaptic.ToggleOff)
                            watchListViewModel.removeFromWatchList(item.mediaId)
                        } else {
                            hapticController.trigger(AppHaptic.ToggleOn)
                            watchListViewModel.addToWatchList(item)
                        }
                    },
                    onSaveProgress = { progressData ->
                        continueWatchingViewModel.saveProgress(
                            mediaId = progressData.mediaId,
                            title = progressData.title,
                            posterPath = progressData.posterPath,
                            backdropPath = progressData.backdropPath,
                            currentPositionMs = progressData.currentPositionMs,
                            totalDurationMs = progressData.totalDurationMs,
                            releaseDate = progressData.releaseDate,
                            rating = progressData.rating,
                            mediaType = "tv"
                        )
                    },
                    onFetchVideos = { id -> viewModel.fetchTvShowVideos(id) },
                    onSeasonSelect = { seasonNum ->
                        hapticController.trigger(AppHaptic.SegmentTick)
                        viewModel.selectSeason(seasonNum)
                    }
                )
            }
        }
    }
}

@Composable
private fun TvDetailsContent(
    tvDetails: TvShowDetailsDTO,
    availableSeasons: List<SeasonSummaryDTO>,
    selectedSeasonNumber: Int,
    episodesState: MovieState<List<EpisodeDTO>>,
    similarTvState: MovieState<List<TvShowDetailsDTO>>,
    watchProvidersState: MovieState<CountryWatchProvidersDTO>,
    videoState: MovieState<VideoResponse>,
    navController: NavHostController,
    isWatchlisted: Boolean,
    onWatchlistToggle: (WatchListModel) -> Unit,
    onSaveProgress: (ProgressSaveData) -> Unit,
    onFetchVideos: (Int) -> Unit,
    onSeasonSelect: (Int) -> Unit
) {
    val hapticController = rememberHapticController()
    var activeTab by remember { mutableStateOf(TvDetailsTab.EPISODES) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    val activeSeason = availableSeasons.find { it.seasonNumber == selectedSeasonNumber }
        ?: availableSeasons.firstOrNull()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // 1. Hero Section
        item {
            PrimeTvHeroHeader(
                tvDetails = tvDetails,
                navController = navController,
                videoState = videoState,
                isWatchlisted = isWatchlisted,
                onWatchlistToggle = onWatchlistToggle,
                onSaveProgress = onSaveProgress,
                onFetchVideos = onFetchVideos
            )
        }

        // 2. Overview & Metadata
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                if (tvDetails.genres.isNotEmpty()) {
                    Text(
                        text = tvDetails.genres.joinToString("  •  ") { it.name },
                        color = TmdbCinematicTheme.TextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                tvDetails.overview?.let { overview ->
                    if (overview.isNotBlank()) {
                        ExpandableOverviewSection(overview = overview)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                HorizontalDivider(color = Color.White.copy(alpha = 0.12f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Releases", color = TmdbCinematicTheme.TextSecondary, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = tvDetails.firstAirDate?.take(4) ?: "N/A",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(30.dp)
                            .background(Color.White.copy(alpha = 0.15f))
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Seasons", color = TmdbCinematicTheme.TextSecondary, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${tvDetails.numberOfSeasons}",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                HorizontalDivider(color = Color.White.copy(alpha = 0.12f))
                Spacer(modifier = Modifier.height(16.dp))

                // Watch Providers (Streaming Availability) — Conditional spacing
                if (watchProvidersState is MovieState.Success) {
                    val providers = watchProvidersState.data
                    val hasProviders = !providers?.flatrate.isNullOrEmpty() ||
                            !providers?.rent.isNullOrEmpty() ||
                            !providers?.buy.isNullOrEmpty()

                    if (hasProviders) {
                        TvWatchProvidersSection(watchProvidersState = watchProvidersState)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                } else if (watchProvidersState is MovieState.Loading) {
                    TvWatchProvidersSection(watchProvidersState = watchProvidersState)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // 3. Season Selector Dropdown Button
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(TmdbCinematicTheme.GlassSurface)
                        .border(1.dp, TmdbCinematicTheme.GlassBorderGradient, RoundedCornerShape(12.dp))
                        .clickable {
                            hapticController.trigger(AppHaptic.Click)
                            dropdownExpanded = true
                        }
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "${activeSeason?.name ?: "Season $selectedSeasonNumber"} (${activeSeason?.episodeCount ?: 0} episodes)",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select Season",
                            tint = Color.White
                        )
                    }
                }

                DropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false },
                    modifier = Modifier
                        .background(Color(0xFF1B192B))
                        .border(1.dp, TmdbCinematicTheme.GlassBorderGradient, RoundedCornerShape(8.dp))
                ) {
                    availableSeasons.forEach { season ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "${season.name} (${season.episodeCount} episodes)",
                                    color = if (season.seasonNumber == selectedSeasonNumber) TmdbCinematicTheme.CoralAccent else Color.White
                                )
                            },
                            onClick = {
                                onSeasonSelect(season.seasonNumber)
                                dropdownExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // 4. Tab Bar (Details | Episodes | Related)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TvDetailsTab.entries.forEach { tab ->
                    val isSelected = activeTab == tab
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable {
                                hapticController.trigger(AppHaptic.SegmentTick)
                                activeTab = tab
                            }
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = tab.name.lowercase().replaceFirstChar { it.uppercase() },
                            color = if (isSelected) Color.White else TmdbCinematicTheme.TextSecondary,
                            fontSize = 16.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(3.dp)
                                .background(
                                    if (isSelected) Color(0xFF00C6FF) else Color.Transparent,
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 5. Dynamic Tab View Content
        when (activeTab) {
            TvDetailsTab.EPISODES -> {
                when (episodesState) {
                    is MovieState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = TmdbCinematicTheme.AccentCoral)
                            }
                        }
                    }

                    is MovieState.Error -> {
                        item {
                            Text(
                                text = episodesState.message ?: "Failed to load episodes",
                                color = TmdbCinematicTheme.CoralAccent,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    is MovieState.Success -> {
                        val episodes = episodesState.data ?: emptyList()
                        items(episodes) { episode ->
                            EpisodeItemCard(
                                episode = episode,
                                onClick = {
                                    hapticController.trigger(AppHaptic.Confirm)
                                    val runtimeMs = (episode.runtime ?: 45) * 60 * 1000L
                                    onSaveProgress(
                                        ProgressSaveData(
                                            mediaId = tvDetails.id,
                                            title = "${tvDetails.name} - S${selectedSeasonNumber}E${episode.episodeNumber}",
                                            posterPath = episode.stillPath ?: tvDetails.posterPath,
                                            backdropPath = tvDetails.backdropPath,
                                            currentPositionMs = (runtimeMs * 0.25).toLong(),
                                            totalDurationMs = runtimeMs,
                                            releaseDate = episode.airDate ?: tvDetails.firstAirDate.orEmpty(),
                                            rating = episode.voteAverage
                                        )
                                    )
                                    onFetchVideos(tvDetails.id)
                                }
                            )
                        }
                    }
                }
            }

            TvDetailsTab.DETAILS -> {
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        if (tvDetails.castList.isNotEmpty()) {
                            CastMediaSectionTV(castList = tvDetails.castList)
                        }
                    }
                }
            }

            TvDetailsTab.RELATED -> {
                when (val state = similarTvState) {
                    is MovieState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = TmdbCinematicTheme.AccentCoral)
                            }
                        }
                    }

                    is MovieState.Error -> {
                        item {
                            Text(
                                text = state.message ?: "Failed to load similar shows.",
                                color = TmdbCinematicTheme.CoralAccent,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    is MovieState.Success -> {
                        item {
                            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                                if (state.data.isNotEmpty()) {
                                    SimilarTvMediaSection(
                                        mediaList = state.data,
                                        navController = navController
                                    )
                                } else {
                                    Text(
                                        text = "No similar TV shows found.",
                                        color = TmdbCinematicTheme.TextSecondary,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(vertical = 12.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── EXPANDABLE OVERVIEW COMPOSABLE ──

// ── TV WATCH PROVIDERS COMPOSABLE ──
@Composable
fun TvWatchProvidersSection(
    watchProvidersState: MovieState<CountryWatchProvidersDTO>
) {
    val context = LocalContext.current
    val hapticController = rememberHapticController()

    when (watchProvidersState) {
        is MovieState.Loading -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = TmdbCinematicTheme.CoralAccent,
                    strokeWidth = 2.dp
                )
                Text(
                    text = "Checking streaming availability...",
                    color = TmdbCinematicTheme.TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        is MovieState.Error -> {
            // Silently hide if error or missing providers
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

                    val openStreamingPage: () -> Unit = {
                        webLink?.let { url ->
                            hapticController.trigger(AppHaptic.Click)
                            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                            context.startActivity(intent)
                        }
                    }

                    if (streamList.isNotEmpty()) {
                        ProviderCategoryGroup(
                            title = "Stream",
                            providers = streamList,
                            onProviderClick = openStreamingPage
                        )
                    }

                    if (rentList.isNotEmpty()) {
                        ProviderCategoryGroup(
                            title = "Rent",
                            providers = rentList,
                            onProviderClick = openStreamingPage
                        )
                    }

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

@Composable
fun PrimeTvHeroHeader(
    tvDetails: TvShowDetailsDTO,
    navController: NavHostController,
    videoState: MovieState<VideoResponse>,
    isWatchlisted: Boolean,
    onWatchlistToggle: (WatchListModel) -> Unit,
    onSaveProgress: (ProgressSaveData) -> Unit,
    onFetchVideos: (Int) -> Unit
) {
    val context = LocalContext.current
    val hapticController = rememberHapticController()
    val date = remember { SimpleDateFormat.getDateInstance().format(Date()) }
    var showTrailerDialog by remember { mutableStateOf(false) }

    val formattedGenres = remember(tvDetails.genres) {
        tvDetails.genres.joinToString(", ") { it.name }
    }

    val watchListItem = remember(tvDetails, date, formattedGenres) {
        WatchListModel(
            mediaId = tvDetails.id,
            title = tvDetails.name,
            posterPath = tvDetails.posterPath,
            backdropPath = tvDetails.backdropPath,
            releaseDate = tvDetails.firstAirDate.orEmpty(),
            rating = tvDetails.voteAverage,
            runtime = tvDetails.episodeRunTime?.firstOrNull() ?: 45,
            overview = tvDetails.overview.orEmpty(),
            genres = formattedGenres,
            originalLanguage = tvDetails.spokenLanguages.firstOrNull()?.name ?: "English",
            mediaType = "tv",
            addedOn = date
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(370.dp)
    ) {
        AsyncImage(
            model = BASE_BACKDROP_IMAGE_URL + (tvDetails.backdropPath ?: tvDetails.posterPath),
            contentDescription = tvDetails.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.5f),
                            Color.Transparent,
                            Color(0xFF0F0E17).copy(alpha = 0.9f),
                            Color(0xFF0F0E17)
                        )
                    )
                )
        )

        IconButton(
            onClick = {
                hapticController.trigger(AppHaptic.Click)
                navController.popBackStack()
            },
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 16.dp, top = 8.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.4f))
                .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(16.dp).padding(start = 4.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(
                text = tvDetails.name.uppercase(),
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            // PRIMARY RESUME BUTTON
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .clickable {
                        hapticController.trigger(AppHaptic.Confirm)
                        val runtimeMs = 45 * 60 * 1000L
                        onSaveProgress(
                            ProgressSaveData(
                                mediaId = tvDetails.id,
                                title = tvDetails.name,
                                posterPath = tvDetails.posterPath,
                                backdropPath = tvDetails.backdropPath,
                                currentPositionMs = (runtimeMs * 0.25).toLong(),
                                totalDurationMs = runtimeMs,
                                releaseDate = tvDetails.firstAirDate.orEmpty(),
                                rating = tvDetails.voteAverage
                            )
                        )
                        onFetchVideos(tvDetails.id)
                        showTrailerDialog = true
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Resume",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Resume S1 E1",
                        color = Color.Black,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Episode Progress Bar
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    progress = { 0.35f },
                    modifier = Modifier
                        .weight(1f)
                        .height(3.dp)
                        .clip(CircleShape),
                    color = Color(0xFF00C6FF),
                    trackColor = Color.White.copy(alpha = 0.2f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "34 min left",
                    color = TmdbCinematicTheme.TextSecondary,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // QUICK ACTION BUTTONS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PrimeActionButton(
                    icon = Icons.Default.Refresh,
                    label = "Start over",
                    isHighlighted = false,
                    modifier = Modifier.weight(1f)
                ) {
                    hapticController.trigger(AppHaptic.Click)
                    onFetchVideos(tvDetails.id)
                    showTrailerDialog = true
                }

                PrimeActionButton(
                    icon = Icons.Default.Movie,
                    label = "Trailer",
                    isHighlighted = false,
                    modifier = Modifier.weight(1f)
                ) {
                    hapticController.trigger(AppHaptic.Click)
                    onFetchVideos(tvDetails.id)
                    showTrailerDialog = true
                }

                // Dynamic Watchlist Button
                PrimeActionButton(
                    icon = if (isWatchlisted) Icons.Default.Check else Icons.Default.Add,
                    label = if (isWatchlisted) "In Watchlist" else "Watchlist",
                    isHighlighted = isWatchlisted,
                    modifier = Modifier.weight(1.2f)
                ) {
                    onWatchlistToggle(watchListItem)
                    val msg = if (isWatchlisted) "Removed from Watchlist" else "Added to Watchlist"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }

                PrimeActionButton(
                    icon = Icons.Default.Share,
                    label = "Share",
                    isHighlighted = false,
                    modifier = Modifier.weight(1f)
                ) {
                    hapticController.trigger(AppHaptic.Click)
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "Check out ${tvDetails.name} on TMDB App!")
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share TV Show"))
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
fun PrimeActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isHighlighted: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(38.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isHighlighted) TmdbCinematicTheme.CoralAccent.copy(alpha = 0.25f)
                else TmdbCinematicTheme.GlassSurface
            )
            .border(
                width = 1.dp,
                color = if (isHighlighted) TmdbCinematicTheme.CoralAccent else Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isHighlighted) TmdbCinematicTheme.CoralAccent else Color.White,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                color = if (isHighlighted) TmdbCinematicTheme.CoralAccent else Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ==========================================
// CAST & SIMILAR SECTIONS
// ==========================================

@Composable
fun SimilarTvMediaSection(
    mediaList: List<TvShowDetailsDTO>,
    navController: NavHostController
) {
    val hapticController = rememberHapticController()

    Column {
        Text(
            text = "Customers also watched",
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
                    navController.navigate("${MovieAppScreen.MOVIE_HOME_DETAILS.route}/${mediaList[index].id}/tv")
                }
            }
        }
    }
}

@Composable
fun CastMediaSectionTV(castList: List<Cast>) {
    Column {
        Text(
            text = "CAST",
            color = TmdbCinematicTheme.TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(castList) { cast ->
                CastMemberAvatarItemTV(cast)
            }
        }
    }
}

@Composable
fun CastMemberAvatarItemTV(cast: Cast) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Box(
            modifier = Modifier
                .size(75.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.05f))
                .border(2.dp, TmdbCinematicTheme.GlassSurface, CircleShape),
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
fun EpisodeItemCard(
    episode: EpisodeDTO,
    onClick: () -> Unit = {}
) {
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(shape)
            .background(TmdbCinematicTheme.GlassSurface)
            .border(1.dp, TmdbCinematicTheme.GlassBorderGradient, shape)
            .clickable { onClick() }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(115.dp)
                .height(72.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Black.copy(alpha = 0.3f))
        ) {
            AsyncImage(
                model = "$BASE_POSTER_IMAGE_URL${episode.stillPath}",
                contentDescription = episode.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(TmdbCinematicTheme.PrimaryActionGradient),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play Preview",
                        tint = TmdbCinematicTheme.TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${episode.episodeNumber}. ${episode.name}",
                color = TmdbCinematicTheme.TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = episode.overview?.ifBlank { "No description available." } ?: "No description available.",
                color = TmdbCinematicTheme.TextSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// Progress data struct
data class ProgressSaveData(
    val mediaId: Int,
    val title: String,
    val posterPath: String?,
    val backdropPath: String?,
    val currentPositionMs: Long,
    val totalDurationMs: Long,
    val releaseDate: String,
    val rating: Double
)

@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp")
@Composable
private fun PrimeTvDetailsScreenPreview() {
    val mockTvDetails = TvShowDetailsDTO(
        id = 108978,
        name = "Reacher",
        overview = "Reacher interrogates Sampson, Tamara breaks into a government facility, Jacob receives some crushing news...",
        posterPath = "/sample_poster.jpg",
        backdropPath = "/sample_backdrop.jpg",
        voteAverage = 8.5,
        firstAirDate = "2026-02-03",
        numberOfSeasons = 4,
        numberOfEpisodes = 32,
        genres = listOf(GenreDTO(1, "Drama"), GenreDTO(2, "Suspense"))
    )

    val mockSeasons = listOf(
        SeasonSummaryDTO(id = 4, seasonNumber = 4, name = "Season 4", episodeCount = 8),
        SeasonSummaryDTO(id = 3, seasonNumber = 3, name = "Season 3", episodeCount = 8)
    )

    val mockEpisodes = listOf(
        EpisodeDTO(id = 1, episodeNumber = 1, name = "1. City of Brotherly Love", overview = "Reacher arrives in Philadelphia.", stillPath = "/still1.jpg")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TmdbCinematicTheme.AppBackgroundGradient)
    ) {
        TvDetailsContent(
            tvDetails = mockTvDetails,
            availableSeasons = mockSeasons,
            selectedSeasonNumber = 4,
            episodesState = MovieState.Success(mockEpisodes),
            similarTvState = MovieState.Success(emptyList()),
            watchProvidersState = MovieState.Loading,
            videoState = MovieState.Loading,
            navController = rememberNavController(),
            isWatchlisted = true,
            onWatchlistToggle = {},
            onSaveProgress = {},
            onFetchVideos = {},
            onSeasonSelect = {}
        )
    }
}