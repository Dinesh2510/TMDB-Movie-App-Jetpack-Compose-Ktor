package com.app.movieapp.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.app.movieapp.ui.theme.TmdbTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import com.app.movieapp.ui.theme.TmdbCinematicTheme
import com.app.movieapp.ui.theme.TmdbTheme.AccentGradient

import kotlinx.coroutines.delay
import kotlin.random.Random

// Dummy Data Models tailored for the detailed Homepage
data class FeaturedMovie(val id: Int, val title: String, val genreText: String, val rating: String, val backdropUrl: String)
data class PosterMovie(val id: Int, val title: String, val rating: String, val date: String, val posterUrl: String)
data class DetailedMovieRow(val id: Int, val title: String, val rating: String, val desc: String, val posterUrl: String)

@Composable
fun TmdbHomeScreen() {
    val categories = listOf("All", "Action", "Sci-Fi", "Comedy", "Horror", "Drama")
    var selectedCategory by remember { mutableStateOf("Sci-Fi") }

    val heroMovies = remember {
        listOf(
            FeaturedMovie(1, "Dune: Part Two", "Sci-Fi • 2h 46m", "8.8", "https://picsum.photos/seed/dune/600/350"),
            FeaturedMovie(2, "Oppenheimer", "Biography • 3h 00m", "8.6", "https://picsum.photos/seed/oppen/600/350"),
            FeaturedMovie(3, "The Batman", "Action • 2h 56m", "8.3", "https://picsum.photos/seed/batman/600/350")
        )
    }
    val theaterMovies = remember {
        List(8) { index ->
            PosterMovie(
                id = index + 1,
                title = "Theater Film #$index",
                rating = "★ 8.${8 - (index % 5)}",
                date = "Apr 12, 2026",
                posterUrl = "https://picsum.photos/seed/play_$index/200/300"
            )
        }
    }

    val trendingRows = remember {
        List(20) { index ->
            DetailedMovieRow(
                id = index + 1,
                title = "Interstellar",
                rating = "★★★★★",
                desc = "A journey beyond stars...",
                posterUrl = "https://picsum.photos/seed/trend_$index/200/300"
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TmdbTheme.BackgroundGradient)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp) // Clearance for translucent bottom bar
        ) {
            // 1. Home Header (Top Bar)
            item {
                HomeHeader()
            }

            // 2. Hero Trending Banner Pager
            item {
                HeroTrendingPager(movies = heroMovies)
            }

            // 3. Genre Quick-Filter Chips
            item {
                // --- DUMMY DATA ---
                val dummyTop10IndiaMovies = listOf(
                    Top10IndiaMovie(
                        id = 1,
                        title = "Dune: Part Two",
                        category = "Sci-Fi • Action",
                        rating = "8.8",
                        posterUrl = "https://picsum.photos/seed/dune_poster/300/450"
                    ),
                    Top10IndiaMovie(
                        id = 2,
                        title = "Ikkis",
                        category = "War • Drama",
                        rating = "8.6",
                        posterUrl = "https://picsum.photos/seed/ikkis_poster/300/450"
                    ),
                    Top10IndiaMovie(
                        id = 3,
                        title = "The Dark Knight",
                        category = "Action • Crime",
                        rating = "9.0",
                        posterUrl = "https://picsum.photos/seed/batman_poster/300/450"
                    ),
                    Top10IndiaMovie(
                        id = 4,
                        title = "Awarapan 2",
                        category = "Action • Romance",
                        rating = "8.4",
                        posterUrl = "https://picsum.photos/seed/awarapan_poster/300/450"
                    ),
                    Top10IndiaMovie(
                        id = 5,
                        title = "Cocktail 2",
                        category = "Romance • Comedy",
                        rating = "8.1",
                        posterUrl = "https://picsum.photos/seed/cocktail_poster/300/450"
                    ),
                    Top10IndiaMovie(
                        id = 6,
                        title = "Spider-Man: No Way Home",
                        category = "Action • Sci-Fi",
                        rating = "8.3",
                        posterUrl = "https://picsum.photos/seed/spiderman_poster/300/450"
                    ),
                    Top10IndiaMovie(
                        id = 7,
                        title = "Vaazha II",
                        category = "Comedy • Drama",
                        rating = "8.5",
                        posterUrl = "https://picsum.photos/seed/vaazha_poster/300/450"
                    ),
                    Top10IndiaMovie(
                        id = 8,
                        title = "Interstellar",
                        category = "Sci-Fi • Adventure",
                        rating = "8.7",
                        posterUrl = "https://picsum.photos/seed/interstellar_poster/300/450"
                    ),
                    Top10IndiaMovie(
                        id = 9,
                        title = "The Godfather",
                        category = "Crime • Drama",
                        rating = "9.2",
                        posterUrl = "https://picsum.photos/seed/godfather_poster/300/450"
                    ),
                    Top10IndiaMovie(
                        id = 10,
                        title = "Governor",
                        category = "Political • Thriller",
                        rating = "8.2",
                        posterUrl = "https://picsum.photos/seed/governor_poster/300/450"
                    )
                )

                // --- INTEGRATION EXAMPLE WITHIN A LAZYCOLUMN FEED ---
                val top10Movies = remember { dummyTop10IndiaMovies }

                ModernTop10IndiaSection(
                    top10List = top10Movies,
                    onMovieClick = { selectedMovie ->
                        // Handle movie click navigation or detail open
                        println("Selected Movie: ${selectedMovie.title}")
                    }
                )
            }
            item {

                LandscapeMoviesSection(
                    sectionTitle = "CONTINUE WATCHING",
                    movies = dummyLandscapeMovies,
                    onMovieClick = {}
                )
            }
            item {
                TmdbCategoryExploreSection()
            }


            // 4. "Now Playing in Theaters" Section (Poster Grid)
            item {
                SectionHeader(title = "Now Playing in Theaters")
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(theaterMovies) { movie ->
                        MoviePosterGridCard(movie = movie)
                    }
                }
            }

            // 5. "Trending This Week" Section (Detailed Listings)
            item {
                Spacer(modifier = Modifier.height(24.dp))
                SectionHeader(title = "Trending This Week")
            }

            items(trendingRows, key = { it.id }) { rowItem ->
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    MovieDetailedRowCard(item = rowItem)
                }
            }
        }
    }
}

