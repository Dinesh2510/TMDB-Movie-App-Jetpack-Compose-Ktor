package com.app.movieapp.screens

import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.app.movieapp.data.viewmodel.SearchViewModel
import com.app.movieapp.graph.MovieAppScreen
import com.app.movieapp.screens.Componets.ErrorStrip
import com.app.movieapp.screens.Componets.SearchMovieCard
import com.app.movieapp.ui.theme.TmdbCinematicTheme
import com.app.movieapp.utlis.CenteredCircularProgressIndicator
import com.app.movieapp.utlis.Constants.Companion.BASE_POSTER_IMAGE_URL
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = koinViewModel(),
    navController: NavHostController,
) {
    val searchResult = searchViewModel.multiSearchState.value.collectAsLazyPagingItems()
    var queryText by rememberSaveable { mutableStateOf(searchViewModel.searchParam.value) }
    val context = LocalContext.current

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 12.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // --- HEADING SECTION WITH LEFT BACK BUTTON ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Back Button (NOW MOVED TO LEFT)
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

                    // --- CINEMATIC SEARCH INPUT ---
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

                    // --- QUICK SUGGESTION CHIPS ---
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val suggestions = listOf("Jack Reacher", "Dune", "Spider Man", "TopGun Maverick", "Marvel")
                        items(suggestions.size) { tag ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(TmdbCinematicTheme.GlassSurface)
                                    .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                                    .clickable {
                                        queryText = suggestions[tag]
                                        searchViewModel.searchParam.value = suggestions[tag]
                                        searchViewModel.searchRemoteMovie(true)
                                    }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = suggestions[tag],
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                // Search Trigger Action Button
                if (queryText != searchViewModel.searchParam.value && queryText.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(TmdbCinematicTheme.PrimaryActionGradient)
                            .clickable {
                                searchViewModel.searchParam.value = queryText
                                searchViewModel.searchRemoteMovie(true)
                            }
                            .padding(horizontal = 16.dp)
                            .align(Alignment.CenterHorizontally),
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

                // Results Counter Badge
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

                // Main Lazy Column for Paged Items
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

                    // Paging State Loading / Error Handlers
                    searchResult.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> {
                                item {
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

                            loadState.refresh is LoadState.Error -> {
                                val error = loadState.refresh as LoadState.Error
                                item {
                                    ErrorStrip(message = error.error.localizedMessage ?: "Failed to load results.")
                                }
                            }

                            loadState.append is LoadState.Error -> {
                                val error = loadState.append as LoadState.Error
                                item {
                                    ErrorStrip(message = error.error.localizedMessage ?: "Failed to load more items.")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}