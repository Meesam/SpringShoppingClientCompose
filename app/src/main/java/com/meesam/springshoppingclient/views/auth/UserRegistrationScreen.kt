package com.meesam.springshoppingclient.views.auth

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.meesam.springshoppingclient.events.UserRegistrationEvents
import com.meesam.springshoppingclient.states.AppState
import com.meesam.springshoppingclient.viewmodel.RegistrationViewModel
import com.meesam.springshoppingclient.views.common.InputPasswordField
import com.meesam.springshoppingclient.views.common.InputTextField
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
            .padding(top = 50.dp, bottom = 20.dp)

    ) {
        Text(
            "Welcome back",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )

        Text(
            "Register for new account",
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(50.dp))

        ElevatedCard(
            modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Column(
                modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 20.dp)
            ) {
                Text(
                    "Name",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.9f),
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
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.9f),
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
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.9f),
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
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.9f),
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
                Button(
                    onClick = {
                        registrationViewModel.onEvent(UserRegistrationEvents.OnRegisterClick)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    enabled = registrationViewModel.isFormValid && !isLoading,
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(if (isLoading) "Creating account..." else "Register")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Already have an Account?",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onBackToLogin()
                        },
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }
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