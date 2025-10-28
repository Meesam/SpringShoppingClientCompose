package com.meesam.springshoppingclient.views.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.meesam.springshoppingclient.navigation.AppDestinations
import com.meesam.springshoppingclient.views.cart.CartScreen
import com.meesam.springshoppingclient.views.common.BottomNavigationBar
import com.meesam.springshoppingclient.views.favorite.FavoriteScreen
import com.meesam.springshoppingclient.views.feed.FeedScreen
import com.meesam.springshoppingclient.views.products.ProductScreen
import com.meesam.springshoppingclient.views.profile.ProfileScreen
import com.meesam.springshoppingclient.views.theme.AppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    mainNavController: NavHostController,
    isAdminLoggedIn: Boolean,
    onSignOut: () -> Unit
) {
    // For simpler bottom nav without nested NavHost:
    var currentBottomTabRoute by rememberSaveable { mutableStateOf(AppDestinations.FEED_ROUTE) }
    // Or, if using a nested NavHost for bottom tabs, create a bottomNavController here:
    // val bottomNavController = rememberNavController()

    // 1. State: How much the header has collapsed (offset from its starting position)
    val headerOffset = rememberSaveable { mutableStateOf(0f) }

// 2. Constants: Maximum and minimum heights for the header
    val MaxHeaderHeight = 220.dp
    val MinHeaderHeight = 150.dp // Standard TopAppBar height

    val density = LocalDensity.current

    val MaxHeaderHeightPX = with(density) {
        MaxHeaderHeight.toPx()
    }

    val MinHeaderHeightPX = with(density) {
        MinHeaderHeight.toPx()
    }

    // 3. NestedScrollConnection: The logic to consume the scroll
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val currentOffset = headerOffset.value
                val delta = available.y // Vertical scroll change

                // Calculate the new offset, clamping it between the max and min allowed collapse.
                val newOffset = (currentOffset + delta).coerceIn(
                    MinHeaderHeightPX - MaxHeaderHeightPX, // Max collapse
                    0f // Min collapse (fully expanded)
                )

                // The consumed scroll is the difference in offset
                val consumed = newOffset - currentOffset

                headerOffset.value = newOffset

                return Offset(0f, consumed) // Consume scroll to move the header
            }

            // Fling handlers (onPreFling/onPostFling) are also typically needed for smooth animations
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        bottomBar = {
            AnimatedVisibility(
                visible = headerOffset.value >= 0,
                enter =
                    slideInVertically() +
                            expandVertically() +
                            fadeIn()+
                            scaleIn(initialScale = -1.2f),
                exit = slideOutVertically() + shrinkVertically() + fadeOut() + scaleOut(targetScale = 1.2f)
            ) {
                BottomNavigationBar(
                    currentRoute = currentBottomTabRoute,
                    isAdminLoggedIn = isAdminLoggedIn,
                    onTabSelected = { route ->
                        currentBottomTabRoute = route
                        // If using bottomNavController:
                        // bottomNavController.navigate(route) { launchSingleTop = true; popUpTo(bottomNavController.graph.startDestinationId){ saveState = true } }
                    },
                )
            }
        },
        floatingActionButton = {
            if (currentBottomTabRoute == AppDestinations.CART_ROUTE) {
                Button(onClick = {
                    mainNavController.navigate(AppDestinations.CHECKOUT_ROUTE)
                }) {
                    Text("Checkout")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End

    ) { paddingValues ->

        Box(
            modifier = modifier
               // .padding(paddingValues)
                .nestedScroll(nestedScrollConnection)
        ) {
            when (currentBottomTabRoute) {
                AppDestinations.PRODUCT_ROUTE -> ProductScreen() {
                    mainNavController.navigate(AppDestinations.productDetailRoute(it))
                }

                AppDestinations.PROFILE_ROUTE -> ProfileScreen(
                    mainNavController = mainNavController,
                    onSignOut = onSignOut
                )

                AppDestinations.FEED_ROUTE -> FeedScreen(
                    //modifier.padding(paddingValues),
                  maxHeaderHeight =   MaxHeaderHeight,
                   minHeaderHeight =  MinHeaderHeight,
                   currentOffset =  headerOffset.value,
                    mainNavController = mainNavController
                ) {
                    mainNavController.navigate(AppDestinations.productDetailRoute(it))
                }

                AppDestinations.FAVORITE_ROUTE -> FavoriteScreen()
                AppDestinations.CART_ROUTE -> CartScreen()
            }
        }
    }
}

@Composable
@Preview
fun AppBarPrev(modifier: Modifier = Modifier) {
    AppTheme {
        val mainNavController = rememberNavController()
        HomeScreen(
            mainNavController = mainNavController,
            isAdminLoggedIn = false,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {}
    }
}