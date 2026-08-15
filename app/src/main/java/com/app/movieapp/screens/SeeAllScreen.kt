package com.app.movieapp.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.app.movieapp.R
import com.app.movieapp.data.viewmodel.HomeViewModel
import com.app.movieapp.graph.MovieAppScreen
import com.app.movieapp.models.Movies
import com.app.movieapp.screens.Componets.ErrorStrip
import com.app.movieapp.screens.Componets.MovieItemSeeAll
import com.app.movieapp.utlis.CenteredTopBar
import com.app.movieapp.utlis.Constants.Companion.discoverListScreen
import com.app.movieapp.utlis.Constants.Companion.nowPlayingAllListScreen
import com.app.movieapp.utlis.Constants.Companion.popularAllListScreen
import com.app.movieapp.utlis.Constants.Companion.upcomingListScreen
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SeeAllScreen(
    selectedTitle: String,
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel()
) {
    BackHandler {
        navController.popBackStack()
    }

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
        topBar = {
            CenteredTopBar(
                title = title,
                onClickSearch = { navController.navigate(MovieAppScreen.MOVIE_SEARCH.route) },
                onClickBack = { navController.navigate(MovieAppScreen.MOVIE_HOME.route) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface)
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

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GenreWiseMoviesScreen(
    genId: String,
    genName: String,
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel()
) {
    // Safely trigger side effect when genId changes
    LaunchedEffect(genId) {
        genId.toIntOrNull()?.let { viewModel.setGenreData(it) }
    }

    BackHandler {
        navController.popBackStack()
    }

    val genresWiseMoviePagination = viewModel.genresWiseMovieListState?.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            CenteredTopBar(
                title = genName,
                onClickSearch = { navController.navigate(MovieAppScreen.MOVIE_SEARCH.route) },
                onClickBack = { navController.navigate(MovieAppScreen.MOVIE_HOME.route) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            genresWiseMoviePagination?.let { pagingItems ->
                MovieGridContent(
                    pagingItems = pagingItems,
                    navController = navController
                )
            }
        }
    }
}

/**
 * Reusable Paginated Movie Grid Component with full-span indicators
 */
@Composable
private fun MovieGridContent(
    pagingItems: LazyPagingItems<Movies>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyGridState()

    LazyVerticalGrid(
        state = listState,
        contentPadding = PaddingValues(16.dp),
        columns = GridCells.Adaptive(150.dp),
        modifier = modifier.fillMaxSize()
    ) {
        // Items
        items(
            count = pagingItems.itemCount,
            key = { index -> pagingItems[index]?.id ?: index }
        ) { index ->
            pagingItems[index]?.let { movie ->
                MovieItemSeeAll(
                    media = movie,
                    navController = navController
                )
            }
        }

        // Loading and Error States spanning the entire grid width
        when (val refreshState = pagingItems.loadState.refresh) {
            is LoadState.Loading -> {
                header {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
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
                        CircularProgressIndicator()
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

fun LazyGridScope.header(
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(span = { GridItemSpan(maxLineSpan) }, content = content)
}