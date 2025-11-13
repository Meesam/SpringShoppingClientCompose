package com.meesam.springshoppingclient.views.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AirportShuttle
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.HeadsetMic
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.meesam.springshoppingclient.R
import com.meesam.springshoppingclient.events.UserProfileEvent
import com.meesam.springshoppingclient.navigation.AppDestinations
import com.meesam.springshoppingclient.navigation.ProfileSubDestinations
import com.meesam.springshoppingclient.states.AppState
import com.meesam.springshoppingclient.viewmodel.ProfileViewModel
import com.meesam.springshoppingclient.views.payment.PaymentSettingScreen
import com.meesam.springshoppingclient.views.theme.AppTheme
import com.meesam.springshoppingclient.views.theme.subHeading


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    onSignOut: () -> Unit,
    mainNavController: NavHostController
) {
    val userProfile by profileViewModel.userProfile.collectAsState()
    val profileNavController = rememberNavController()
    val backStackEntry by profileNavController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        topBar = {
            if (currentRoute != ProfileSubDestinations.PROFILE_DETAILS) {
                MyTopAppBar(profileNavController, currentRoute)
            }
        },

        ) { paddingValues ->
        NavHost(
            navController = profileNavController,
            startDestination = ProfileSubDestinations.PROFILE_DETAILS,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(ProfileSubDestinations.PROFILE_DETAILS) {
                when (val result = userProfile) {
                    is AppState.Error -> {
                        Column(
                            modifier = modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(result.errorMessage.toString())
                        }
                    }

                    is AppState.Idle -> {}
                    is AppState.Loading -> {
                        Column(
                            modifier = modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is AppState.Success -> {
                        if (result.data != null) {
                            ProfileUi(
                                name = result.data.name,
                                email = result.data.email,
                                dob = result.data.dob,
                                profilePicture = result.data.profilePicUrl,
                                profileViewModel = profileViewModel,
                                profileNavController = profileNavController,
                                onClick = onSignOut,
                            )
                        }
                    }
                }
            }
            composable(ProfileSubDestinations.EDIT_PROFILE) {
                EditProfileScreen(mainNavController = mainNavController)
            }
            composable(ProfileSubDestinations.CHANGE_PASSWORD) {
                ChangePasswordScreen()
            }
            composable(ProfileSubDestinations.NOTIFICATION_SETTINGS) {
                NotificationSettingScreen()
            }
            composable(ProfileSubDestinations.PAYMENT_SETTINGS) {
                PaymentSettingScreen()
            }
            composable(ProfileSubDestinations.ADDRESSES_ROUTE) {
                AddressScreen(mainNavController = mainNavController)
            }
        }
    }

}

data class ProfileScreenOptions(
    val title: String,
    val icon: ImageVector,
    //val content: @Composable (ColumnScope.() -> Unit)
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileUi(
    modifier: Modifier = Modifier,
    name: String,
    email: String,
    dob: String,
    profilePicture: String?,
    profileViewModel: ProfileViewModel,
    profileNavController: NavHostController,
    onClick: () -> Unit,

    ) {
    val optionList = remember {
        mutableListOf(
            ProfileScreenOptions(
                title = "Edit Profile",
                icon = Icons.Default.AccountCircle,
                route = ProfileSubDestinations.EDIT_PROFILE
            ),
            ProfileScreenOptions(
                title = "Change Password",
                icon = Icons.Default.Lock,
                route = ProfileSubDestinations.CHANGE_PASSWORD
            ),
            ProfileScreenOptions(
                title = "Notifications",
                icon = Icons.Default.Notifications,
                route = ProfileSubDestinations.NOTIFICATION_SETTINGS
            ),
            ProfileScreenOptions(
                title = "Language",
                icon = Icons.Default.Language,
                route = ProfileSubDestinations.EDIT_PROFILE
            ),
            ProfileScreenOptions(
                title = "Security",
                icon = Icons.Default.Security,
                route = ProfileSubDestinations.EDIT_PROFILE
            ),
        )
    }

    /*if (activeSheet != null) {
        ModalBottomSheet(
            onDismissRequest = {
                profileViewModel.onEvent(UserProfileEvent.OnDismissSheet)
            },
            sheetState = sheetState,
            modifier = Modifier.fillMaxHeight(),
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ) {
            activeSheet?.content?.invoke(this)
        }
    }*/


    LazyColumn(
        modifier = Modifier

            .fillMaxSize()
            .padding(vertical = 16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(subHeading.copy(0.2f), shape = RoundedCornerShape(10.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),

                    ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(70.dp)
                            .background(
                                if (profilePicture.isNullOrEmpty()) Color.DarkGray else Color.Transparent,
                                shape = CircleShape
                            )
                            .border(
                                BorderStroke(
                                    0.5.dp,
                                    SolidColor(MaterialTheme.colorScheme.background)
                                ),
                                shape = CircleShape
                            )
                    ) {
                        if (profilePicture.isNullOrEmpty()) {
                            Text(
                                name.first().toString(),
                                color = subHeading,
                                style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 30.sp)
                            )
                        } else {
                            AsyncImage(
                                model = profilePicture,
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(shape = CircleShape), // Fill the Box
                                contentScale = ContentScale.FillBounds, // Or ContentScale.Fit, as needed
                                placeholder = painterResource(id = R.drawable.ic_launcher_background), // Optional: Placeholder
                                error = painterResource(id = R.drawable.ic_launcher_foreground) // Optional: Error image
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column() {
                        Text(
                            name,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontFamily = FontFamily(Font(R.font.nunito_bold))
                            ),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            dob, style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = FontFamily(Font(R.font.nunito_bold))
                            ), color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                        )
                        Text(
                            email, style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = FontFamily(Font(R.font.nunito_bold))
                            ), color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                        )
                    }

                }
            }
        }

        item {
            OrderBlock()
        }

        item {
            Text(
                "Account Settings", style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FontFamily(Font(R.font.nunito_bold)),
                )
            )
        }
        items(optionList) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(indication = null, interactionSource = null) {
                        profileNavController.navigate(it.route)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        it.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary.copy(1f)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        it.title, style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontFamily(Font(R.font.nunito_semibold))
                        ), color = MaterialTheme.colorScheme.onSurface.copy(0.7f)
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Default.KeyboardArrowRight,
                        contentDescription = "Open",
                        tint = MaterialTheme.colorScheme.onSurface.copy(0.7f)
                    )
                }
            }
        }

        item {
            PaymentBlock(profileNavController = profileNavController)
        }

        item {
            AddressBlock(profileNavController = profileNavController)
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onClick, modifier = Modifier.fillMaxWidth(), colors = ButtonColors(
                        containerColor = Color.Red, contentColor = Color.White,
                        disabledContainerColor = Color.Red,
                        disabledContentColor = Color.White
                    )
                ) {
                    Text("Sign Out")
                }
            }
        }
        item {
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePrev() {
    AppTheme {
        OrderBlock()
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentBlockPrev() {
    AppTheme {
        // PaymentBlock()
    }
}

@Composable
fun OrderBlock(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = {},
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    Icons.Outlined.AirportShuttle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(8.dp))
                Text("Orders", fontFamily = FontFamily(Font(R.font.nunito_bold)))
            }

            Spacer(Modifier.width(16.dp))

            OutlinedButton(
                onClick = {},
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(8.dp))
                Text("Wishlist", fontFamily = FontFamily(Font(R.font.nunito_bold)))
            }
        }

        Row(modifier = modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = {},
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    Icons.Outlined.CardGiftcard,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(8.dp))
                Text("Coupons", fontFamily = FontFamily(Font(R.font.nunito_bold)))
            }
            Spacer(Modifier.width(16.dp))

            OutlinedButton(
                onClick = {},
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    Icons.Outlined.HeadsetMic,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(8.dp))
                Text("Help Center", fontFamily = FontFamily(Font(R.font.nunito_bold)))
            }
        }
    }

}

