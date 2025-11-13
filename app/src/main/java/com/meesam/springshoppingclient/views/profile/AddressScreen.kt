package com.meesam.springshoppingclient.views.profile

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Hotel
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.meesam.springshoppingclient.R
import com.meesam.springshoppingclient.events.AddressEvents
import com.meesam.springshoppingclient.model.UserAddressResponse
import com.meesam.springshoppingclient.navigation.AppDestinations
import com.meesam.springshoppingclient.states.AppState
import com.meesam.springshoppingclient.viewmodel.AddressViewModel
import com.meesam.springshoppingclient.views.theme.AppTheme
import com.valentinilk.shimmer.shimmer


@Composable
fun AddressScreen(modifier: Modifier = Modifier, mainNavController: NavHostController) {
    val addressScreenViewModel: AddressViewModel = hiltViewModel()
    val userAddress by addressScreenViewModel.userAddress.collectAsState()
    val togglePrimaryAddressState by addressScreenViewModel.togglePrimaryAddressState.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(togglePrimaryAddressState) {
        when (togglePrimaryAddressState) {
            is AppState.Error, AppState.Idle, AppState.Loading -> {}
            is AppState.Success -> {
                Toast.makeText(context, "Primary address changed successfully", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            addressScreenViewModel.onEvent(AddressEvents.LoadUserAddressList)
        }
    }

    when (val result = userAddress) {
        is AppState.Loading -> {
            ShimmerAddressList()
        }

        is AppState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(result.errorMessage.toString())
            }
        }

        is AppState.Idle -> {}
        is AppState.Success -> {
            AddressUi(
                mainNavController = mainNavController,
                addressList = result.data,
                addressScreenViewModel = addressScreenViewModel,
                togglePrimaryAddressState = togglePrimaryAddressState
            )
        }
    }
}

@Composable
fun AddressUi(
    modifier: Modifier = Modifier,
    mainNavController: NavHostController,
    addressList: List<UserAddressResponse>,
    addressScreenViewModel: AddressViewModel,
    togglePrimaryAddressState: AppState<*>
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.surfaceContainerLowest),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            IconButton(onClick = {
                mainNavController.navigate(AppDestinations.ADDRESS_SCREEN_ROUTE)
            }, modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        Icons.Outlined.Add,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Add new address", style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontFamily(Font(R.font.nunito_semibold))
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        items(addressList) {
            AddressCard(it) { selectedAddressId ->
                addressScreenViewModel.onEvent(AddressEvents.OnAddressToggleClick(selectedAddressId))
            }
        }
    }
}

@Composable
fun AddressCard(userAddress: UserAddressResponse, onAddressToggle: (addressId: Long) -> Unit) {
    val addressParts = listOf<String>(
        userAddress.street.toString(),
        userAddress.city.toString(),
        userAddress.state.toString(),
        userAddress.pin.toString(),
        userAddress.country.toString()
    )
    val mainAddress = addressParts.filter { it.isNotBlank() }.joinToString(separator = ", ")
    OutlinedCard(
        colors = if (userAddress.isPrimary) {
            CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(0.1f)
            )
        } else {
            CardDefaults.outlinedCardColors()
        },
        onClick = {
            onAddressToggle(userAddress.id?.toLong() ?: 0)
        }, modifier = Modifier.fillMaxWidth(),
        border = if (userAddress.isPrimary) {
            BorderStroke(
                width = 1.dp,
                brush = SolidColor(MaterialTheme.colorScheme.primary)
            )
        } else {
            CardDefaults.outlinedCardBorder()
        }
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                when (userAddress.addressType) {
                    "Home" -> {
                        Icons.Outlined.Home
                    }

                    "Work" -> {
                        Icons.Outlined.Apartment
                    }

                    "Hotel" -> {
                        Icons.Outlined.Hotel
                    }

                    else -> {
                        Icons.Outlined.Home
                    }
                },
                contentDescription = null,
                tint = if (userAddress.isPrimary) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                    0.5f
                )
            )

            Spacer(Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    userAddress.contactName.toString(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = FontFamily(Font(R.font.nunito_bold))
                    )
                )
                Text(
                    mainAddress,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontFamily = FontFamily(Font(R.font.nunito_semibold))
                    ),
                    color = MaterialTheme.colorScheme.onSurface.copy(0.7f),
                    overflow = TextOverflow.Visible,
                )
                Text(
                    "Near by " + userAddress.nearBy.toString(),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontFamily = FontFamily(Font(R.font.nunito_semibold))
                    ),
                    color = MaterialTheme.colorScheme.onSurface.copy(0.7f),
                    overflow = TextOverflow.Visible,
                )
                Text(
                    userAddress.contactNumber.toString(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = FontFamily(Font(R.font.nunito_bold))
                    )
                )
            }

            IconButton(onClick = {}) {
                Icon(Icons.Outlined.MoreVert, contentDescription = null)
            }

        }

        Column(modifier = Modifier.fillMaxWidth()) {

        }
    }
}

@Composable
fun ShimmerAddressList(modifier: Modifier = Modifier) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = modifier
            .fillMaxSize()
            .shimmer(), // Apply the shimmer effect here!
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(5) { // Show 5 placeholder items
            AddressCardShimmer()
        }
    }
}

@Composable
fun AddressCardShimmer(modifier: Modifier = Modifier) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Icon Placeholder
            Box(
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.LightGray)
            )

            Spacer(Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp) // Spacing between placeholder lines
            ) {
                // Contact Name Placeholder
                Box(
                    modifier = Modifier
                        .height(20.dp)
                        .fillMaxWidth(0.5f) // 50% of the available width
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                        .background(Color.LightGray)
                )
                // Main Address Placeholder
                Box(
                    modifier = Modifier
                        .height(18.dp)
                        .fillMaxWidth(0.9f) // 90% width
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                        .background(Color.LightGray)
                )
                // "Near by" Placeholder
                Box(
                    modifier = Modifier
                        .height(18.dp)
                        .fillMaxWidth(0.7f) // 70% width
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                        .background(Color.LightGray)
                )
                // Contact Number Placeholder
                Box(
                    modifier = Modifier
                        .height(20.dp)
                        .fillMaxWidth(0.4f) // 40% width
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                        .background(Color.LightGray)
                )
            }

            // MoreVert Icon Placeholder
            Box(
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                    .background(Color.LightGray)
            )
        }
    }
}


@Composable
@Preview(showBackground = true)
fun AddressCardPrev(modifier: Modifier = Modifier) {
    val address = UserAddressResponse(
        addressType = "Home",
        street = "New Ashok Nagar",
        state = "Delhi",
        city = "New Delhi",
        pin = "110091",
        country = "India",
        nearBy = "Hanuman mandir",
        isPrimary = false,
        contactNumber = "8826120526",
        contactName = "Meesam"
    )
    AppTheme {
        //AddressCard(address)
    }
}

@Composable
@Preview(showBackground = true)
fun AddressScreenPrev(modifier: Modifier = Modifier) {
    AppTheme {
        //AddressScreen()
    }
}