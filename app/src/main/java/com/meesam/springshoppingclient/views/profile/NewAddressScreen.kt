package com.meesam.springshoppingclient.views.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.meesam.springshoppingclient.R
import com.meesam.springshoppingclient.views.common.PrimaryButton
import com.meesam.springshoppingclient.views.theme.AppTheme
import android.location.Geocoder
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.ChipDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.LocationSearching
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.Streetview
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.meesam.springshoppingclient.events.AddressEvents
import com.meesam.springshoppingclient.states.AppState
import com.meesam.springshoppingclient.viewmodel.AddressViewModel
import com.meesam.springshoppingclient.views.common.InputTextField
import com.meesam.springshoppingclient.views.theme.subHeading
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale


@Composable
fun NewAddressScreen(modifier: Modifier = Modifier,onBackClick: () -> Unit) {
    val addressViewModel: AddressViewModel = hiltViewModel()
    val addAddressState = addressViewModel.addAddressState.collectAsState()
    NewAddressUi(addressViewModel = addressViewModel,addAddressState = addAddressState.value){
        onBackClick()
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewAddressUi(modifier: Modifier = Modifier, addressViewModel: AddressViewModel, addAddressState: AppState<*>, onBackClick: () -> Unit) {
    var currentAddress by remember { mutableStateOf<String>("Fetching address...") }
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val isLoading = addAddressState is AppState.Loading
    val isSuccess = addAddressState is AppState.Success

    LaunchedEffect(isSuccess) {
        if(isSuccess){
            showBottomSheet = false
            Toast.makeText(context, "New Address added successfully",Toast.LENGTH_SHORT  ).show()
            onBackClick()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
        ) {
            CurrentLocationMapScreen(onGetAddress = {
                currentAddress = it
            }) {
                onBackClick()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.9f)
                .padding(16.dp)
        ) {
            Text(
                "Delivering your order to", style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily(Font(R.font.nunito_bold))
                ), color = MaterialTheme.colorScheme.onSurface.copy(0.7f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedCard(
                onClick = {}, modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            currentAddress,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontFamily = FontFamily(Font(R.font.nunito_regular))
                            ),
                            color = MaterialTheme.colorScheme.onSurface.copy(0.7f),
                            overflow = TextOverflow.Visible,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(title = "Add more Address details", enabled = true, isLoading = false) {
                showBottomSheet = true
            }
        }
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState,
            sheetGesturesEnabled = false,
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            dragHandle = null,
            modifier = Modifier.padding(bottom = 120.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Enter complete address",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontFamily(Font(R.font.nunito_bold))
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    IconButton(onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    }) {
                        Icon(Icons.Outlined.Clear, contentDescription = null)
                    }
                }
                MoreAddressDetails(modifier = Modifier.fillMaxHeight(0.5f), addressViewModel)
                PrimaryButton(
                    title = if (isLoading) "Saving Changes..." else "Save Changes",
                    enabled = addressViewModel.isFormValid && !isLoading,
                    isLoading = isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = if (sheetState.isVisible) 16.dp else 0.dp,
                            bottom = 16.dp
                        )
                ) {
                    addressViewModel.onEvent(AddressEvents.OnSaveChangesClick)
                }
            }
        }
    }

}


