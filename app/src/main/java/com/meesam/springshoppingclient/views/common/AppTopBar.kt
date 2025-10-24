package com.meesam.springshoppingclient.views.common

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
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.navigation.NavHostController
import com.meesam.springshoppingclient.R
import com.meesam.springshoppingclient.navigation.AppDestinations
import com.meesam.springshoppingclient.views.category.CategoryListScreen
import com.meesam.springshoppingclient.views.theme.AppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    mainNavController: NavHostController,
    currentOffset: Float,
    maxHeaderHeight: Dp,
    minHeaderHeight: Dp
) {

    val density = LocalDensity.current
    val maxHeaderHeightPX = with(density){
        maxHeaderHeight.toPx()
    }

    val minHeaderHeightPX = with(density){
        minHeaderHeight.toPx()
    }

    val maxOffset = minHeaderHeightPX - maxHeaderHeightPX

    val currentHeight: Dp = with(density) {
        (maxHeaderHeightPX + currentOffset).coerceAtLeast(minHeaderHeightPX).toDp()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(currentHeight)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f),
                    )
                )
            )
            .clipToBounds() // Important to prevent drawing outside bounds
    ) {
        // We use a Column to arrange the scrolling content above the static CategoryList
        Column(Modifier.fillMaxSize()) {

            // PART 1: The content that scrolls up and disappears.
            // We wrap it in a Box with weight to take up all available space above the categories.
            // clipToBounds here is crucial to hide the content as it scrolls up.
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clipToBounds(),
                contentAlignment = Alignment.BottomCenter // Keep content at the bottom of its space
            ) {
                // This is the Column that actually moves
                Column(
                    modifier = Modifier
                        .graphicsLayer {
                            // Clamp the translation so it stops at its final position
                            translationY = 0.3f
            }
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val progress = (1f - (currentOffset / maxOffset)).coerceIn(0f, 1f)

                    // This Row fades out
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(progress) // Fades out as it scrolls up
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f, fill = false)
                        ) {
                            Icon(
                                Icons.Outlined.LocationOn,
                                contentDescription = "Location",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                "H.no.19, Gali 10, parwana road...",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.ChevronRight,
                                contentDescription = "Change Location",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                Icons.Outlined.Notifications,
                                contentDescription = "Notifications",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))

                    // This search bar moves up with the location row
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.onPrimary,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.onPrimary,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 12.dp)
                            .clickable { mainNavController.navigate(AppDestinations.SEARCH_SUGGESTION_ROUTE) }
                    ) {
                        Icon(
                            Icons.Outlined.Search, contentDescription = "Search",
                            modifier = Modifier.align(Alignment.CenterStart),
                        )
                        Text(
                            "Search",
                            modifier = Modifier.padding(start = 30.dp),
                            style = LocalTextStyle.current
                        )
                    }
                    // Spacer to create space between search and categories when expanded
                    Spacer(Modifier.height(16.dp))
                }
            }


            // PART 2: The static content that is always visible.
            // This is placed outside the scrolling Column, so it is not affected by the graphicsLayer translation.
            // The `progress` value is passed to it so it can fade in its own elements if needed.
            val progress = 1f - (currentOffset / (minHeaderHeightPX - maxHeaderHeightPX))
            CategoryListScreen(
                modifier = Modifier.padding(horizontal = 16.dp),
                progress = progress.coerceIn(0f,1f)
            )
        }
    }


}


@Preview(showBackground = true)
@Composable
fun AppTopBarPrev() {
    AppTheme {

    }
}