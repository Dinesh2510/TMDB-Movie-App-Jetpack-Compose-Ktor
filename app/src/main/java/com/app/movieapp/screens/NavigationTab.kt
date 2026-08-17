package com.app.movieapp.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage

// --- DESIGN SYSTEM & THEME ---
// Defining specific cosmic dark, glass, and accent colors for maximum pop.
object CinematicTheme {
    // Screen background (matched to deep indigos in the reference)
    val ScreenBgGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF0F0E17), Color(0xFF161522), Color(0xFF0F0E17))
    )

    // Main Dark Glass Surface for Navigation (High Contrast with white content)
    val NavigationSurface = Color(0xFF181726).copy(alpha = 0.85f) // Dark indigo-grey, semi-translucent

    // Liquid Glass Edges/Refraction Border
    val GlassBorderGradient = Brush.verticalGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.50f), // Top light highlight
            Color.White.copy(alpha = 0.15f)  // Bottom soft fade
        )
    )

    // Active Selection Accent (Vibrant Coral/Orange Gradient Pill)
    val ActiveGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFFEA5B43), Color(0xFFFF7A00))
    )

    // Foreground Text & Icons
    val TextPrimary = Color.White
    val TextSecondary = Color.White.copy(alpha = 0.70f)
}

// --- DATA MODELS ---
data class MovieItem(
    val id: Int,
    val title: String,
    val subtitle: String,
    val imageUrl: String
)

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Movies : Screen("movies", "Movies", Icons.Filled.Movie)
    object Favorites : Screen("favorites", "Favorites", Icons.Filled.Star)
    object Profile : Screen("profile", "Profile", Icons.Filled.Person)
}

// --- UPDATED BOTTOM NAVIGATION BAR ---
// Strictly separates navigation pill from Search FAB with specific dark glass styling.
@Composable
fun FloatingAirNavigationBar(
    currentRoute: String?,
    onTabSelected: (Screen) -> Unit,
    onSearchClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = remember {
        listOf(Screen.Home, Screen.Movies, Screen.Favorites, Screen.Profile)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 12.dp,
                // Pushes the bar up to float correctly over device gesture bars.
                bottom = 16.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // LAYER 1: Main Dark Glass Navigation Pill (High Contrast Surface)
        Box(
            modifier = Modifier
                .height(68.dp)
                .weight(1f)
                .shadow(
                    elevation = 16.dp,
                    shape = CircleShape,
                    ambientColor = Color.Black.copy(alpha = 0.3f),
                    spotColor = Color.Black.copy(alpha = 0.4f)
                )
                .clip(CircleShape)
                .border(1.5.dp, CinematicTheme.GlassBorderGradient, CircleShape)
                .background(CinematicTheme.NavigationSurface)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                tabs.forEach { screen ->
                    val isSelected = currentRoute == screen.route

                    // Content color transitions (White on Dark Glass)
                    val animatedContentColor by animateColorAsState(
                        targetValue = if (isSelected) CinematicTheme.TextPrimary else CinematicTheme.TextSecondary,
                        animationSpec = tween(durationMillis = 250),
                        label = "tabContent"
                    )

                    // Spring scale animation for selected icon/text Column
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.08f else 1.0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        label = "tabScale"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(4.dp)
                            .clip(CircleShape)
                            .then(
                                // LAYER 2: SELECTED ACCENT (Coral Gradient Pill)
                                if (isSelected) Modifier.background(CinematicTheme.ActiveGradient)
                                else Modifier
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onTabSelected(screen) },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.scale(scale),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title,
                                tint = animatedContentColor,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = screen.title,
                                color = animatedContentColor,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // LAYER 3: Standalone Right Circular Search FAB (Matching Dark Glass surface)
        Box(
            modifier = Modifier
                .size(68.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = CircleShape,
                    ambientColor = Color.Black.copy(alpha = 0.3f),
                    spotColor = Color.Black.copy(alpha = 0.4f)
                )
                .clip(CircleShape)
                .border(1.5.dp, CinematicTheme.GlassBorderGradient, CircleShape)
                .background(CinematicTheme.NavigationSurface)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onSearchClicked() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

// --- MAIN CONTAINER ---
@Composable
fun MainAppScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        containerColor = Color.Transparent,
        // Disables standard Scaffold padding calculation so full gradient bleeds correctly.
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            FloatingAirNavigationBar(
                currentRoute = currentRoute,
                onTabSelected = { screen ->
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onSearchClicked = { /* Handle search Activity launch here */ }
            )
        }
    ) { _ ->
        // Note: innerPadding intentionally omitted from NavHost for cinematic background flow.
        NavHost(
            navController = navController,
            startDestination = Screen.Movies.route, // Highlighting 'Movies' active tab
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Screen.Home.route) { ScreenContent("Home Screen") }
            composable(Screen.Movies.route) { ScreenContent("Movies Screen") }
            composable(Screen.Favorites.route) { ScreenContent("Favorites Screen") }
            composable(Screen.Profile.route) { ScreenContent("Profile Screen") }
        }
    }
}

// --- SCREEN CONTENT (Scrollable Feed) ---
@Composable
fun ScreenContent(title: String) {
    // Generate 50 items for the feed
    val itemsList = remember(title) {
        List(50) { index ->
            MovieItem(
                id = index + 1,
                title = "$title Item #${index + 1}",
                subtitle = "Action, Drama, Sci-Fi • 2026",
                imageUrl = "https://picsum.photos/seed/${title}_${index + 1}/200/300"
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CinematicTheme.ScreenBgGradient)
    ) {
        AnimatedContent(
            targetState = itemsList,
            transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
            label = "screenTransition"
        ) { targetList ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = 48.dp,
                    start = 16.dp,
                    end = 16.dp,
                    // Clearance to ensure last items scroll above the floating bottom bar.
                    bottom = 120.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "$title Collection",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                items(targetList, key = { it.id }) { item ->
                    MovieListItemCard(item = item)
                }
            }
        }
    }
}

@Composable
fun MovieListItemCard(item: MovieItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            // Translucent dark glass card surface
            containerColor = Color.White.copy(alpha = 0.15f)
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
                model = item.imageUrl,
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(74.dp)
                    .clip(RoundedCornerShape(12.dp))
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
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.subtitle,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White.copy(alpha = 0.7f),
                    maxLines = 1
                )
            }
        }
    }
}

// --- PREVIEW ---
@Preview(showBackground = true, widthDp = 412, heightDp = 800)
@Composable
fun MainAppPreview() {
    MainAppScreen()
}