@Composable
fun CurrentLocationMapScreen(onGetAddress: (address: String) -> Unit, onBackClick: () -> Unit) {
    // 1. Initial State Setup
    val context = LocalContext.current
    var currentAddress by remember { mutableStateOf<String>("Fetching address...") }

    val defaultLocation = LatLng(20.5937, 78.9629)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 1f)
    }

    var locationPermissionGranted by remember {
        mutableStateOf(
            checkLocationPermission(context)
        )
    }

    var lastKnownLocation by remember { mutableStateOf<LatLng?>(null) }

    val onLocationFetched: (LatLng) -> Unit = { newLocation ->
        lastKnownLocation = newLocation // <-- LAT/LNG IS PASSED/STORED HERE
    }


    var isMapLoaded by remember { mutableStateOf(false) }

    // 2. Permission Launcher (The "Can I have your location?" dialog)
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        locationPermissionGranted = isGranted
        if (isGranted) {
            // Permission granted, now fetch location
            fetchCurrentLocation(context, onLocationFetched)
        }
    }

    // 3. Fetch Location Logic
    LaunchedEffect(locationPermissionGranted) {
        if (!locationPermissionGranted) {
            // Request permission if not granted
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            // Already granted, fetch location immediately
            fetchCurrentLocation(context, onLocationFetched)
        }
    }

    // 4. Center Camera Logic
    LaunchedEffect(lastKnownLocation, isMapLoaded) {
        if (isMapLoaded) { // <-- Check the flag
            lastKnownLocation?.let { location ->
                val addressResult = getAddressFromLatLng(context, location)

                // Update the new state variable
                currentAddress = addressResult ?: "Address lookup failed."
                onGetAddress(currentAddress)

                val newCameraPosition = CameraPosition.fromLatLngZoom(
                    location, // <-- LatLng is used here
                    16f
                )

                // Move the camera to the new position
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newCameraPosition(newCameraPosition),
                    durationMs = 1500
                )
            }
        }
    }

    // 5. Map Composable
    Box() {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapLoaded = { isMapLoaded = true },
            onMapClick = { clickedLocation ->
                onLocationFetched(clickedLocation)
            },
            properties = MapProperties(
                // Show the blue dot IF and ONLY IF permission is granted
                isMyLocationEnabled = locationPermissionGranted
            )
        ) {
            // You can add markers or other map elements here
            lastKnownLocation?.let { location ->
                Marker(
                    state = MarkerState(position = location), // <-- LatLng is used here too
                    title = "Current Location",
                    draggable = true
                )
            }
        }

        IconButton(
            onClick = {
                onBackClick()
            }, colors = IconButtonColors(
                containerColor = Color.Black.copy(0.4f),
                contentColor = Color.White,
                disabledContentColor = Color.Unspecified,
                disabledContainerColor = Color.Unspecified
            ),
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 50.dp, start = 16.dp)

        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        OutlinedButton(
            onClick = {},
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                Icons.Outlined.MyLocation,
                contentDescription = null,
                Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Use my current location", style = MaterialTheme.typography.titleSmall.copy(
                    fontFamily = FontFamily(Font(R.font.nunito_bold))
                ), color = MaterialTheme.colorScheme.primary
            )
        }
    }

}

// --- Utility Functions ---

/** Checks if the app has ACCESS_FINE_LOCATION permission. */
private fun checkLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

/** Uses FusedLocationProviderClient to get the last known location. */
@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
private fun fetchCurrentLocation(context: Context, onLocationFetched: (LatLng) -> Unit) {
    // ... (Permission Check) ...

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        location?.let {
            // 🚨 THIS IS WHERE you call the function!
            val latLng = LatLng(it.latitude, it.longitude)
            onLocationFetched(latLng) // <-- BAM! The data is sent back to Compose state.
        }
    }.addOnFailureListener { exception ->
        // Handle failure, e.g., log it or show a message
        Log.d("Location", "Error fetching location: ${exception.message}")
    }
}