@Composable
fun PaymentBlock(modifier: Modifier = Modifier, profileNavController: NavHostController) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(subHeading.copy(0.2f), shape = RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        Text(
            "Payment Settings", style = MaterialTheme.typography.titleLarge.copy(
                fontFamily = FontFamily(Font(R.font.nunito_bold)),
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(indication = null, interactionSource = null) {
                    profileNavController.navigate(ProfileSubDestinations.PAYMENT_SETTINGS)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Payment,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary.copy(1f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    "Saved Payment Methods", style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = FontFamily(Font(R.font.nunito_semibold))
                    ), color = MaterialTheme.colorScheme.onSurface.copy(0.7f)
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = "Open",
                    tint = MaterialTheme.colorScheme.onSurface.copy(0.7f)
                )
            }
        }
    }
}

@Composable
fun AddressBlock(modifier: Modifier = Modifier,profileNavController: NavHostController) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(subHeading.copy(0.2f), shape = RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        Text(
            "Address Settings", style = MaterialTheme.typography.titleLarge.copy(
                fontFamily = FontFamily(Font(R.font.nunito_bold)),
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(indication = null, interactionSource = null) {
                    profileNavController.navigate(ProfileSubDestinations.ADDRESSES_ROUTE)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary.copy(1f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    "Saved Addresses", style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = FontFamily(Font(R.font.nunito_semibold))
                    ), color = MaterialTheme.colorScheme.onSurface.copy(0.7f)
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = "Open",
                    tint = MaterialTheme.colorScheme.onSurface.copy(0.7f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(navController: NavHostController, currentRoute: String?) {
    val title = when (currentRoute) {
        ProfileSubDestinations.CHANGE_PASSWORD -> "Change Password"
        ProfileSubDestinations.EDIT_PROFILE -> "Edit Profile"
        ProfileSubDestinations.PAYMENT_SETTINGS -> "Payment Settings"
        ProfileSubDestinations.NOTIFICATION_SETTINGS -> "Notifications Settings"
        ProfileSubDestinations.ADDRESSES_ROUTE -> "Address Settings"
        else -> ""
    }

    // Determine if the Back button should be shown (i.e., we are NOT on the start screen)
    val canPop = navController.previousBackStackEntry != null

    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = {
            if (canPop) {
                // 3. Implement the Back Button logic
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        // You can add more actions here (e.g., a menu icon)
        /*actions = {
            // Example: A settings icon only visible on the Home screen
            if (currentRoute == Screen.HOME) {
                IconButton(onClick = { navController.navigate(Screen.SETTINGS) }) {
                    // [Image of a gear icon]
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings"
                    )
                }
            }
        }*/
    )
}