// --- Home Screen UI Components ---

@Composable
fun HomeHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Welcome Back, Alex 👋",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 12.sp
            )
            Text(
                text = "Discover Movies",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Functional Filter/Tune Icon
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(TmdbTheme.GlassSurface)
                .border(1.dp, TmdbTheme.GlassBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Tune,
                contentDescription = "Filter",
                tint = Color.White
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeroTrendingPager(movies: List<FeaturedMovie>) {
    val pagerState = rememberPagerState(pageCount = { movies.size })

    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 12.dp
        ) { page ->
            val movie = movies[page]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = movie.backdropUrl,
                        contentDescription = movie.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Cinematic Overlay (Dark gradient from bottom up)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.85f)
                                    )
                                )
                            )
                    )

                    // Hero Text & FAB
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = movie.title,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${movie.genreText} • ${movie.rating}",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                        }

                        // Neon Play FAB
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(TmdbTheme.PrimaryGradient),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = "Play Trailer",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}



data class Top10IndiaMovie(
    val id: Int,
    val title: String,
    val category: String,
    val rating: String,
    val posterUrl: String
)

@Composable
fun ModernTop10IndiaCard(
    rank: Int,
    movie: Top10IndiaMovie,
    onMovieClick: (Top10IndiaMovie) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(185.dp)
            .height(245.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onMovieClick(movie) }
    ) {
        // LAYER 1: Large Outlined Rank Number positioned behind & overlapping left edge
        Text(
            text = "$rank",
            style = TextStyle(
                fontSize = 110.sp,
                fontWeight = FontWeight.Black,
                color = Color.White.copy(alpha = 0.12f),
                drawStyle = Stroke(width = 6f) // Modern outlined typography effect
            ),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-6).dp, y = 14.dp)
        )

        // LAYER 2: Main Floating Glass Poster Card
        Card(
            modifier = Modifier
                .width(148.dp)
                .height(220.dp)
                .align(Alignment.TopEnd)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = Color.Black.copy(alpha = 0.5f),
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
                        width = 1.2.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.45f),
                                Color.White.copy(alpha = 0.08f)
                            )
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                // High-Res Poster Image
                AsyncImage(
                    model = movie.posterUrl,
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Dark Cinematic Overlay Gradient
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

                // Top Floating Glass Rating Pill
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
                        text = movie.rating,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Bookmark Icon Button
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.BookmarkBorder,
                        contentDescription = "Watchlist",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }

                // Bottom Content Details & Quick Play Action
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
                            text = movie.title,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(1.dp))
                        Text(
                            text = movie.category,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 10.sp,
                            maxLines = 1
                        )
                    }

                    // Neon Coral Action Button
                    Box(
                        modifier = Modifier
                            .size(28.dp)
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
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ModernTop10IndiaSection(
    top10List: List<Top10IndiaMovie>,
    onMovieClick: (Top10IndiaMovie) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "TRENDING 10",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                /*Text(
                    text = "🇮🇳",
                    fontSize = 16.sp
                )*/
            }
            /*Text(
                text = "See all",
                color = Color(0xFFFF7A00),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )*/
        }

        // Horizontal Carousel
        LazyRow(
            contentPadding = PaddingValues(start = 12.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(top10List, key = { _, item -> item.id }) { index, movie ->
                ModernTop10IndiaCard(
                    rank = index + 1,
                    movie = movie,
                    onMovieClick = onMovieClick
                )
            }
        }
    }
}
/*cate*/
// Data model matching dynamic TMDB API responses
data class CategoryGenre(
    val id: Int,
    val name: String,
    val imageUrl: String // Image fetched from dynamic TMDB endpoint
)

