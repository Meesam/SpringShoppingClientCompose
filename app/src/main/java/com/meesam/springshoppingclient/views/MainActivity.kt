package com.meesam.springshoppingclient.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.meesam.springshoppingclient.navigation.AppDestinations
import com.meesam.springshoppingclient.viewmodel.ProfileViewModel
import com.meesam.springshoppingclient.views.auth.OtpScreen
import com.meesam.springshoppingclient.views.auth.UserLoginScreen
import com.meesam.springshoppingclient.views.auth.UserRegistrationScreen
import com.meesam.springshoppingclient.views.common.DemoTextField
import com.meesam.springshoppingclient.views.home.HomeScreen
import com.meesam.springshoppingclient.views.onboarding.OnBoardingScreen
import com.meesam.springshoppingclient.views.theme.AppTheme
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.meesam.springshoppingclient.events.UserProfileEvent
import com.meesam.springshoppingclient.views.payment.PaymentSettingScreen
import com.meesam.springshoppingclient.views.products.ProductDetailScreen
import com.meesam.springshoppingclient.views.products.ProductScreen
import com.meesam.springshoppingclient.views.profile.CameraScreen
import com.meesam.springshoppingclient.views.profile.EditProfileScreen
import com.meesam.springshoppingclient.views.profile.NewAddressScreen
import com.meesam.springshoppingclient.views.search.SearchSuggestionScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            AppTheme {
                App()
            }
        }
    }
}

@Composable
fun App(){
    Surface(modifier = Modifier
        .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
        .fillMaxSize()) {
        AppNavigation()
    }
}


@Composable
fun AppNavigation() {
    val mainNavController = rememberNavController()
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val isLoadingInitialUser by profileViewModel.isLoadingInitialUser.collectAsState()
    val isUserLoggedIn by profileViewModel.isUserLoggedIn.collectAsState()
    if (isLoadingInitialUser) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        val startDestination = if (isUserLoggedIn) {
            AppDestinations.HOME_ROUTE
        } else {
            AppDestinations.ONBOARDING_ROUTE
            //AppDestinations.HOME_ROUTE
        }

        NavHost(
            navController = mainNavController,
            startDestination = startDestination,
        ) {

            composable(AppDestinations.DEMO_ROUTE) {
                DemoTextField()
            }
            composable(AppDestinations.ONBOARDING_ROUTE) {
                OnBoardingScreen(onNavigateToLogin = {
                    mainNavController.navigate(AppDestinations.LOGIN_ROUTE)
                }, onNavigateToRegister = {
                    mainNavController.navigate(AppDestinations.REGISTER_ROUTE)
                })
            }

            composable(AppDestinations.OTP_ROUTE) {
                OtpScreen(){
                    mainNavController.navigate(AppDestinations.LOGIN_ROUTE) {
                        popUpTo(AppDestinations.OTP_ROUTE) { inclusive = true }
                    }
                }
            }

            composable(AppDestinations.SEARCH_SUGGESTION_ROUTE) {
                SearchSuggestionScreen(){
                    mainNavController.navigate(AppDestinations.HOME_ROUTE) {
                        popUpTo(AppDestinations.SEARCH_SUGGESTION_ROUTE) { inclusive = true }
                    }
                }
            }

            composable(AppDestinations.LOGIN_ROUTE) {
                UserLoginScreen(
                    onLoginSuccess = {
                        mainNavController.navigate(AppDestinations.HOME_ROUTE) {
                            popUpTo(AppDestinations.LOGIN_ROUTE) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        mainNavController.navigate(AppDestinations.REGISTER_ROUTE)
                    }
                )
            }

            composable(AppDestinations.HOME_ROUTE) {
                // Home screen will manage its own content based on bottom nav selection
                HomeScreen(
                    mainNavController = mainNavController,
                    isAdminLoggedIn = false,
                    onSignOut = {
                        profileViewModel.onEvent(UserProfileEvent.OnSignOut)
                        mainNavController.navigate(AppDestinations.LOGIN_ROUTE) {
                            popUpTo(AppDestinations.HOME_ROUTE) { inclusive = true }
                            launchSingleTop = true
                        }
                    }) // Pass mainNavController if Profile needs to navigate outside Home
            }


            composable(AppDestinations.REGISTER_ROUTE) {
                UserRegistrationScreen(
                    onBackToLogin = { mainNavController.popBackStack() },
                    onRegisterSuccess = {
                        mainNavController.navigate(AppDestinations.OTP_ROUTE)
                    }
                )
            }

            /*composable(AppDestinations.CHECKOUT_ROUTE) {
                CheckOutScreen(
                    mainNavController = mainNavController,
                    onBack = { mainNavController.popBackStack() }
                )
            }*/

            composable(AppDestinations.CAMERA_SCREEN_ROUTE) {
                CameraScreen{
                   mainNavController.popBackStack()
                }
            }
            composable(AppDestinations.ADDRESS_SCREEN_ROUTE) {
                NewAddressScreen(){
                    mainNavController.popBackStack()
                }
            }
            composable(AppDestinations.PRODUCT_ROUTE) {
                ProductScreen(
                    onProductClick = {userId ->
                        mainNavController.navigate(AppDestinations.productDetailRoute(userId))
                    }
                )
            }
            composable(
                 route = AppDestinations.PRODUCT_DETAIL_ROUTE_PATTERN,
                 arguments = listOf(
                     navArgument(AppDestinations.PRODUCT_ID_KEY) {
                         type = NavType.LongType
                     }
                 )
             ) {backStackEntry ->
                 val productId = backStackEntry.arguments?.getLong(AppDestinations.PRODUCT_ID_KEY)
                 productId?.let {id->
                     ProductDetailScreen(
                         productId = id,
                         /*onUserAddedSuccessfully = {
                             mainNavController.navigate(AppDestinations.USER_LIST_ROUTE) {
                                 popUpTo(AppDestinations.USER_LIST_ROUTE) {
                                     inclusive =
                                         true // This makes userList the new top, removing previous instances
                                 }
                             }
                         },*/
                         onGoBack = {
                             mainNavController.popBackStack()
                         })
                 }
             }
        }
    }


}

@Composable
@Preview(showBackground = true)
fun AppPreview(modifier: Modifier = Modifier) {
    AppTheme {
        App()
    }
}








