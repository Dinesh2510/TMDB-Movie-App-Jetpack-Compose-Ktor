package com.app.movieapp.screens.Componets

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Scale
import coil3.size.Size
import coil3.toBitmap
import com.app.movieapp.R
import com.app.movieapp.graph.MovieAppScreen
import com.app.movieapp.models.Movies
import com.app.movieapp.utlis.Constants
import com.app.movieapp.utlis.Constants.Companion.BASE_POSTER_IMAGE_URL
import com.app.movieapp.utlis.netflixFamily

import androidx.compose.foundation.layout.fillMaxHeight

import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Surface

import androidx.compose.ui.draw.shadow
import com.app.movieapp.data.local.WatchListModel

import com.app.movieapp.ui.theme.TmdbCinematicTheme
class MovieUIComponents : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

        }
    }
}

@Composable
fun HomeThumbWithTitle(
    homeMediaUI: Movies,
    onClickMovies: () -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.normal_padding_half))
            .width(dimensionResource(id = R.dimen.home_grid_card_width))
            .height(dimensionResource(id = R.dimen.home_grid_card_height)), onClick = {
            onClickMovies()
        }
    ) {
        Column {
            AsyncImage(
                model = Constants.BASE_POSTER_IMAGE_URL + homeMediaUI.posterPath,
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.placeholder),
                error = painterResource(id = R.drawable.ic_broken_image),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.height(dimensionResource(id = R.dimen.home_grid_poster_height))
            )
            homeMediaUI.title?.let {
                Text(
                    text = it,
                    fontFamily = netflixFamily,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.small_padding))
                        .fillMaxSize()
                        .wrapContentHeight(align = CenterVertically),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
fun HomeThumbRectWithTitle(
    imageUrl: String,
    title: String,
    OpenDetailsPage: () -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.normal_padding_half))
            .width(220.dp)
            .height(130.dp), onClick = {
            OpenDetailsPage()
        }
    ) {
        Box( // Use Box for layering elements
            modifier = Modifier.fillMaxSize()
        ) {
            // Background image
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.placeholder),
                error = painterResource(id = R.drawable.ic_broken_image),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            // Overlay with gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.8f)
                            )
                        )
                    )
            )

            // Title on bottom
            Text(
                text = title,
                fontFamily = netflixFamily,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.small_padding))
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                color = Color.White // Set white color for text on overlay
            )
            /* Text(
                    text = "$year • $genre • $duration",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Start
                    )
                )*/
        }
    }
}