@Composable
fun CategoryImageCard(
    category: CategoryGenre,
    isSelected: Boolean,
    onCategoryClick: (CategoryGenre) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(130.dp)
            .height(80.dp)
            .clickable { onCategoryClick(category) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (isSelected) {
                        Modifier.border(
                            width = 2.dp,
                            brush = AccentGradient,
                            shape = RoundedCornerShape(16.dp)
                        )
                    } else {
                        Modifier.border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                )
        ) {
            // Background Image loaded directly from TMDB
            AsyncImage(
                model = category.imageUrl,
                contentDescription = category.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            )

            // Darkening Gradient overlay for maximum text contrast
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

            // Category Title Centered
            Text(
                text = category.name,
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

// Horizontal scroll list component
@Composable
fun DynamicCategorySection(
    categories: List<CategoryGenre>,
    selectedGenreId: Int?,
    onCategorySelected: (CategoryGenre) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(categories, key = { it.id }) { category ->
            CategoryImageCard(
                category = category,
                isSelected = category.id == selectedGenreId,
                onCategoryClick = onCategorySelected
            )
        }
    }
}

// --- DUMMY TMDB GENRE DATA WITH IMAGE BACKDROPS ---
val dummyTmdbCategories = listOf(
    CategoryGenre(
        id = 28,
        name = "Action",
        imageUrl = "https://picsum.photos/seed/dune/600/350" // Dark Knight backdrop
    ),
    CategoryGenre(
        id = 878,
        name = "Sci-Fi",
        imageUrl = "https://picsum.photos/seed/dune/600/350" // Dune backdrop
    ),
    CategoryGenre(
        id = 35,
        name = "Comedy",
        imageUrl = "https://picsum.photos/seed/dune/600/350"
    ),
    CategoryGenre(
        id = 27,
        name = "Horror",
        imageUrl = "https://picsum.photos/seed/dune/600/350"
    ),
    CategoryGenre(
        id = 18,
        name = "Drama",
        imageUrl = "https://picsum.photos/seed/dune/600/350" // Oppenheimer backdrop
    ),
    CategoryGenre(
        id = 53,
        name = "Thriller",
        imageUrl = "https://picsum.photos/seed/dune/600/350"
    )
)

// --- PARENT INTEGRATION CONTAINER ---
@Composable
fun TmdbCategoryExploreSection() {
    val categories = remember { dummyTmdbCategories }
    var selectedGenreId by remember { mutableStateOf<Int?>(28) } // Default selected: Action (28)

    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        // Section Title Header
        Text(
            text = "EXPLORE BY GENRE",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp,).padding( bottom = 12.dp)
        )

        // Dynamic Horizontal Category Scroll
        DynamicCategorySection(
            categories = categories,
            selectedGenreId = selectedGenreId,
            onCategorySelected = { clickedCategory ->
                selectedGenreId = clickedCategory.id
                // Trigger API call to fetch movies for this genre ID (e.g., viewModel.fetchMoviesByGenre(clickedCategory.id))
            }
        )
    }
}

