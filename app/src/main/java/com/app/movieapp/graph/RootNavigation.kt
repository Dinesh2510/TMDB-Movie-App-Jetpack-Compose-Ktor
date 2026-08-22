package com.app.movieapp.graph

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.movieapp.screens.GenreWiseMoviesScreen
import com.app.movieapp.screens.MainAppScreen
import com.app.movieapp.screens.MovieDetailsScreen
import com.app.movieapp.screens.SavedMovieScreen
import com.app.movieapp.screens.ScreenAbout
import com.app.movieapp.screens.SearchScreen
import com.app.movieapp.screens.SeeAllScreen
import com.app.movieapp.screens.SplashScreen

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.movieapp.data.viewmodel.AuthViewModel
import com.app.movieapp.screens.AiMovieChatScreen
import com.app.movieapp.screens.GenreWiseMoviesScreen
import com.app.movieapp.screens.LoginScreen
import com.app.movieapp.screens.MainAppScreen
import com.app.movieapp.screens.MovieDetailsScreen
import com.app.movieapp.screens.OldMovieHomeScreen
import com.app.movieapp.screens.OnboardingScreen
import com.app.movieapp.screens.ProfileScreen
import com.app.movieapp.screens.RegisterScreen
import com.app.movieapp.screens.SavedMovieScreen
import com.app.movieapp.screens.ScreenAbout
import com.app.movieapp.screens.SearchScreen
import com.app.movieapp.screens.SeeAllScreen
import com.app.movieapp.screens.SplashScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun RootNavigation(
    authViewModel: AuthViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val isOnboardingCompleted by authViewModel.isOnboardingCompleted.collectAsState()

    val MOVIE_ID_ARG = "movieId"
    val SeeAllTags = "seeAllTags"
    val genreId = "genId"
    val genreName = "genName"

    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = MovieAppScreen.SPLASH.route
    ) {
        composable(route = MovieAppScreen.SPLASH.route) {
            SplashScreen({
                val targetRoute = when {

                    isLoggedIn -> MovieAppScreen.MOVIE_HOME.route

                    isOnboardingCompleted -> MovieAppScreen.LOGIN.route

                    else -> MovieAppScreen.ONBOARDING.route

                }

                navController.navigate(targetRoute) {

                    popUpTo(MovieAppScreen.SPLASH.route) { inclusive = true }

                }
            })
        }

        composable(route = MovieAppScreen.ONBOARDING.route) {
            OnboardingScreen(
                onCreateAccountClick = {
                    navController.navigate(MovieAppScreen.REGISTER.route)
                },
                onLoginClick = {
                    navController.navigate(MovieAppScreen.LOGIN.route)
                }
            )
        }

        composable(route = MovieAppScreen.REGISTER.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(MovieAppScreen.MOVIE_HOME.route) {
                        popUpTo(MovieAppScreen.ONBOARDING.route) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.navigate(MovieAppScreen.LOGIN.route) {
                        popUpTo(MovieAppScreen.REGISTER.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = MovieAppScreen.LOGIN.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(MovieAppScreen.MOVIE_HOME.route) {
                        popUpTo(MovieAppScreen.ONBOARDING.route) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(MovieAppScreen.REGISTER.route) {
                        popUpTo(MovieAppScreen.LOGIN.route) { inclusive = true }
                    }
                }
            )
        }



        composable(
            route = MovieAppScreen.MOVIE_HOME_DETAILS.route + "/{$MOVIE_ID_ARG}",
            arguments = listOf(navArgument(MOVIE_ID_ARG) { type = NavType.StringType })
        ) {
            MovieDetailsScreen(navController, it.arguments?.getString(MOVIE_ID_ARG) ?: "1")
        }

        composable(
            route = MovieAppScreen.MOVIE_SEE_ALL.route + "/{$SeeAllTags}",
            arguments = listOf(navArgument(SeeAllTags) { type = NavType.StringType })
        ) {
            SeeAllScreen(it.arguments?.getString(SeeAllTags) ?: "1", navController)
        }

        composable(
            route = MovieAppScreen.MOVIE_GENRE_WISE.route + "/{$genreId}/{$genreName}",
            arguments = listOf(
                navArgument(genreId) { type = NavType.StringType },
                navArgument(genreName) { type = NavType.StringType }
            )
        ) {
            GenreWiseMoviesScreen(
                it.arguments?.getString(genreId) ?: "1",
                it.arguments?.getString(genreName) ?: "",
                navController
            )
        }

        composable(route = MovieAppScreen.MOVIE_SEARCH.route) {
            SearchScreen(navController = navController)
        }

        composable(route = MovieAppScreen.MOVIE_AI.route) {
            AiMovieChatScreen(navController = navController)
        }

        composable(route = MovieAppScreen.MOVIE_WATCHLIST.route) {
            SavedMovieScreen(navController = navController)
        }
// Inside RootNavigation.kt
        composable(route = MovieAppScreen.MOVIE_HOME.route) {
            // Pass the root navController here!
            //OldMovieHomeScreen( navController)
                   MainAppScreen(rootNavController = navController)
        }
        composable(route = MovieAppScreen.MOVIE_ABOUT.route) {
            ScreenAbout()
        }
    }
}

sealed class MovieAppScreen(val route: String) {
    object SPLASH : MovieAppScreen(route = "splash")
    object ONBOARDING : MovieAppScreen(route = "onboarding")
    object LOGIN : MovieAppScreen(route = "login")
    object REGISTER : MovieAppScreen(route = "register")
    object MOVIE_HOME : MovieAppScreen(route = "home")
    object MOVIE_HOME_DETAILS : MovieAppScreen(route = "homeDetails")
    object MOVIE_SEE_ALL : MovieAppScreen(route = "seeAll")
    object MOVIE_GENRE_WISE : MovieAppScreen(route = "genreWiseMovie")
    object MOVIE_SEARCH : MovieAppScreen(route = "search")
    object MOVIE_ABOUT : MovieAppScreen(route = "about")
    object MOVIE_WATCHLIST : MovieAppScreen(route = "watch")
    object MOVIE_AI : MovieAppScreen(route = "movieAI")
}

object Graph {
    const val ROOT = "root_graph"
}