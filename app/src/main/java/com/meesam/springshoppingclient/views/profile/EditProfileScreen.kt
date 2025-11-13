package com.meesam.springshoppingclient.views.profile

import android.Manifest
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraEnhance
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.meesam.springshoppingclient.R
import com.meesam.springshoppingclient.events.EditProfileEvents
import com.meesam.springshoppingclient.viewmodel.EditScreenViewModel
import com.meesam.springshoppingclient.viewmodel.ProfileViewModel
import com.meesam.springshoppingclient.views.common.InputDatePickerField
import com.meesam.springshoppingclient.views.common.InputTextField
import com.meesam.springshoppingclient.views.common.PrimaryButton
import com.meesam.springshoppingclient.views.theme.AppTheme
import com.meesam.springshoppingclient.views.theme.subHeading
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.meesam.springshoppingclient.navigation.AppDestinations
import com.meesam.springshoppingclient.states.AppState
import com.meesam.springshoppingclient.views.common.AppErr
import com.meesam.springshoppingclient.views.common.AppSuccess


@Composable
fun EditProfileScreen(modifier: Modifier = Modifier, mainNavController: NavHostController) {
    val editScreenViewModel: EditScreenViewModel = hiltViewModel()
    val editProfileState by editScreenViewModel.editProfileState.collectAsState()
    val userProfileState by editScreenViewModel.userProfile.collectAsState()

    when (val result = userProfileState) {
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

        AppState.Idle -> {}
        AppState.Loading -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }

        is AppState.Success -> {
            EditProfileUi(
                modifier = modifier,
                editDetailsViewModel = editScreenViewModel,
                editScreenState = editProfileState,
                mainNavController = mainNavController
            )
        }
    }

    when (editProfileState) {
        is AppState.Idle, is AppState.Loading, is AppState.Error, is AppState.Success -> {
            EditProfileUi(
                modifier = modifier,
                editDetailsViewModel = editScreenViewModel,
                editScreenState = editProfileState,
                mainNavController = mainNavController
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EditProfileUi(
    modifier: Modifier = Modifier,
    mainNavController: NavHostController,
    editDetailsViewModel: EditScreenViewModel,
    editScreenState: AppState<*>
) {
    val isLoading = editScreenState is AppState.Loading
    val isError = editScreenState is AppState.Error
    val isSuccess = editScreenState is AppState.Success
    val profilePicture by editDetailsViewModel.profilePicture.collectAsState()

    var showCamera by remember { mutableStateOf(false) }

    // 2. State to hold the captured image URI
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Permission state
    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )

    // Launcher for the permission request
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, show the camera
            showCamera = true
        } else {
            // Handle permission denial, e.g., show a toast or a message
            Log.d("EditProfileScreen", "Camera permission denied.")
        }
    }

    if (showCamera && cameraPermissionState.status.isGranted) {
        mainNavController.navigate(AppDestinations.CAMERA_SCREEN_ROUTE)
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(subHeading.copy(0.2f), shape = RoundedCornerShape(10.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(90.dp)
                        .background(
                            subHeading,
                            shape = CircleShape
                        )
                        .clickable {
                            if (cameraPermissionState.status.isGranted) {
                                showCamera = true
                            } else {
                                // Request the permission
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                ) {
                    if (profilePicture.isEmpty()) {
                        Text(
                            "Meesam".first().toString(),
                            //color = subHeading,
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontFamily = FontFamily(Font(R.font.nunito_bold))
                            ), color = MaterialTheme.colorScheme.onSurface.copy(0.7f)
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

                    Icon(
                        Icons.Filled.CameraEnhance,
                        null,
                        modifier = Modifier.align(Alignment.BottomEnd),
                        tint = MaterialTheme.colorScheme.onSurface.copy(0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            if (isError) {
                AppErr(errorMessage = editScreenState.errorMessage.toString())
                Spacer(Modifier.height(16.dp))
            }
            if (isSuccess) {
                AppSuccess(successMessage = editScreenState.data.toString())
                Spacer(Modifier.height(16.dp))
            }

            Text(
                "Name",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily(Font(R.font.nunito_bold))
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(0.9f),
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(Modifier.height(5.dp))
            InputTextField(
                textFieldState = editDetailsViewModel.name,
                isError = editDetailsViewModel.nameError != null,
                errorMessage = editDetailsViewModel.nameError.toString(),
                placeholder = "Please enter your name",
                leadingIcon = Icons.Outlined.Person,
                enabled = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Mobile Number",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily(Font(R.font.nunito_bold))
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(0.9f),
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(Modifier.height(5.dp))
            InputTextField(
                textFieldState = editDetailsViewModel.phone,
                isError = editDetailsViewModel.phoneError != null,
                errorMessage = editDetailsViewModel.phoneError.toString(),
                placeholder = "Please enter your mobile number",
                leadingIcon = Icons.Outlined.PhoneAndroid,
                enabled = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Date of birth",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily(Font(R.font.nunito_bold))
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(0.9f),
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(Modifier.height(5.dp))
            InputDatePickerField(
                textFieldState = editDetailsViewModel.dob,
                isError = editDetailsViewModel.dobError != null,
                errorMessage = editDetailsViewModel.dobError.toString(),
                placeholder = "Please enter your date of birth",
                enabled = true,
            )
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(
                title = if (isLoading) "Updating Profile..." else "Save Change",
                enabled = editDetailsViewModel.isFormValid && !isLoading,
                isLoading = isLoading
            ) {
                editDetailsViewModel.onEvent(EditProfileEvents.OnSaveChangesClick)
            }
        }
    }

}

@Composable
@Preview(showBackground = true)
fun EditProfileScreenPrev() {
    AppTheme {
        //EditProfileScreen()
    }
}