@Composable
fun SearchMovieCard(
    imageUrl: String,
    title: String?,
    overview: String?, // Acts as release date or description
    onCardClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(115.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color.Black.copy(alpha = 0.5f)
            )
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, TmdbCinematicTheme.GlassBorderGradient, RoundedCornerShape(20.dp))
            .clickable { onCardClick() },
        colors = CardDefaults.cardColors(containerColor = TmdbCinematicTheme.GlassSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Poster Thumbnail Frame
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .fillMaxHeight()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.05f))
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // 2. Movie Info Details Column
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(vertical = 14.dp, horizontal = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                // Title
                Text(
                    text = title ?: "Untitled",
                    color = TmdbCinematicTheme.TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Release Date or Overview Line with Icon
                if (!overview.isNullOrBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                       Text(
                            text = overview,
                            color = TmdbCinematicTheme.TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // 3. Right Action Arrow Indicator
            Box(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.05f))
                    .border(1.dp, Color.White.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "Open Details",
                    tint = TmdbCinematicTheme.TextSecondary,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Composable
fun SavedMovieCard(
    item: WatchListModel,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit
) {
    val posterUrl = item.posterPath?.let { "$BASE_POSTER_IMAGE_URL$it" }
        ?: item.backdropPath?.let { "$BASE_POSTER_IMAGE_URL$it" }
        ?: ""

    val cardShape = RoundedCornerShape(18.dp)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(134.dp)
            .clip(cardShape)
            .clickable { onCardClick() }
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color.Black.copy(alpha = 0.6f)
            )
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, TmdbCinematicTheme.GlassBorderGradient, RoundedCornerShape(20.dp)),
        shape = cardShape,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131927).copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.15f),
                            Color.White.copy(alpha = 0.03f)
                        )
                    ),
                    shape = cardShape
                )
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Movie Poster (Cinema 2:3 Ratio)
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(2f / 3f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1E2638))
                ) {
                    AsyncImage(
                        model = posterUrl,
                        contentDescription = item.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // 2. Movie Information & Metadata
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Header: Title & Forward Arrow
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color.White,
                                    letterSpacing = 0.2.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )

                            Box(
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.05f))
                                    .border(1.dp, Color.White.copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                    contentDescription = "Open Details",
                                    tint = TmdbCinematicTheme.TextSecondary,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }

                        // Synopsis / Overview or Genres fallback (2 lines)
                        val displayDescription = item.overview.ifBlank { item.genres }
                        if (displayDescription.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = displayDescription,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    color = Color(0xFF94A3B8)
                                ),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Metadata Badges Footer (Rating, Year, Duration, Language)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        // Rating Pill
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFFFB800).copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Star,
                                contentDescription = "Rating",
                                tint = Color(0xFFFFB800),
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = String.format("%.1f", item.rating),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFC72C),
                                    fontSize = 11.sp
                                )
                            )
                        }

                        // Release Year
                        val releaseYear = item.releaseDate.take(4)
                        if (releaseYear.isNotBlank()) {
                            MetadataPill(text = releaseYear)
                        }

                        // Duration
                        item.runtime?.let { duration ->
                            if (duration > 0) {
                                MetadataPill(text = "${duration}m")
                            }
                        }

                        // Language
                        if (item.originalLanguage.isNotBlank()) {
                            MetadataPill(text = item.originalLanguage.take(3).uppercase())
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MetadataPill(text: String) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = Color.White.copy(alpha = 0.08f)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium,
                color = Color(0xFFCBD5E1),
                fontSize = 10.sp
            ),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        )
    }
}
@Composable
fun HomeSmallThumb(imageUrl: String, OpenDetailsPage: () -> Unit) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(180.dp)
            .clickable(onClick = {
                Log.e("TAG", "HomeSmallThumb: ")
                OpenDetailsPage()
            }),
        border = BorderStroke(
            width = 1.dp, color = MaterialTheme.colorScheme.primary.copy(0.5f)
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (imageUrl != null) {

                Image(
                    modifier = Modifier
                        .fillMaxSize(),
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current).data(data = imageUrl)
                            .apply(block = fun ImageRequest.Builder.() {
                                size(Size.ORIGINAL)
                                scale(Scale.FILL)
                                crossfade(true)
                            }).build()
                    ),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = null
                )
            } else {

                Image(
                    modifier = Modifier
                        .fillMaxSize(),
                    painter = painterResource(id = R.drawable.placeholder_error),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = null
                )
            }

        }
    }
    Spacer(modifier = Modifier.width(16.dp))
}

@Composable
fun HomeGenre(name: String, onclick: () -> Unit) {
    val defaultDominantColor = MaterialTheme.colorScheme.primaryContainer
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.secondaryContainer,
                        dominantColor
                    )
                )
            )
            .clickable {
                onclick()
            }
    ) {
        Text(
            modifier = Modifier
                .padding(
                    horizontal = 12.dp,
                    vertical = 4.dp
                ),
            text = name,
            fontFamily = netflixFamily,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            color = Color.White,
            overflow = TextOverflow.Ellipsis
        )
    }

    Spacer(modifier = Modifier.width(8.dp))
}

