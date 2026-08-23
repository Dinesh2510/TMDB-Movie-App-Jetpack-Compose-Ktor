/*
 * Copyright (c) 2026 Dinesh2510
 * File : MovieDetailScreen.kt
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

import android.content.Intent
import android.widget.Toast
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.navigation.NavHostController
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
import com.app.movieapp.screens.components.CinematicErrorState
import com.app.movieapp.screens.components.HomeSmallThumb
import com.app.movieapp.ui.theme.TmdbCinematicTheme
import com.app.movieapp.utlis.CenteredCircularProgressIndicator
import com.app.movieapp.utlis.Constants
import com.app.movieapp.utlis.Constants.Companion.BASE_POSTER_IMAGE_URL
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

    LaunchedEffect(movieId) {
        viewModel.fetchAllMovieDetails(movieId)
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
                    onRetryClick = { viewModel.fetchAllMovieDetails(movieId) }
                )
            }

            is MovieDetailsUIState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 40.dp)
                ) {
                    DisplayMovieData(
                        moviesInfo = state.movieDetails,
                        navController = navController,
                        watchListViewModel = watchListViewModel,
                        continueWatchingViewModel = continueWatchingViewModel
                    )

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
                            // 1. Overview Section
                            state.movieDetails.overview?.let {
                                if (state.movieDetails.overview.isNotBlank()) {
                                    Text(
                                        text = it,
                                        color = TmdbCinematicTheme.TextSecondary,
                                        fontSize = 14.sp,
                                        lineHeight = 21.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                Spacer(modifier = Modifier.height(20.dp))
                                }
                            }

                            // 2. Cast Section
                            if (state.castList.isNotEmpty()) {
                                CastMediaSection(state.castList)
                                Spacer(modifier = Modifier.height(20.dp))
                            }

                            // 3. Similar Movies Section
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

@Composable
fun DisplayMovieData(
    moviesInfo: MovieDetailsDTO,
    navController: NavHostController,
    watchListViewModel: WatchListViewModel,
    continueWatchingViewModel: ContinueWatchingViewModel
) {
    LaunchedEffect(moviesInfo.id) {
        watchListViewModel.exist(moviesInfo.id)
    }
    val exist = watchListViewModel.exist.value
    val context = LocalContext.current
    val date = SimpleDateFormat.getDateInstance().format(Date())

    val formattedGenres = moviesInfo.genres.joinToString(", ") { it.name }

    val myListMovie = WatchListModel(
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(480.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(Constants.BASE_BACKDROP_IMAGE_URL + moviesInfo.backdropPath),
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
                onClick = { navController.popBackStack() },
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
                            watchListViewModel.removeFromWatchList(mediaId = moviesInfo.id)
                            Toast.makeText(context, "Removed from Watchlist", Toast.LENGTH_SHORT)
                                .show()
                        } else {
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
                        val runtimeMs = (moviesInfo.runtime ?: 120) * 60 * 1000L
                        continueWatchingViewModel.saveProgress(
                            mediaId = moviesInfo.id,
                            title = moviesInfo.title,
                            posterPath = moviesInfo.posterPath,
                            backdropPath = moviesInfo.backdropPath,
                            currentPositionMs = (runtimeMs * 0.25).toLong(),
                            totalDurationMs = runtimeMs,
                            releaseDate = moviesInfo.releaseDate,
                            rating = moviesInfo.voteAverage
                        )
                        Toast.makeText(context, "Streaming ${moviesInfo.title}", Toast.LENGTH_SHORT)
                            .show()
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
                .border(2.dp, TmdbCinematicTheme.CoralAccent, CircleShape),
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
                    navController.navigate("${MovieAppScreen.MOVIE_HOME_DETAILS.route}/${mediaList[index].id}")
                }
            }
        }
    }
}