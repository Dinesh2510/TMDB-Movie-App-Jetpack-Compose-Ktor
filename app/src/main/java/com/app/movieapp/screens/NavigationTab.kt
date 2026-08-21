package com.app.movieapp.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.app.movieapp.graph.Graph
import com.app.movieapp.graph.MovieAppScreen
import com.app.movieapp.ui.theme.FrostedGlassTheme


sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Movies : Screen("movies", "Movies", Icons.Filled.Movie)
    object Saved : Screen("saved", "Saved", Icons.Filled.Bookmark)
    object Profile : Screen("profile", "Profile", Icons.Filled.Person)
}

// --- FLOATING LIQUID FROSTED GLASS NAVIGATION BAR ---
@Composable
fun FloatingAirNavigationBar(
    currentRoute: String?,
    onTabSelected: (Screen) -> Unit,
    onSearchClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = remember {
        listOf(Screen.Home, Screen.Movies, Screen.Saved, Screen.Profile)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 12.dp,
                bottom = 16.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // MAIN NAVIGATION PILL CONTAINER
        Box(
            modifier = Modifier
                .height(68.dp)
                .weight(1f)
        ) {
            // LAYER 1: Frosted Glass Background with Shadow & Border
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shadow(
                        elevation = 20.dp,
                        shape = CircleShape,
                        ambientColor = Color.Black.copy(alpha = 0.4f),
                        spotColor = Color.Black.copy(alpha = 0.5f)
                    )
                    .clip(CircleShape)
                    .border(1.5.dp, FrostedGlassTheme.GlassBorderGradient, CircleShape)
                    .background(FrostedGlassTheme.GlassSurfaceColor)
            )

            // LAYER 2: Crisp Active Pill & Icons
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                tabs.forEach { screen ->
                    val isSelected = currentRoute == screen.route

                    val animatedContentColor by animateColorAsState(
                        targetValue = if (isSelected) Color.White else Color.White.copy(alpha = 0.65f),
                        animationSpec = tween(durationMillis = 250),
                        label = "tabContent"
                    )

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
                            .padding(5.dp)
                            .clip(CircleShape)
                            .then(
                                if (isSelected) Modifier.background(FrostedGlassTheme.ActiveGradient)
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

        // SEPARATE SEARCH FAB
        Box(
            modifier = Modifier
                .size(68.dp)
                .shadow(
                    elevation = 20.dp,
                    shape = CircleShape,
                    ambientColor = Color.Black.copy(alpha = 0.4f),
                    spotColor = Color.Black.copy(alpha = 0.5f)
                )
                .clip(CircleShape)
                .border(1.5.dp, FrostedGlassTheme.GlassBorderGradient, CircleShape)
                .background(FrostedGlassTheme.GlassSurfaceColor)
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
fun MainAppScreen(
    rootNavController: NavHostController // Attached to RootNavigation graph
) {
    val bottomNavController = rememberNavController() // Inner bottom tab graph
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            FloatingAirNavigationBar(
                currentRoute = currentRoute,
                onTabSelected = { screen ->
                    bottomNavController.navigate(screen.route) {
                        popUpTo(bottomNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onSearchClicked = {
                    // Navigates using rootNavController (No IllegalArgumentException!)
                    rootNavController.navigate(MovieAppScreen.MOVIE_SEARCH.route)
                }
            )
        }
    ) { _ ->
        NavHost(
            navController = bottomNavController,
            startDestination = Screen.Home.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Screen.Home.route) {
                TmdbHomeScreen(navController =rootNavController )
            }
            composable(Screen.Movies.route) {
                TopRatedScreen(rootNavController)
            }
            composable(Screen.Saved.route) {
                SavedMovieScreen(navController = rootNavController)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onWatchlistClick = {
                        bottomNavController.navigate(Screen.Saved.route) {
                            popUpTo(bottomNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onLogoutClick = {
                        rootNavController.navigate(MovieAppScreen.ONBOARDING.route) {
                            popUpTo(Graph.ROOT) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 850)
@Composable
fun MainAppPreview() {
    MainAppScreen(rememberNavController())
}