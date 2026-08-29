/*
 * Copyright (c) 2026 Dinesh2510
 * File : TmdbHomeScreen.kt
 * Project : TMDB Ktor
 * Module : TMDB_Ktor.app.main
 * Created on : 2026-08-22 15:27
 * Last modified: 2026-08-27 12:00
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

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.app.movieapp.data.local.ContinueWatchingModel
import com.app.movieapp.data.local.WatchListModel
import com.app.movieapp.data.viewmodel.ContinueWatchingViewModel
import com.app.movieapp.data.viewmodel.HomeFeedUIState
import com.app.movieapp.data.viewmodel.HomeViewModel
import com.app.movieapp.data.viewmodel.WatchListViewModel
import com.app.movieapp.graph.MovieAppScreen
import com.app.movieapp.models.Genre
import com.app.movieapp.models.Movies
import com.app.movieapp.screens.components.CinematicErrorState
import com.app.movieapp.ui.theme.TmdbCinematicTheme
import com.app.movieapp.utlis.AppHaptic
import com.app.movieapp.utlis.CenteredCircularProgressIndicator
import com.app.movieapp.utlis.Constants.Companion.BASE_BACKDROP_IMAGE_URL
import com.app.movieapp.utlis.Constants.Companion.BASE_POSTER_IMAGE_URL
import com.app.movieapp.utlis.Constants.Companion.nowPlayingAllListScreen
import com.app.movieapp.utlis.Constants.Companion.popularAllListScreen
import com.app.movieapp.utlis.Constants.Companion.upcomingListScreen
import com.app.movieapp.utlis.GenreImageMapper
import com.app.movieapp.utlis.netflixFamily
import com.app.movieapp.utlis.rememberHapticController
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.random.Random

// --- Helper Extension for Watchlist Conversion ---
fun Movies.toWatchListModel(): WatchListModel {
    val date = SimpleDateFormat.getDateInstance().format(Date())
    return WatchListModel(
        mediaId = this.id,
        title = this.displayTitle,
        posterPath = this.posterPath,
        backdropPath = this.backdropPath,
        releaseDate = this.displayReleaseDate,
        rating = this.voteAverage,
        runtime = this.runtime ?: 0,
        overview = this.overview,
        genres = this.genres?.joinToString(", ") { it.name } ?: "",
        originalLanguage = this.originalLanguage.ifEmpty { "English" },
        mediaType = getMediaType(),
        addedOn = date
    )
}

// Dynamic media_type resolver
 fun Movies.getMediaType(): String {
    return when {
        this.mediaType != null -> this.mediaType
        this.name != null -> "tv"
        else -> "movie"
    }
}

// Navigation Helper
private fun NavController.navigateToDetails(movie: Movies) {
    val type = movie.getMediaType()
    this.navigate("${MovieAppScreen.MOVIE_HOME_DETAILS.route}/${movie.id}/$type")
}

@Composable
fun TmdbHomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel(),
    continueWatchingViewModel: ContinueWatchingViewModel = koinViewModel(),
    watchListViewModel: WatchListViewModel = koinViewModel()
) {
    val homeState by viewModel.homeFeedState.collectAsState()
    val continueWatchingList by continueWatchingViewModel.continueWatchingList.collectAsState()

    // Observe Watchlist
    val watchListFlow by watchListViewModel.myMovieData
    val watchListItems by watchListFlow.collectAsState(initial = emptyList())
    val savedMediaIds = remember(watchListItems) { watchListItems.map { it.mediaId }.toSet() }

    val context = LocalContext.current
    val haptics = rememberHapticController()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TmdbCinematicTheme.AppBackgroundGradient)
    ) {
        when (val state = homeState) {
            is HomeFeedUIState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CenteredCircularProgressIndicator()
                }
            }
            is HomeFeedUIState.Error -> {
                CinematicErrorState(
                    errorMessage = stringResource(id = state.messageRes),
                    onRetryClick = { viewModel.fetchAllHomeData() }
                )
            }
            is HomeFeedUIState.Success -> {
                val discoverMovies = state.discoverMovies?.results ?: emptyList()
                val trendingAllMovies = state.trendingAll?.results?.take(10) ?: emptyList()
                val nowPlayingMovies = state.nowPlayingMovies?.results ?: emptyList()
                val trendingMovies = state.trendingMovies?.results ?: emptyList()
                val upcomingMovies = state.upcomingMovies?.results ?: emptyList()
                val genres = state.genres?.genres ?: emptyList()

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 120.dp)
                ) {
                    // 1. Home Header
                    item {
                        HomeHeader(
                            onSearchClick = {
                                navController.navigate(MovieAppScreen.MOVIE_AI.route)
                                haptics.trigger(AppHaptic.Click)
                            }
                        )
                    }

                    // 2. Hero Featured Movies Pager
                    if (discoverMovies.isNotEmpty()) {
                        item {
                            HeroTrendingPager(
                                movies = discoverMovies.take(5),
                                onMovieClick = { movie ->
                                    navController.navigateToDetails(movie)
                                }
                            )
                        }
                    }

                    // 3. CONTINUE WATCHING SECTION
                    if (continueWatchingList.isNotEmpty()) {
                        item {
                            ContinueWatchSection(
                                continueWatchingList = continueWatchingList,
                                navController = navController,
                                onRemoveClick = { mediaId ->
                                    continueWatchingViewModel.removeProgress(mediaId)
                                }
                            )
                        }
                    }

                    // 4. TRENDING 10 Section (Movies & TV Shows)
                    if (trendingAllMovies.isNotEmpty()) {
                        item {
                            ModernTop10Section(
                                top10List = trendingAllMovies,
                                onMovieClick = { movie ->
                                    navController.navigateToDetails(movie)
                                }
                            )
                        }
                    }

                    // 5. Upcoming Spotlight
                    if (upcomingMovies.isNotEmpty()) {
                        item {
                            LandscapeMoviesSection(
                                sectionTitle = "UPCOMING SPOTLIGHT",
                                movies = upcomingMovies,
                                savedMediaIds = savedMediaIds,
                                onBookmarkClick = { movie, isBookmarked ->
                                    haptics.trigger(AppHaptic.Click)
                                    if (isBookmarked) {
                                        watchListViewModel.removeFromWatchList(movie.id)
                                        Toast.makeText(context, "Removed from Watchlist", Toast.LENGTH_SHORT).show()
                                    } else {
                                        watchListViewModel.addToWatchList(movie.toWatchListModel())
                                        Toast.makeText(context, "Added to Watchlist", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                onSeeAllClick = {
                                    navController.navigate("${MovieAppScreen.MOVIE_SEE_ALL.route}/$upcomingListScreen")
                                },
                                onMovieClick = { movie ->
                                    navController.navigateToDetails(movie)
                                }
                            )
                        }
                    }

                    // 6. Explore by Genre Section
                    if (genres.isNotEmpty()) {
                        item {
                            TmdbCategoryExploreSection(
                                genres = genres,
                                onGenreClick = { genre ->
                                    navController.navigate("${MovieAppScreen.MOVIE_GENRE_WISE.route}/${genre.id}/${genre.name}")
                                }
                            )
                        }
                    }

                    // 7. "Now Playing in Theaters" Section
                    if (nowPlayingMovies.isNotEmpty()) {
                        item {
                            SectionHeader(
                                title = "Now Playing in Theaters",
                                onSeeAllClick = {
                                    navController.navigate("${MovieAppScreen.MOVIE_SEE_ALL.route}/$nowPlayingAllListScreen")
                                }
                            )
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                items(nowPlayingMovies, key = { it.id }) { movie ->
                                    MoviePosterGridCard(
                                        movie = movie,
                                        onMovieClick = {
                                            navController.navigateToDetails(movie)
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // 8. "Trending This Week" Section
                    if (trendingMovies.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                            SectionHeader(
                                title = "Trending This Week",
                                onSeeAllClick = {
                                    navController.navigate("${MovieAppScreen.MOVIE_SEE_ALL.route}/$popularAllListScreen")
                                }
                            )
                        }

                        itemsIndexed(trendingMovies, key = { index, movie -> "${movie.id}_$index" }) { _, movie ->
                            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                                MovieDetailedRowCard(
                                    item = movie,
                                    onMovieClick = {
                                        navController.navigateToDetails(movie)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContinueWatchSection(
    continueWatchingList: List<ContinueWatchingModel>,
    navController: NavController,
    onRemoveClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        SectionHeader(
            title = "Continue Watching",
            onSeeAllClick = {}
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(continueWatchingList, key = { it.mediaId }) { item ->
                ContinueWatchingCard(
                    item = item,
                    onCardClick = {
                        val type = item.mediaType.ifBlank { "movie" }
                        navController.navigate(
                            "${MovieAppScreen.MOVIE_HOME_DETAILS.route}/${item.mediaId}/$type"
                        )
                    },
                    onRemoveClick = {
                        onRemoveClick(item.mediaId)
                    }
                )
            }
        }
    }
}

// --- Home Header ---
@Composable
fun HomeHeader(onSearchClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 8.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Welcome Back 👋",
                color = TmdbCinematicTheme.TextSecondary,
                fontSize = 12.sp
            )
            Text(
                text = "Discover Movies & Shows",
                color = TmdbCinematicTheme.TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        IconButton(
            onClick = onSearchClick,
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(TmdbCinematicTheme.GlassSurface)
                .border(1.dp, TmdbCinematicTheme.GlassBorderGradient, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = "Search",
                tint = TmdbCinematicTheme.TextPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// --- Hero Banner Pager ---
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeroTrendingPager(
    movies: List<Movies>,
    modifier: Modifier = Modifier,
    autoScrollDurationMs: Long = 3500L,
    onMovieClick: (Movies) -> Unit
) {
    if (movies.isEmpty()) return

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { movies.size }
    )

    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    LaunchedEffect(isDragged, movies.size) {
        if (!isDragged && movies.size > 1) {
            while (true) {
                delay(autoScrollDurationMs)
                val nextPage = (pagerState.currentPage + 1) % movies.size
                pagerState.animateScrollToPage(
                    page = nextPage,
                    animationSpec = tween(durationMillis = 800)
                )
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 12.dp,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val movie = movies[page]
            val backdropUrl = "$BASE_BACKDROP_IMAGE_URL${movie.backdropPath}"

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { onMovieClick(movie) },
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131927))
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = backdropUrl,
                        contentDescription = movie.displayTitle,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.3f),
                                        Color.Black.copy(alpha = 0.9f)
                                    ),
                                    startY = 50f
                                )
                            )
                    )

                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = movie.displayTitle,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "★ ${String.format("%.1f", movie.voteAverage)} • ${movie.displayReleaseDate.take(4)}",
                                color = Color(0xFFCBD5E1),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(Color(0xFFFF5252), Color(0xFFFF7A00))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }

        if (movies.size > 1) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(movies.size.coerceAtMost(8)) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .size(
                                width = if (isSelected) 18.dp else 6.dp,
                                height = 6.dp
                            )
                            .clip(CircleShape)
                            .background(
                                if (isSelected) TmdbCinematicTheme.CoralAccent else Color.White.copy(alpha = 0.25f)
                            )
                    )
                }
            }
        }
    }
}

// --- TRENDING 10 SECTION ---
@Composable
fun ModernTop10Section(
    top10List: List<Movies>,
    onMovieClick: (Movies) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "TRENDING",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
            )
        }

        LazyRow(
            contentPadding = PaddingValues(start = 12.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(top10List, key = { index, item -> "${item.id}_$index" }) { index, movie ->
                ModernTop10Card(
                    rank = index + 1,
                    movie = movie,
                    onMovieClick = onMovieClick
                )
            }
        }
    }
}

@Composable
fun ModernTop10Card(
    rank: Int,
    movie: Movies,
    onMovieClick: (Movies) -> Unit,
    modifier: Modifier = Modifier
) {
    val posterUrl = "$BASE_POSTER_IMAGE_URL${movie.posterPath}"

    Box(
        modifier = modifier
            .width(185.dp)
            .height(245.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onMovieClick(movie) }
    ) {
        Text(
            text = "$rank",
            style = TextStyle(
                fontSize = 110.sp,
                fontWeight = FontWeight.Black,
                color = Color.White.copy(alpha = 0.12f),
                drawStyle = Stroke(width = 6f)
            ),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-6).dp, y = 14.dp)
        )

        Card(
            modifier = Modifier
                .width(148.dp)
                .height(220.dp)
                .align(Alignment.TopEnd)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(20.dp),
                    spotColor = Color.Black.copy(alpha = 0.6f)
                ),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        1.2.dp,
                        TmdbCinematicTheme.GlassBorderGradient,
                        RoundedCornerShape(20.dp)
                    )
            ) {
                AsyncImage(
                    model = posterUrl,
                    contentDescription = movie.displayTitle,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.3f),
                                    Color.Black.copy(alpha = 0.92f)
                                )
                            )
                        )
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.60f))
                        .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                        .padding(horizontal = 7.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = String.format("%.1f", movie.voteAverage),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = movie.displayTitle,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(1.dp))
                        Text(
                            text = movie.displayReleaseDate.take(4),
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 10.sp,
                            maxLines = 1
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(TmdbCinematicTheme.PrimaryActionGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

// --- EXPLORE BY GENRE ---
@Composable
fun TmdbCategoryExploreSection(
    genres: List<Genre>,
    onGenreClick: (Genre) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(
            text = "EXPLORE BY GENRE",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 12.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(genres, key = { it.id!! }) { genre ->
                val imageUrl = GenreImageMapper.getImageUrlForGenre(genre.id)
                CategoryImageCard(
                    categoryName = genre.name,
                    imageUrl = imageUrl,
                    onCategoryClick = { onGenreClick(genre) }
                )
            }
        }
    }
}

@Composable
fun CategoryImageCard(
    categoryName: String,
    imageUrl: String,
    onCategoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(130.dp)
            .height(80.dp)
            .clickable { onCategoryClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = TmdbCinematicTheme.GlassSurface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(1.dp, TmdbCinematicTheme.GlassBorderGradient, RoundedCornerShape(16.dp))
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = categoryName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.30f),
                                Color.Black.copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            Text(
                text = categoryName,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
            )
        }
    }
}

// --- LANDSCAPE SECTION ---
@Composable
fun LandscapeMoviesSection(
    sectionTitle: String,
    movies: List<Movies>,
    savedMediaIds: Set<Int>,
    onBookmarkClick: (Movies, Boolean) -> Unit,
    onSeeAllClick: () -> Unit,
    onMovieClick: (Movies) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        SectionHeader(title = sectionTitle, onSeeAllClick = onSeeAllClick)

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(movies, key = { it.id }) { movie ->
                val isBookmarked = savedMediaIds.contains(movie.id)
                LandscapeMovieCard(
                    movie = movie,
                    isBookmarked = isBookmarked,
                    onBookmarkClick = { onBookmarkClick(movie, isBookmarked) },
                    onMovieClick = onMovieClick
                )
            }
        }
    }
}

@Composable
fun LandscapeMovieCard(
    movie: Movies,
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit,
    onMovieClick: (Movies) -> Unit,
    modifier: Modifier = Modifier
) {
    val backdropUrl = "$BASE_BACKDROP_IMAGE_URL${movie.backdropPath}"

    Card(
        modifier = modifier
            .width(240.dp)
            .height(135.dp)
            .clickable { onMovieClick(movie) },
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(18.dp)
                )
        ) {
            AsyncImage(
                model = backdropUrl,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(18.dp))
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.3f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.9f)
                            )
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.65f))
                    .padding(horizontal = 7.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Rating",
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(11.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = String.format("%.1f", movie.voteAverage),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable { onBookmarkClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = "Save",
                    tint = if (isBookmarked) TmdbCinematicTheme.CoralAccent else Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    movie.displayTitle.let {
                        Text(
                            text = it,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    if (!movie.genres.isNullOrEmpty()) {
                        Text(
                            text = movie.genres.joinToString(" | ") { it.name },
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xFFFF5252), Color(0xFFFF7A00))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ContinueWatchingCard(
    item: ContinueWatchingModel,
    onCardClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    val imageUrl = "$BASE_BACKDROP_IMAGE_URL${item.backdropPath ?: item.posterPath}"
    val cardShape = RoundedCornerShape(16.dp)

    Card(
        modifier = Modifier
            .width(240.dp)
            .height(135.dp)
            .clip(cardShape)
            .clickable { onCardClick() },
        shape = cardShape,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131927)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = item.title,
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
                                Color.Black.copy(alpha = 0.9f)
                            )
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.Center)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.6f))
                    .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Resume",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable { onRemoveClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove",
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(14.dp)
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.title,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = netflixFamily,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${item.remainingMinutes}m left",
                        color = TmdbCinematicTheme.CoralAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { item.progressFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    color = TmdbCinematicTheme.CoralAccent,
                    trackColor = Color.White.copy(alpha = 0.2f),
                    strokeCap = StrokeCap.Round
                )
            }
        }
    }
}

// --- STANDARD COMPONENTS ---
@Composable
fun SectionHeader(title: String, onSeeAllClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "See all",
            color = TmdbCinematicTheme.CoralAccent,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { onSeeAllClick() }
        )
    }
}

@Composable
fun MoviePosterGridCard(
    movie: Movies,
    onMovieClick: () -> Unit
) {
    val posterUrl = "$BASE_POSTER_IMAGE_URL${movie.posterPath}"

    Column(
        modifier = Modifier
            .width(130.dp)
            .clickable { onMovieClick() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.7f),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = posterUrl,
                    contentDescription = movie.displayTitle,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.65f))
                        .padding(horizontal = 6.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = String.format("%.1f", movie.voteAverage),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = movie.displayTitle,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun MovieDetailedRowCard(
    item: Movies,
    modifier: Modifier = Modifier,
    onMovieClick: () -> Unit
) {
    val posterUrl = "$BASE_POSTER_IMAGE_URL${item.posterPath}"
    val cardShape = RoundedCornerShape(16.dp)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(132.dp)
            .clip(cardShape)
            .clickable { onMovieClick() },
        shape = cardShape,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF131927).copy(alpha = 0.85f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.15f),
                            Color.White.copy(alpha = 0.03f)
                        )
                    ),
                    shape = cardShape
                )
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(2f / 3f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1E2638))
                ) {
                    AsyncImage(
                        model = posterUrl,
                        contentDescription = item.displayTitle,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.displayTitle,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.White,
                                    letterSpacing = 0.2.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        if (item.overview.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.overview,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    color = Color(0xFF94A3B8)
                                ),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFFFB800).copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Star,
                                contentDescription = "Rating",
                                tint = Color(0xFFFFB800),
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = String.format("%.1f", item.voteAverage),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFC72C),
                                    fontSize = 11.sp
                                )
                            )
                        }

                        val releaseYear = item.displayReleaseDate.take(4)
                        if (releaseYear.isNotBlank()) {
                            MetadataPill(text = releaseYear)
                        }

                        if (item.originalLanguage.isNotBlank()) {
                            MetadataPill(text = item.originalLanguage.uppercase())
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MetadataPill(text: String) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = Color.White.copy(alpha = 0.08f)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium,
                color = Color(0xFFCBD5E1),
                fontSize = 10.sp
            ),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        )
    }
}