fun getAddressFromLatLng(context: Context, latLng: LatLng): String? {
    // Geocoder can be initialized with a specific locale
    val geocoder = Geocoder(context, Locale.getDefault())

    // The Geocoding operation requires API 33+ to use the non-deprecated methods.
    // For simplicity, we'll use the deprecated method that works across more versions for now.

    return try {
        // Fetch up to 1 result.
        @Suppress("DEPRECATION")
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

        if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]

            // Build a clean, readable address string.
            // Address lines, locality, and country are the most useful parts.
            val addressParts = listOf(
                address.thoroughfare,
                address.subLocality,
                address.adminArea,
                address.countryName
            )
            addressParts.filter { !it.isNullOrBlank() }.joinToString(separator = ", ")

        } else {
            "Address not found for these coordinates."
        }
    } catch (e: IOException) {
        // This usually means a network issue (e.g., no internet connection)
        Log.e("Geocoder", "Network error during geocoding: ${e.message}")
        null
    } catch (e: IllegalArgumentException) {
        // This means the lat/lng values are invalid
        Log.e("Geocoder", "Invalid coordinates: ${e.message}")
        null
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MoreAddressDetails(modifier: Modifier = Modifier, addressViewModel: AddressViewModel) {
    val addressTypeState = addressViewModel.addressTypeState.collectAsState()
    val scrollState = rememberScrollState()
    val streetFocusRequest = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        streetFocusRequest.requestFocus()
    }
    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                "Save address as",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontFamily = FontFamily(Font(R.font.nunito_bold))
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
            )
            when (val result = addressTypeState.value) {
                is AppState.Loading -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                    }

                }

                is AppState.Error, AppState.Idle -> {}

                is AppState.Success -> {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(result.data) {
                            Chip(
                                onClick = {
                                    addressViewModel.onEvent(AddressEvents.OnSelectAddressType(it.title))
                                },
                                shape = RoundedCornerShape(10.dp),
                                leadingIcon = {
                                    Icon(
                                        it.icon,
                                        contentDescription = null,
                                        Modifier.size(20.dp),
                                        tint = if (it.isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                                            0.5f
                                        )
                                    )
                                },
                                colors = ChipDefaults.chipColors(
                                    backgroundColor = if (it.isSelected) Color(
                                        0XFFe2dfff
                                    ).copy(0.7f) else MaterialTheme.colorScheme.surfaceContainerLowest
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    color = if (it.isSelected) MaterialTheme.colorScheme.primary else subHeading
                                )
                            ) {
                                Text(
                                    it.title, style = MaterialTheme.typography.titleSmall.copy(
                                        fontFamily = FontFamily(Font(R.font.nunito_bold))
                                    ),
                                    color = if (it.isSelected) Color(0XFF424178) else MaterialTheme.colorScheme.onSurface.copy(
                                        0.5f
                                    )
                                )
                            }
                        }
                    }
                }

            }

        }
        Spacer(Modifier.height(16.dp))

        Text(
            "Address",
            style = MaterialTheme.typography.titleSmall.copy(
                fontFamily = FontFamily(Font(R.font.nunito_bold))
            ),
            color = MaterialTheme.colorScheme.onSurface.copy(0.9f),
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(Modifier.height(5.dp))
        InputTextField(
            textFieldState = addressViewModel.street,
            isError = addressViewModel.streetError != null,
            errorMessage = addressViewModel.streetError.toString(),
            placeholder = "Please enter street",
            leadingIcon = Icons.Outlined.Streetview,
            enabled = true,
            modifier = Modifier.focusRequester(streetFocusRequest)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "ZipCode",
            style = MaterialTheme.typography.titleSmall.copy(
                fontFamily = FontFamily(Font(R.font.nunito_bold))
            ),
            color = MaterialTheme.colorScheme.onSurface.copy(0.9f),
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(Modifier.height(5.dp))
        InputTextField(
            textFieldState = addressViewModel.zipCode,
            isError = addressViewModel.zipCodeError != null,
            errorMessage = addressViewModel.zipCodeError.toString(),
            placeholder = "Please enter zipcode",
            leadingIcon = Icons.Outlined.LocationSearching,
            enabled = true
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "Near by",
            style = MaterialTheme.typography.titleSmall.copy(
                fontFamily = FontFamily(Font(R.font.nunito_bold))
            ),
            color = MaterialTheme.colorScheme.onSurface.copy(0.9f),
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(Modifier.height(5.dp))
        InputTextField(
            textFieldState = addressViewModel.nearBy,
            isError = false,
            errorMessage = "",
            placeholder = "Please enter nearby location",
            leadingIcon = Icons.Outlined.LocationCity,
            enabled = true
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "Contact Person",
            style = MaterialTheme.typography.titleSmall.copy(
                fontFamily = FontFamily(Font(R.font.nunito_bold))
            ),
            color = MaterialTheme.colorScheme.onSurface.copy(0.9f),
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(Modifier.height(5.dp))
        InputTextField(
            textFieldState = addressViewModel.contactPerson,
            isError = addressViewModel.contactPersonError != null,
            errorMessage = addressViewModel.contactPersonError.toString(),
            placeholder = "Please enter contact person",
            leadingIcon = Icons.Outlined.Person,
            enabled = true
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "Contact Number",
            style = MaterialTheme.typography.titleSmall.copy(
                fontFamily = FontFamily(Font(R.font.nunito_bold))
            ),
            color = MaterialTheme.colorScheme.onSurface.copy(0.9f),
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(Modifier.height(5.dp))
        InputTextField(
            textFieldState = addressViewModel.contactNumber,
            isError = addressViewModel.contactNumberError != null,
            errorMessage = addressViewModel.contactNumberError.toString(),
            placeholder = "Please enter contact number",
            leadingIcon = Icons.Outlined.PhoneAndroid,
            enabled = true
        )
        Spacer(Modifier.height(16.dp))

    }
}

@Composable
@Preview(showBackground = true)
fun NewAddressScreenPrev(modifier: Modifier = Modifier) {
    AppTheme {
        NewAddressScreen() {

        }
    }
}

@Composable
@Preview(showBackground = true)
fun MoreAddressDetailsPrev(modifier: Modifier = Modifier) {
    AppTheme {
       // MoreAddressDetails()
    }
}