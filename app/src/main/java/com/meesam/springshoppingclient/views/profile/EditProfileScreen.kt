package com.meesam.springshoppingclient.views.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.meesam.springshoppingclient.R
import com.meesam.springshoppingclient.events.EditProfileEvents
import com.meesam.springshoppingclient.events.UserProfileEvent
import com.meesam.springshoppingclient.states.AppState
import com.meesam.springshoppingclient.viewmodel.EditScreenViewModel
import com.meesam.springshoppingclient.viewmodel.ProfileViewModel
import com.meesam.springshoppingclient.views.common.InputDatePickerField
import com.meesam.springshoppingclient.views.common.InputTextField
import com.meesam.springshoppingclient.views.common.PrimaryButton
import com.meesam.springshoppingclient.views.theme.AppTheme

@Composable
fun EditProfileScreen(modifier: Modifier = Modifier) {
    val editScreenViewModel: EditScreenViewModel = hiltViewModel()
    val editProfileState by editScreenViewModel.editProfileState.collectAsState()
    val profileViewModel: ProfileViewModel = hiltViewModel()

    val isLoading = editProfileState is AppState.Loading
    val isError = editProfileState is AppState.Error

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(16.dp),
    ) {
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
            textFieldState = editScreenViewModel.name,
            isError = editScreenViewModel.nameError != null,
            errorMessage = editScreenViewModel.nameError.toString(),
            placeholder = "Please enter your name",
            leadingIcon = Icons.Outlined.Person,
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
        InputDatePickerField(textFieldState = editScreenViewModel.dob,
            isError = editScreenViewModel.dobError != null,
            errorMessage = editScreenViewModel.dobError.toString(),
            placeholder = "Please enter your date of birth",
            enabled = true,)


        Spacer(modifier = Modifier.height(16.dp))
        PrimaryButton(
            title = if (isLoading) "Updating Profile..." else "Save Change",
            enabled = editScreenViewModel.isFormValid && !isLoading,
            isLoading = isLoading
        ) {
            editScreenViewModel.onEvent(EditProfileEvents.OnSaveChangesClick)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {
            profileViewModel.onEvent(UserProfileEvent.OnDismissSheet)
        }) {
            Text(
                "Cancel",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily(Font(R.font.nunito_regular))
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun EditProfileScreenPrev() {
    AppTheme {
        EditProfileScreen()
    }
}


