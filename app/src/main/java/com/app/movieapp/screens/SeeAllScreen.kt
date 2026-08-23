/*
 * Copyright (c) 2026 Dinesh2510
 * File : SeeAllScreen.kt
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

import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.app.movieapp.R
import com.app.movieapp.data.viewmodel.HomeViewModel
import com.app.movieapp.graph.MovieAppScreen
import com.app.movieapp.models.Movies
import com.app.movieapp.screens.componets.ErrorStrip
import com.app.movieapp.ui.theme.TmdbCinematicTheme
import com.app.movieapp.utlis.CenteredCircularProgressIndicator
import com.app.movieapp.utlis.Constants.Companion.BASE_POSTER_IMAGE_URL
import com.app.movieapp.utlis.Constants.Companion.discoverListScreen
import com.app.movieapp.utlis.Constants.Companion.nowPlayingAllListScreen
import com.app.movieapp.utlis.Constants.Companion.popularAllListScreen
import com.app.movieapp.utlis.Constants.Companion.upcomingListScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun SeeAllScreen(
    selectedTitle: String,
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel()
) {
    BackHandler { navController.popBackStack() }

    val (title, allMoviesPagination) = when (selectedTitle) {
        nowPlayingAllListScreen -> {
            stringResource(id = R.string.nowPlaying) to viewModel.nowPlayingAllListState.collectAsLazyPagingItems()
        }
        discoverListScreen -> {
            stringResource(id = R.string.discover) to viewModel.discoverListState.collectAsLazyPagingItems()
        }
        upcomingListScreen -> {
            stringResource(id = R.string.upcoming) to viewModel.upcomingListState.collectAsLazyPagingItems()
        }
        popularAllListScreen -> {
            stringResource(id = R.string.popular) to viewModel.popularAllListState.collectAsLazyPagingItems()
        }
        else -> "" to null
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            CinematicGridHeader(
                title = title,
                subtitle = "Explore Catalog",
                onClickBack = { navController.popBackStack() },
                onClickSearch = { navController.navigate(MovieAppScreen.MOVIE_SEARCH.route) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(TmdbCinematicTheme.AppBackgroundGradient)
                .padding(paddingValues)
        ) {
            allMoviesPagination?.let { pagingItems ->
                MovieGridContent(
                    pagingItems = pagingItems,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun GenreWiseMoviesScreen(
    genId: String,
    genName: String,
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel()
) {
    // Triggers ViewModel to update the selected genre ID
    LaunchedEffect(genId) {
        genId.toIntOrNull()?.let { viewModel.setGenreData(it) }
    }

    BackHandler { navController.popBackStack() }

    // Safely collect as lazy paging items
    val genresWiseMoviePagination = viewModel.genresWiseMovieListState.collectAsLazyPagingItems()

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            CinematicGridHeader(
                title = genName,
                subtitle = "Genre Collection",
                onClickBack = { navController.popBackStack() },
                onClickSearch = { navController.navigate(MovieAppScreen.MOVIE_SEARCH.route) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(TmdbCinematicTheme.AppBackgroundGradient)
                .padding(paddingValues)
        ) {
            MovieGridContent(
                pagingItems = genresWiseMoviePagination,
                navController = navController
            )
        }
    }
}

// --- TOP HEADER BAR ---
@Composable
private fun CinematicGridHeader(
    title: String,
    subtitle: String,
    onClickBack: () -> Unit,
    onClickSearch: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onClickBack,
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

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = title,
                        color = TmdbCinematicTheme.TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = subtitle,
                        color = TmdbCinematicTheme.CoralAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            IconButton(
                onClick = onClickSearch,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(TmdbCinematicTheme.GlassSurface)
                    .border(1.dp, TmdbCinematicTheme.GlassBorderGradient, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = TmdbCinematicTheme.TextPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

// --- PAGINATED GRID CONTENT ---
@Composable
private fun MovieGridContent(
    pagingItems: LazyPagingItems<Movies>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyGridState()

    LazyVerticalGrid(
        state = listState,
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 100.dp),
        columns = GridCells.Adaptive(150.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(
            count = pagingItems.itemCount,
            key = { index -> "${pagingItems[index]?.id}_$index" }
        ) { index ->
            pagingItems[index]?.let { movie ->
                MovieItemSeeAll(
                    media = movie,
                    navController = navController
                )
            }
        }

        // Loading and Error States
        when (val refreshState = pagingItems.loadState.refresh) {
            is LoadState.Loading -> {
                header {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CenteredCircularProgressIndicator()
                    }
                }
            }
            is LoadState.Error -> {
                header {
                    refreshState.error.localizedMessage?.let {
                        Log.e("TAG_LoadState", "Refresh Error: ${refreshState.error}")
                        ErrorStrip(message = it)
                    }
                }
            }
            else -> Unit
        }

        when (val appendState = pagingItems.loadState.append) {
            is LoadState.Loading -> {
                header {
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
            is LoadState.Error -> {
                header {
                    appendState.error.localizedMessage?.let {
                        Log.e("TAG_LoadState", "Append Error: ${appendState.error}")
                        ErrorStrip(message = it)
                    }
                }
            }
            else -> Unit
        }
    }
}

// --- REDESIGNED MOVIE CARD ---
@Composable
fun MovieItemSeeAll(
    media: Movies,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val imageUrl = "$BASE_POSTER_IMAGE_URL${media.posterPath}"
    val title = media.displayTitle

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color.Black.copy(alpha = 0.5f)
            )
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, TmdbCinematicTheme.GlassBorderGradient, RoundedCornerShape(20.dp))
            .clickable {
                navController.navigate("${MovieAppScreen.MOVIE_HOME_DETAILS.route}/${media.id}")
            },
        colors = CardDefaults.cardColors(containerColor = TmdbCinematicTheme.GlassSurface)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Rating Floating Tag
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Black.copy(alpha = 0.6f))
                        .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = String.format("%.1f", media.voteAverage),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Title & Info Container
            Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)) {
                Text(
                    text = title,
                    color = TmdbCinematicTheme.TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = media.displayReleaseDate.take(4).ifEmpty { "N/A" },
                    color = TmdbCinematicTheme.TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

fun LazyGridScope.header(
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(span = { GridItemSpan(maxLineSpan) }, content = content)
}