// --- COMPOSE PREVIEW ---
/*@Preview(showBackground = true, widthDp = 412, heightDp = 200)
@Composable
fun DynamicCategorySectionPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0E17))
    ) {
        TmdbCategoryExploreSection()
    }
}*/
/*cat end*/


// --- DATA MODEL FOR TMDB LANDSCAPE CATEGORIES ---
data class TmdbLandscapeGenre(
    val id: Int,
    val name: String,
    val backdropUrl: String // TMDB backdrop path (16:9)
)

// --- DUMMY DATA WITH REAL TMDB BACKDROP URLS ---
val dummyLandscapeGenres = listOf(
    TmdbLandscapeGenre(
        id = 28,
        name = "Action",
        backdropUrl = "https://image.tmdb.org/t/p/w780/8xV1A3Xi3A6d3aK0Ew7N3B40Ie.jpg"
    ),
    TmdbLandscapeGenre(
        id = 878,
        name = "Sci-Fi",
        backdropUrl = "https://image.tmdb.org/t/p/w780/xJHokMbljvjADYdit5fK2V2O2fH.jpg"
    ),
    TmdbLandscapeGenre(
        id = 18,
        name = "Drama",
        backdropUrl = "https://image.tmdb.org/t/p/w780/fm6K8O2e3R0A7aF13C8K2b4O6.jpg"
    ),
    TmdbLandscapeGenre(
        id = 27,
        name = "Horror",
        backdropUrl = "https://image.tmdb.org/t/p/w780/t53Uq4O8z4z3D7aI40A5N2e6C.jpg"
    ),
    TmdbLandscapeGenre(
        id = 35,
        name = "Comedy",
        backdropUrl = "https://image.tmdb.org/t/p/w780/r9P1O9vEaI2H6d76A5oF12C3b7.jpg"
    )
)

// --- LANDSCAPE CATEGORY CARD COMPOSABLE ---
@Composable
fun LandscapeCategoryCard(
    genre: TmdbLandscapeGenre,
    isSelected: Boolean,
    onGenreClick: (TmdbLandscapeGenre) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(160.dp)  // 16:9 Proportional Width
            .height(90.dp)  // 16:9 Proportional Height
            .clickable { onGenreClick(genre) },
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (isSelected) {
                        Modifier.border(
                            width = 2.dp,
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFFFF5252), Color(0xFFFF7A00))
                            ),
                            shape = RoundedCornerShape(18.dp)
                        )
                    } else {
                        Modifier.border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(18.dp)
                        )
                    }
                )
        ) {
            // 16:9 High-Res Backdrop Image
            AsyncImage(
                model = genre.backdropUrl,
                contentDescription = genre.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(18.dp))
            )

            // Cinematic Linear Scrim (Darkens bottom left for max readability)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.88f),
                                Color.Black.copy(alpha = 0.20f)
                            )
                        )
                    )
            )

            // Category Label
            Text(
                text = genre.name,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 12.dp, bottom = 10.dp)
            )
        }
    }
}


// --- DATA MODEL FOR MOVIES/SHOWS (TMDB Backdrop API) ---
data class LandscapeMovie(
    val id: Int,
    val title: String,
    val genreText: String,
    val rating: String,
    val backdropUrl: String
)

// --- DUMMY DATA FOR TESTING ---
val dummyLandscapeMovies = listOf(
    LandscapeMovie(
        id = 1,
        title = "Dune: Part Two",
        genreText = "Sci-Fi • 2h 46m",
        rating = "8.8",
        backdropUrl = "https://image.tmdb.org/t/p/w780/8xV1A3Xi3A6d3aK0Ew7N3B40Ie.jpg"
    ),
    LandscapeMovie(
        id = 2,
        title = "Oppenheimer",
        genreText = "Biography • 3h 00m",
        rating = "8.6",
        backdropUrl = "https://image.tmdb.org/t/p/w780/fm6K8O2e3R0A7aF13C8K2b4O6.jpg"
    ),
    LandscapeMovie(
        id = 3,
        title = "The Dark Knight",
        genreText = "Action • 2h 32m",
        rating = "9.0",
        backdropUrl = "https://image.tmdb.org/t/p/w780/nMK2819TyP3p0j5q6aL1x.jpg"
    ),
    LandscapeMovie(
        id = 4,
        title = "Interstellar",
        genreText = "Sci-Fi • 2h 49m",
        rating = "8.7",
        backdropUrl = "https://image.tmdb.org/t/p/w780/xJHokMbljvjADYdit5fK2V2O2fH.jpg"
    )
)

