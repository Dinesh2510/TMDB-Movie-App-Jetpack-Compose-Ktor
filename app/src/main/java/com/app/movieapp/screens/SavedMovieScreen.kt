package com.app.movieapp.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.app.movieapp.data.viewmodel.WatchListViewModel
import com.app.movieapp.graph.MovieAppScreen
import com.app.movieapp.screens.Componets.SearchMovieCard
import com.app.movieapp.ui.theme.TmdbCinematicTheme
import com.app.movieapp.utlis.Constants.Companion.BASE_POSTER_IMAGE_URL
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedMovieScreen(
    watchListViewModel: WatchListViewModel = koinViewModel(),
    navController: NavHostController,
) {
    val roomData by watchListViewModel.myMovieData.value.collectAsState(initial = emptyList())
    val context = LocalContext.current

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            // Glassmorphic Top Header Bar
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
                    // Back & Title Group
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
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(start = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "My Watchlist",
                                color = TmdbCinematicTheme.TextPrimary,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "${roomData.size} Saved Items",
                                color = TmdbCinematicTheme.CoralAccent,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Search Shortcut
                    IconButton(
                        onClick = { navController.navigate(MovieAppScreen.MOVIE_SEARCH.route) },
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
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(TmdbCinematicTheme.AppBackgroundGradient)
                .padding(padding)
        ) {
            if (roomData.isEmpty()) {
                // Empty Watchlist State
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(1.dp, TmdbCinematicTheme.GlassBorderGradient, CircleShape),
                        colors = CardDefaults.cardColors(containerColor = TmdbCinematicTheme.GlassSurface)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.BookmarkBorder,
                                contentDescription = "Empty Watchlist",
                                tint = TmdbCinematicTheme.TextSecondary,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Your Watchlist is Empty",
                        color = TmdbCinematicTheme.TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Explore movies and tap the bookmark icon to save your favorite titles here.",
                        color = TmdbCinematicTheme.TextSecondary,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            } else {
                // Watchlist Items List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp)
                ) {
                    items(roomData, key = { it.mediaId }) { movie ->
                        val imageUrl = movie.imagePath?.let { BASE_POSTER_IMAGE_URL + it } ?: ""

                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { dismissValue ->
                                when (dismissValue) {
                                    SwipeToDismissBoxValue.EndToStart -> {
                                        watchListViewModel.removeFromWatchList(movie.mediaId)
                                        Toast.makeText(context, "Removed from Watchlist", Toast.LENGTH_SHORT).show()
                                        true
                                    }
                                    else -> false
                                }
                            },
                            positionalThreshold = { distance -> distance * 0.25f }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = false,
                            backgroundContent = { CinematicDismissBackground(dismissState) },
                            content = {
                                SearchMovieCard(
                                    imageUrl = imageUrl,
                                    title = movie.title ?: "Untitled",
                                    overview = movie.releaseDate ?: ""
                                ) {
                                    navController.navigate("${MovieAppScreen.MOVIE_HOME_DETAILS.route}/${movie.mediaId}")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

// --- GLASSMORPHIC SWIPE-TO-DELETE BACKGROUND ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CinematicDismissBackground(dismissState: SwipeToDismissBoxState) {
    val color = if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
        Color(0xFF2C151B) // Dark Red Tint
    } else {
        Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
            .background(color)
            .border(1.dp, TmdbCinematicTheme.CoralAccent.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Remove",
                color = TmdbCinematicTheme.CoralAccent,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.DeleteSweep,
                contentDescription = "Delete",
                tint = TmdbCinematicTheme.CoralAccent,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}