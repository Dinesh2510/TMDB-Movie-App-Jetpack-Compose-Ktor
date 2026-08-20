package com.app.movieapp.screens

import android.content.Intent
import android.util.Log
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.rememberAsyncImagePainter
import com.app.movieapp.R
import com.app.movieapp.data.local.WatchListModel
import com.app.movieapp.data.remote.response.MovieDetailsDTO
import com.app.movieapp.data.remote.response.MovieResponse
import com.app.movieapp.data.viewmodel.MovieDetailsViewModel
import com.app.movieapp.data.viewmodel.WatchListViewModel
import com.app.movieapp.graph.MovieAppScreen
import com.app.movieapp.models.Cast
import com.app.movieapp.models.Genre
import com.app.movieapp.screens.Componets.HomeSmallThumb
import com.app.movieapp.ui.theme.TmdbCinematicTheme
import com.app.movieapp.utlis.CenteredCircularProgressIndicator
import com.app.movieapp.utlis.Constants
import com.app.movieapp.utlis.Constants.Companion.BASE_POSTER_IMAGE_URL
import com.app.movieapp.utlis.MovieState
import com.app.movieapp.utlis.ShowError
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun MovieDetailsScreen(
    navController: NavHostController,
    movieId: String,
    viewModel: MovieDetailsViewModel = koinViewModel(),
    watchListViewModel: WatchListViewModel = koinViewModel()
) {
    val detailsMovieState by viewModel.detailsMovieResponses.collectAsState()
    val castMovieState by viewModel.castMovieResponses.collectAsState()
    val similarMovieState by viewModel.similarMovieResponses.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.fetchMoviesDetails(movieId)
        viewModel.fetchSimilarMovies(movieId)
        viewModel.fetchCasteOfMovies(movieId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TmdbCinematicTheme.AppBackgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 40.dp)
        ) {
            when (detailsMovieState) {
                is MovieState.Success -> {
                    val moviesInfo = (detailsMovieState as MovieState.Success<MovieDetailsDTO?>).data
                    if (moviesInfo != null) {
                        DisplayMovieData(moviesInfo, navController, watchListViewModel)
                    }
                }
                is MovieState.Error -> {
                    ShowError((detailsMovieState as MovieState.Error).message)
                }
                is MovieState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CenteredCircularProgressIndicator()
                    }
                }
            }

            // Floating Glass Content Card Container
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
                    .border(1.dp, TmdbCinematicTheme.GlassBorderGradient, RoundedCornerShape(28.dp)),
                colors = CardDefaults.cardColors(containerColor = TmdbCinematicTheme.GlassSurface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // 1. Overview Section
                    if (detailsMovieState is MovieState.Success) {
                        val moviesInfo = (detailsMovieState as MovieState.Success<MovieDetailsDTO?>).data
                        moviesInfo?.overview?.let { overview ->
                            Text(
                                text = overview,
                                color = TmdbCinematicTheme.TextSecondary,
                                fontSize = 14.sp,
                                lineHeight = 21.sp,
                                fontWeight = FontWeight.Normal
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }

                    // 2. Cast Section
                    when (castMovieState) {
                        is MovieState.Success -> {
                            val castList = (castMovieState as MovieState.Success<List<Cast>?>).data ?: emptyList()
                            if (castList.isNotEmpty()) {
                                CastMediaSection(castList)
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                        is MovieState.Error -> {
                            ShowError((castMovieState as MovieState.Error).message)
                        }
                        is MovieState.Loading -> {
                            CenteredCircularProgressIndicator()
                        }
                    }

                    // 3. Similar Movies Section
                    when (similarMovieState) {
                        is MovieState.Success -> {
                            val movieList = (similarMovieState as MovieState.Success<MovieResponse?>).data
                            if (movieList != null && movieList.results.isNotEmpty()) {
                                SimilarMediaSection(movieList, navController)
                            }
                        }
                        is MovieState.Error -> {
                            ShowError((similarMovieState as MovieState.Error).message)
                        }
                        is MovieState.Loading -> {
                            CenteredCircularProgressIndicator()
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
    watchListViewModel: WatchListViewModel
) {
    LaunchedEffect(moviesInfo.id) {
        watchListViewModel.exist(moviesInfo.id)
    }
    val exist = watchListViewModel.exist.value
    val context = LocalContext.current
    val date = SimpleDateFormat.getDateInstance().format(Date())

    val myListMovie = WatchListModel(
        mediaId = moviesInfo.id,
        imagePath = moviesInfo.posterPath,
        title = moviesInfo.title,
        releaseDate = moviesInfo.releaseDate,
        rating = moviesInfo.voteAverage,
        addedOn = date
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(480.dp)
    ) {
        // Full Backdrop Image
        Image(
            painter = rememberAsyncImagePainter(Constants.BASE_BACKDROP_IMAGE_URL + moviesInfo.backdropPath),
            contentDescription = "Backdrop",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Dark Vertical Gradient Overlay for readability
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

        // Top Action Bar Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 44.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Glass Back Button
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
                    modifier = Modifier.size(16.dp).padding(start = 4.dp)
                )
            }

            // Top Right Action Group
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                // Watchlist Bookmark Button
                IconButton(
                    onClick = {
                        if (exist != 0) {
                            watchListViewModel.removeFromWatchList(mediaId = moviesInfo.id)
                            Toast.makeText(context, "Removed from Watchlist", Toast.LENGTH_SHORT).show()
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

                // Share Button
                IconButton(
                    onClick = {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "Check out ${moviesInfo.title} on TMDB App!")
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

        // Movie Title, Metadata & Action Button Overlay
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Main Movie Title
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

            // Genres String Line
            if (moviesInfo.genres.isNotEmpty()) {
                Text(
                    text = moviesInfo.genres.joinToString(" | ") { it.name },
                    color = TmdbCinematicTheme.TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Metadata Row: Rating, Runtime, PG-13 Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Rating
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${moviesInfo.voteAverage}/10",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Runtime
                Text(
                    text = "${moviesInfo.runtime ?: 0} min",
                    color = TmdbCinematicTheme.TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )

                // Rating Badge
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

            // Cyan-Neon "WATCH NOW" Button
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
                        Toast.makeText(context, "Streaming ${moviesInfo.title}", Toast.LENGTH_SHORT).show()
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
        // Glowing Neon Circle Avatar Frame
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

// --- COMPOSE PREVIEWS ---

@Preview(showBackground = true, widthDp = 412, heightDp = 850)
@Composable
fun MovieDetailsPreview() {
    val dummyMovie = MovieDetailsDTO(
        id = 1,
        title = "Cosmic Odyssey",
        overview = "Captain Ava and her crew navigate uncharted space, discovering ancient alien secrets and facing existential threats to save humanity.",
        posterPath = "",
        backdropPath = "",
        releaseDate = "2026-08-15",
        voteAverage = 4.8,
        runtime = 135,
        tagline = "The Universe Awaits",
        genres = listOf(Genre(1, "Sci-Fi"), Genre(2, "Adventure"), Genre(3, "Mystery")),
        spokenLanguages = emptyList()
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TmdbCinematicTheme.AppBackgroundGradient)
    ) {
        DisplayMovieData(
            moviesInfo = dummyMovie,
            navController = rememberNavController(),
            watchListViewModel = koinViewModel()
        )
    }
}