// --- LANDSCAPE MOVIE CARD COMPOSABLE ---
@Composable
fun LandscapeMovieCard(
    movie: LandscapeMovie,
    onMovieClick: (LandscapeMovie) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(240.dp) // Wide 16:9 Landscape Frame
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
            // 16:9 Backdrop Image from TMDB
            AsyncImage(
                model = movie.backdropUrl,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(18.dp))
            )

            // Dark Cinematic Gradient Overlay (Top & Bottom readability)
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

            // Top Left Rating Badge
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
                    text = movie.rating,
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Top Right Bookmark Shortcut
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.BookmarkBorder,
                    contentDescription = "Save",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }

            // Bottom Info Row & Quick Play Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = movie.title,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Text(
                        text = movie.genreText,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Neon Coral Play Button
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
    movie: LandscapeMovie,
    onMovieClick: (LandscapeMovie) -> Unit,
    modifier: Modifier = Modifier,
    // Assigns random playback progress (30% to 85%) if no specific float value is passed
    progress: Float = remember(movie.id) { Random.nextFloat() * 0.55f + 0.30f }
) {
    Card(
        modifier = modifier
            .width(240.dp) // Wide 16:9 Landscape Frame
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
            // 16:9 Backdrop Image
            AsyncImage(
                model = movie.backdropUrl,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(18.dp))
            )

            // Cinematic Dark Gradient Overlay (For Text Readability)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.2f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.95f)
                            )
                        )
                    )
            )

            // Bottom Column: Title, Quick Play Button, & Progress Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = movie.title,
                            color = TmdbCinematicTheme.TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            text = movie.genreText,
                            color = TmdbCinematicTheme.TextSecondary,
                            fontSize = 11.sp,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Neon Coral Resume Play Button
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(TmdbCinematicTheme.PrimaryActionGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "Resume",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Playback Progress Bar Line
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(CircleShape),
                    color = TmdbCinematicTheme.CoralAccent,
                    trackColor = Color.White.copy(alpha = 0.25f),
                    strokeCap = StrokeCap.Round
                )
            }
        }
    }
}
// --- LANDSCAPE MOVIES HORIZONTAL SECTION ---
@Composable
fun LandscapeMoviesSection(
    sectionTitle: String,
    movies: List<LandscapeMovie>,
    onMovieClick: (LandscapeMovie) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        // Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp,).padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = sectionTitle,
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Text(
                text = "See all",
                color = Color(0xFFFF7A00),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Horizontal Carousel
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(movies, key = { it.id }) { movie ->
                ContinueWatchingCard(
                    movie = movie,
                    onMovieClick = onMovieClick
                )

                /*LandscapeMovieCard(
                    movie = movie,
                    onMovieClick = onMovieClick
                )*/
            }
        }
    }
}

// --- COMPOSE PREVIEW ---
@Preview(showBackground = true, widthDp = 412, heightDp = 260)
@Composable
fun LandscapeMoviesSectionPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0E17))
    ) {
        LandscapeMoviesSection(
            sectionTitle = "CONTINUE WATCHING",
            movies = dummyLandscapeMovies,
            onMovieClick = {}
        )
    }
}
@Composable
fun SectionHeader(title: String) {
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
            color = Color(0xFFFF7A00),
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun MoviePosterGridCard(movie: PosterMovie) {
    Column(
        modifier = Modifier.width(130.dp)
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
                    model = movie.posterUrl,
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Rating Badge (Top Right)
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
                        text = movie.rating,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = movie.title,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}

@Composable
fun MovieDetailedRowCard(item: DetailedMovieRow) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = TmdbTheme.GlassSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.posterUrl,
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.DarkGray)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${item.rating} • ${item.desc}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White.copy(alpha = 0.7f),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Watchlist CTA
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f))
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Watchlist", color = Color.White, fontSize = 10.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 850)
@Composable
fun TmdbHomeScreenPreview() {
    TmdbHomeScreen()
}