package com.meesam.springshoppingclient.views.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.meesam.springshoppingclient.R
import com.meesam.springshoppingclient.events.UserProfileEvent
import com.meesam.springshoppingclient.states.AppState
import com.meesam.springshoppingclient.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel = hiltViewModel(), onSignOut: () -> Unit) {

    val userProfile by profileViewModel.userProfile.collectAsState()
    val editProfileSheet by profileViewModel.showEditProfileBottomSheet.collectAsState()

    when (val result = userProfile) {
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
        is AppState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }

        is AppState.Success -> {
            if (result.data != null) {
                ProfileUi(
                    result.data.name,
                    result.data.email,
                    profilePicture = result.data.profilePicUrl,
                    dob = result.data.dob,
                    onClick = onSignOut,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileUi(
    name: String,
    email: String,
    dob: String,
    profilePicture: String?,
    onClick: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(10.dp))
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),

                ) {
                // here will be image and text
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(70.dp)
                        .background(
                            if (profilePicture.isNullOrEmpty()) Color.DarkGray else Color.Transparent,
                            shape = CircleShape
                        )
                        .border(
                            BorderStroke(0.5.dp, SolidColor(MaterialTheme.colorScheme.background)),
                            shape = CircleShape
                        )
                ) {
                    if (profilePicture.isNullOrEmpty()) {
                        Text(
                            name.first().toString(),
                            color = Color.White,
                            style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 30.sp)
                        )
                    } else {
                        AsyncImage(
                            model = profilePicture,
                            contentDescription = "Profile Picture",
                            modifier = Modifier.fillMaxSize().clip(shape = CircleShape), // Fill the Box
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
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.background
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(dob, color = MaterialTheme.colorScheme.background)
                    Text(email, color = MaterialTheme.colorScheme.background)
                }

                Spacer(modifier = Modifier.width(20.dp))

            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(10.dp))
                .padding(start = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Account Information")
                Spacer(modifier = Modifier.width(10.dp))
                Text("Edit Profile")
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Open")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(10.dp))
                .padding(start = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Lock, contentDescription = "Account Information")
                Spacer(modifier = Modifier.width(10.dp))
                Text("Chane Password")
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Open")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(10.dp))
                .padding(start = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Notifications, contentDescription = "Account Information")
                Spacer(modifier = Modifier.width(10.dp))
                Text("Notifications")
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Open")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(10.dp))
                .padding(start = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Language, contentDescription = "Account Information")
                Spacer(modifier = Modifier.width(10.dp))
                Text("Language")
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Open")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(10.dp))
                .padding(start = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Security, contentDescription = "Account Information")
                Spacer(modifier = Modifier.width(10.dp))
                Text("Security")
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Open")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
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
}

@Preview(showBackground = true)
@Composable
fun ProfilePrev() {
    ProfileUi(
        "Meesam",
        "meesam.engineer@gmail.com",
        profilePicture = "ff",
        dob = "02-May-1986",
        onClick = {})
}