@Composable
fun HomeHeader(title: String, onClick: () -> Unit, showMore: Boolean) {
    var iconPos by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontFamily = netflixFamily,
                fontWeight = FontWeight.Normal,
            )
            if (title == "Popular Movies")
                IconButton(onClick = { iconPos = !iconPos }) {
                    Icon(
                        imageVector = if (iconPos) Icons.Default.ArrowDropUp else Icons.Default.Info,
                        contentDescription = "Button"
                    )
                }
        }
        if (showMore) {
            TextButton(onClick = { onClick() }) {
                Text(
                    text = stringResource(R.string.movies_more),
                    color = Color.White,
                    fontFamily = netflixFamily,
                    fontWeight = FontWeight.Normal,
                    style = MaterialTheme.typography.titleSmall
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = null
                )
            }
        }
    }

    AnimatedVisibility(visible = iconPos) {
        Box(
            Modifier
                .padding(start = 16.dp, bottom = 16.dp)
                .border(1.dp, Color.White)
                .clip(RoundedCornerShape(8.dp))
        ) {
            Text(
                text = "This listing has Paging 3",
                style = MaterialTheme.typography.titleMedium,
                fontFamily = netflixFamily,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(8.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
}





@Composable
fun ErrorStrip(message: String) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
                .padding(8.dp)
        ) {
            Image(
                modifier = Modifier
                    .clip(CircleShape)
                    .width(42.dp)
                    .height(42.dp),
                painter = painterResource(id = R.drawable.baseline_error_24),
                contentDescription = "",
                colorFilter = ColorFilter.tint(Color.White)
            )
            Text(
                color = Color.White,
                text = message,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp)
                    .align(CenterVertically)
            )
        }
    }
}

@Composable
fun MovieItemSeeAll(
    media: Movies,
    navController: NavController,
    modifier: Modifier = Modifier,
) {

    val imageUrl = "${BASE_POSTER_IMAGE_URL}${media.posterPath}"

    val title = media.title

    val imagePainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .size(Size.ORIGINAL)
            .build()
    )
    val imageState = imagePainter.state

    val defaultDominantColor = MaterialTheme.colorScheme.primaryContainer
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    Box(
        modifier = modifier.padding(
            bottom = 16.dp,
            start = 8.dp,
            end = 8.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.secondaryContainer,
                            dominantColor
                        )
                    )
                )
                .clickable {
                    navController.navigate(MovieAppScreen.MOVIE_HOME_DETAILS.route + "/${media.id}")
                }
        ) {

            Box(
                modifier = Modifier
                    .height(240.dp)
                    .fillMaxSize()
                    .padding(6.dp)
            ) {

                if (imageState is AsyncImagePainter.State.Success) {
                    Image(
                        painter = imageState.painter,
                        contentDescription = title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.background)
                    )
                }

                if (imageState is AsyncImagePainter.State.Error) {
                    dominantColor = MaterialTheme.colorScheme.primary
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.background)
                            .padding(32.dp)
                            .alpha(0.8f),
                        painter = painterResource(id = R.drawable.ic_broken_image),
                        contentDescription = title,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }


                if (imageState is AsyncImagePainter.State.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(150.dp)
                            .align(Alignment.Center)
                            .scale(0.5f)
                    )
                }
            }

            Text(
                modifier = Modifier
                    .padding(
                        horizontal = 12.dp,
                        vertical = 4.dp
                    ),
                text = title?:"",
                fontFamily = netflixFamily,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                color = Color.White,
                overflow = TextOverflow.Ellipsis,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 4.dp,
                        start = 11.dp,
                        end = 16.dp,
                        bottom = 8.dp
                    ),
                verticalAlignment = CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC700),
                        modifier = Modifier
                            .width(18.dp)
                            .height(18.dp)
                    )

                    Text(
                        modifier = Modifier
                            .padding(
                                horizontal = 4.dp
                            ),
                        text = media.voteAverage.toString().take(3),
                        fontFamily = netflixFamily,
                        fontSize = 14.sp,
                        maxLines = 1,
                        color = Color.LightGray
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: SwipeToDismissBoxState) {
    val color = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> Color.Transparent
        SwipeToDismissBoxValue.EndToStart -> Color(0xFFFF1744)
        SwipeToDismissBoxValue.Settled -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(12.dp, 8.dp),
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        /*Icon(
            // make sure add baseline_archive_24 resource to drawable folder
            painter = painterResource(R.drawable.ic_home),
            contentDescription = "Archive"
        )*/
        Spacer(modifier = Modifier)

        Icon(
            Icons.Default.Delete,
            contentDescription = "delete"
        )
    }
}