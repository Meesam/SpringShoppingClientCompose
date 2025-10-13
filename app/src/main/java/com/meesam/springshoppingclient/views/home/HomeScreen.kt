package com.meesam.springshoppingclient.views.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.meesam.springshoppingclient.navigation.AppDestinations
import com.meesam.springshoppingclient.views.cart.CartScreen
import com.meesam.springshoppingclient.views.common.AppTopBar
import com.meesam.springshoppingclient.views.common.BottomNavigationBar
import com.meesam.springshoppingclient.views.favorite.FavoriteScreen
import com.meesam.springshoppingclient.views.feed.FeedScreen
import com.meesam.springshoppingclient.views.products.ProductScreen
import com.meesam.springshoppingclient.views.profile.ProfileScreen

@Composable
fun HomeScreen(mainNavController: NavHostController, isAdminLoggedIn: Boolean, onSignOut:()-> Unit) {
    // For simpler bottom nav without nested NavHost:
    var currentBottomTabRoute by rememberSaveable { mutableStateOf(AppDestinations.FEED_ROUTE) }
    // Or, if using a nested NavHost for bottom tabs, create a bottomNavController here:
    // val bottomNavController = rememberNavController()

    Scaffold(
        topBar = {
            Box(modifier = Modifier.padding(16.dp)){
                AppTopBar()
            }

        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentBottomTabRoute,
                isAdminLoggedIn = isAdminLoggedIn,
                onTabSelected = { route ->
                    currentBottomTabRoute = route
                    // If using bottomNavController:
                    // bottomNavController.navigate(route) { launchSingleTop = true; popUpTo(bottomNavController.graph.startDestinationId){ saveState = true } }
                },
            )
        },
        floatingActionButton = {
            if(currentBottomTabRoute == AppDestinations.CART_ROUTE){
                Button(onClick = {
                    mainNavController.navigate(AppDestinations.CHECKOUT_ROUTE)
                }) {
                    Text("Checkout")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End

    ) { paddingValues ->
        // Content based on selected bottom tab
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentBottomTabRoute) {
                AppDestinations.PRODUCT_ROUTE -> ProductScreen() {
                    mainNavController.navigate(AppDestinations.productDetailRoute(it))
                }
                AppDestinations.PROFILE_ROUTE -> ProfileScreen(onSignOut = onSignOut)
                AppDestinations.FEED_ROUTE -> FeedScreen(){
                    mainNavController.navigate(AppDestinations.productDetailRoute(it))
                }
                AppDestinations.FAVORITE_ROUTE -> FavoriteScreen()
                AppDestinations.CART_ROUTE -> CartScreen()
            }
        }
    }
}