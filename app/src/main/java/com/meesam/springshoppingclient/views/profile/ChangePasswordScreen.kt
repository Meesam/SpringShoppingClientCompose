package com.meesam.springshoppingclient.views.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.meesam.springshoppingclient.R
import com.meesam.springshoppingclient.events.ChangePasswordEvents
import com.meesam.springshoppingclient.events.EditProfileEvents
import com.meesam.springshoppingclient.events.UserRegistrationEvents
import com.meesam.springshoppingclient.states.AppState
import com.meesam.springshoppingclient.viewmodel.ChangePasswordViewModel
import com.meesam.springshoppingclient.viewmodel.RegistrationViewModel
import com.meesam.springshoppingclient.views.auth.RegisterForm
import com.meesam.springshoppingclient.views.common.AppErr
import com.meesam.springshoppingclient.views.common.AppSuccess
import com.meesam.springshoppingclient.views.common.InputPasswordField
import com.meesam.springshoppingclient.views.common.InputTextField
import com.meesam.springshoppingclient.views.common.PrimaryButton

@Composable
fun ChangePasswordScreen(
    modifier: Modifier = Modifier
) {
    val changePasswordViewModel: ChangePasswordViewModel = hiltViewModel()
    val changePasswordUiState by changePasswordViewModel.changePasswordState.collectAsState()

    when (changePasswordUiState) {
        is AppState.Idle, is AppState.Loading, is AppState.Error, is AppState.Success -> {
            ChangePasswordUi(
                modifier = modifier,
                changePasswordViewModel = changePasswordViewModel,
                changePasswordUiState = changePasswordUiState
            )
        }
    }
}

@Composable
fun ChangePasswordUi(
    modifier: Modifier = Modifier,
    changePasswordViewModel: ChangePasswordViewModel,
    changePasswordUiState: AppState<*>
) {
    val isLoading = changePasswordUiState is AppState.Loading
    val isError = changePasswordUiState is AppState.Error
    val isSuccess = changePasswordUiState is AppState.Success

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(16.dp),
    ) {

        if(isError){
            AppErr(errorMessage = changePasswordUiState.errorMessage.toString())
            Spacer(Modifier.height(16.dp))
        }
        if(isSuccess){
            AppSuccess(successMessage = changePasswordUiState.data.toString())
            Spacer(Modifier.height(16.dp))
        }

        Text(
            "Old Password",
            style = MaterialTheme.typography.titleMedium.copy(
                fontFamily = FontFamily(Font(R.font.nunito_bold))
            ),
            color = MaterialTheme.colorScheme.onSurface.copy(0.9f),
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(Modifier.height(5.dp))
        InputPasswordField(
            textFieldState = changePasswordViewModel.oldPassword,
            isError = changePasswordViewModel.oldPasswordError != null,
            errorMessage = changePasswordViewModel.oldPasswordError.toString(),
            placeholder = "Please enter your password",
            leadingIcon = Icons.Outlined.Lock,
            enabled = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "New Password",
            style = MaterialTheme.typography.titleMedium.copy(
                fontFamily = FontFamily(Font(R.font.nunito_bold))
            ),
            color = MaterialTheme.colorScheme.onSurface.copy(0.9f),
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(Modifier.height(5.dp))
        InputPasswordField(
            textFieldState = changePasswordViewModel.newPassword,
            isError = changePasswordViewModel.newPasswordError != null,
            errorMessage = changePasswordViewModel.newPasswordError.toString(),
            placeholder = "Confirm password",
            leadingIcon = Icons.Outlined.Lock,
            enabled = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        PrimaryButton(
            title = if (isLoading) "Updating Password..." else "Save Changes",
            enabled = changePasswordViewModel.isFormValid && !isLoading,
            isLoading = isLoading
        ) {
            changePasswordViewModel.onEvent(ChangePasswordEvents.OnChangePasswordButtonClick)
        }
    }
}