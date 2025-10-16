package com.meesam.springshoppingclient.views.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.meesam.springshoppingclient.R
import com.meesam.springshoppingclient.events.UserRegistrationEvents
import com.meesam.springshoppingclient.states.AppState
import com.meesam.springshoppingclient.viewmodel.RegistrationViewModel
import com.meesam.springshoppingclient.views.common.InputPasswordField
import com.meesam.springshoppingclient.views.common.InputTextField
import com.meesam.springshoppingclient.views.common.PrimaryButton
import com.meesam.springshoppingclient.views.theme.AppTheme


@Composable
fun RegisterForm(
    modifier: Modifier = Modifier,
    registerState: AppState<*>,
    registrationViewModel: RegistrationViewModel,
    onBackToLogin: () -> Unit,
) {
    val isLoading = registerState is AppState.Loading
    Column(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(top = 80.dp, bottom = 20.dp, start = 16.dp, end = 16.dp),

        ) {
        Text(
            "Create Account",
            style = MaterialTheme.typography.titleLarge.copy(
                fontFamily = FontFamily(Font(R.font.nunito_bold))
            ),
            textAlign = TextAlign.Center,
        )

        Text(
            "Start exploring with create your account",
            style = MaterialTheme.typography.titleMedium.copy(
                fontFamily = FontFamily(Font(R.font.nunito_bold))
            ),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(0.5f)

        )
        Spacer(Modifier.height(50.dp))
        Column(
            modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(vertical = 50.dp)
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
                textFieldState = registrationViewModel.name,
                isError = registrationViewModel.nameError != null,
                errorMessage = registrationViewModel.nameError.toString(),
                placeholder = "Please enter your name",
                leadingIcon = Icons.Outlined.Person,
                enabled = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Email",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily(Font(R.font.nunito_bold))
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(0.9f),
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(Modifier.height(5.dp))
            InputTextField(
                textFieldState = registrationViewModel.email,
                isError = registrationViewModel.emailError != null,
                errorMessage = registrationViewModel.emailError.toString(),
                placeholder = "Please enter your email",
                leadingIcon = Icons.Outlined.Email,
                enabled = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Password",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily(Font(R.font.nunito_bold))
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(0.9f),
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(Modifier.height(5.dp))
            InputPasswordField(
                textFieldState = registrationViewModel.password,
                isError = registrationViewModel.passwordError != null,
                errorMessage = registrationViewModel.passwordError.toString(),
                placeholder = "Please enter your password",
                leadingIcon = Icons.Outlined.Lock,
                enabled = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Confirm Password",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily(Font(R.font.nunito_bold))
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(0.9f),
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(Modifier.height(5.dp))
            InputPasswordField(
                textFieldState = registrationViewModel.confirmPassword,
                isError = registrationViewModel.confirmPasswordError != null,
                errorMessage = registrationViewModel.confirmPasswordError.toString(),
                placeholder = "Confirm password",
                leadingIcon = Icons.Outlined.Lock,
                enabled = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            PrimaryButton(
                title = if (isLoading) "Creating account..." else "Register",
                enabled = registrationViewModel.isFormValid && !isLoading,
                isLoading = isLoading
            ) {
                registrationViewModel.onEvent(UserRegistrationEvents.OnRegisterClick)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Already have an Account?",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onBackToLogin()
                    },
                style = MaterialTheme.typography.titleSmall.copy(
                    fontFamily = FontFamily(Font(R.font.nunito_regular))
                ),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun UserRegistrationScreen(
    modifier: Modifier = Modifier,
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    val registrationViewModel: RegistrationViewModel = hiltViewModel()
    val registerState by registrationViewModel.registrationState.collectAsState()

    when (registerState) {
        is AppState.Error -> {
            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                Text("Some thing went wrong")
            }
        }

        is AppState.Idle, is AppState.Loading -> {
            RegisterForm(
                registerState = registerState,
                registrationViewModel = registrationViewModel
            ) {
                onBackToLogin()
            }
        }

        is AppState.Success -> {
            onRegisterSuccess()
        }
    }
}

@Composable
@Preview(showBackground = true)
fun UserRegistrationScreenPrev() {
    AppTheme {
        UserRegistrationScreen(onRegisterSuccess = { }) {}
    }
}