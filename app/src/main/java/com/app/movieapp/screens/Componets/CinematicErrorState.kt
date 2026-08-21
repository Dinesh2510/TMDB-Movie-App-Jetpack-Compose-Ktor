package com.app.movieapp.screens.Componets
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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.app.movieapp.data.viewmodel.HomeFeedUIState
import com.app.movieapp.data.viewmodel.HomeViewModel
import com.app.movieapp.graph.MovieAppScreen
import com.app.movieapp.models.Genre
import com.app.movieapp.models.Movies
import com.app.movieapp.ui.theme.TmdbCinematicTheme
import com.app.movieapp.utlis.CenteredCircularProgressIndicator
import com.app.movieapp.utlis.Constants.Companion.BASE_BACKDROP_IMAGE_URL
import com.app.movieapp.utlis.Constants.Companion.BASE_POSTER_IMAGE_URL
import com.app.movieapp.utlis.Constants.Companion.nowPlayingAllListScreen
import com.app.movieapp.utlis.Constants.Companion.popularAllListScreen
import com.app.movieapp.utlis.Constants.Companion.upcomingListScreen
import com.app.movieapp.utlis.GenreImageMapper
import org.koin.androidx.compose.koinViewModel
import kotlin.random.Random
import androidx.compose.animation.core.tween

import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.runtime.LaunchedEffect
import com.app.movieapp.data.viewmodel.ContinueWatchingViewModel
import kotlinx.coroutines.delay

import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.ui.text.style.TextAlign
import com.app.movieapp.data.local.ContinueWatchingModel
import com.app.movieapp.utlis.netflixFamily
@Composable
fun CinematicErrorState(
    errorMessage: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 24.dp,
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = Color.Black.copy(alpha = 0.5f),
                    spotColor = Color.Black.copy(alpha = 0.8f)
                )
                .clip(RoundedCornerShape(28.dp))
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.15f),
                            Color.White.copy(alpha = 0.02f)
                        )
                    ),
                    shape = RoundedCornerShape(28.dp)
                ),
            colors = CardDefaults.cardColors(containerColor = TmdbCinematicTheme.GlassSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Frosted glowing icon frame
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    TmdbCinematicTheme.CoralAccent.copy(alpha = 0.25f),
                                    Color.Transparent
                                )
                            )
                        )
                        .border(
                            width = 1.5.dp,
                            color = TmdbCinematicTheme.CoralAccent.copy(alpha = 0.4f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.WifiOff,
                        contentDescription = "Connection Error",
                        tint = TmdbCinematicTheme.CoralAccent,
                        modifier = Modifier.size(34.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Connection Lost",
                    color = TmdbCinematicTheme.TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.3.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = errorMessage.ifBlank { "Unable to reach TMDB servers. Please check your network and try again." },
                    color = TmdbCinematicTheme.TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(26.dp))

                // Cinematic Retry Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(TmdbCinematicTheme.PrimaryActionGradient)
                        .clickable { onRetryClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Refresh,
                            contentDescription = "Retry",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "TRY AGAIN",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }
    }
}