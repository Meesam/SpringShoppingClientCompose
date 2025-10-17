package com.meesam.springshoppingclient.views.auth


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.meesam.springshoppingclient.R
import com.meesam.springshoppingclient.events.UserLoginEvents
import com.meesam.springshoppingclient.states.AppState
import com.meesam.springshoppingclient.viewmodel.LoginViewModel
import com.meesam.springshoppingclient.views.common.AppErr
import com.meesam.springshoppingclient.views.common.InputPasswordField
import com.meesam.springshoppingclient.views.common.InputTextField
import com.meesam.springshoppingclient.views.common.LinkButton
import com.meesam.springshoppingclient.views.common.PrimaryButton
import com.meesam.springshoppingclient.views.theme.AppTheme

@Composable
fun UserLoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit, onNavigateToRegister: () -> Unit
) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val loginState by loginViewModel.loginUiState.collectAsState()

    when (loginState) {
        is AppState.Idle, is AppState.Loading,is AppState.Error  -> {
            LoginForm(
                modifier = modifier,
                loginViewModel = loginViewModel,
                loginState = loginState
            ) {
                onNavigateToRegister()
            }
        }
        is AppState.Success<*> -> {
            onLoginSuccess()
        }
    }
}

@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel,
    loginState: AppState<*>,
    onNavigateToRegister: () -> Unit
) {

    val isLoading = loginState is AppState.Loading
    val isError = loginState is AppState.Error
    Column(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(top = 80.dp, bottom = 20.dp, start = 16.dp, end = 16.dp),
    ) {

        Text(
            "Login Account",
            style = MaterialTheme.typography.titleLarge.copy(
                fontFamily = FontFamily(Font(R.font.nunito_bold))
            ),
            textAlign = TextAlign.Center,
        )

        Text(
            "Sign In to your registered account",
            style = MaterialTheme.typography.titleMedium.copy(
                fontFamily = FontFamily(Font(R.font.nunito_bold))
            ),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
        )


        Column(
            modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(vertical = 50.dp)
        ) {

            if (isError) {
                AppErr(errorMessage = loginState.errorMessage.toString())
                Spacer(Modifier.height(16.dp))
            }
            Text(
                "Email",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily(Font(R.font.nunito_bold))
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(0.9f),
                modifier = Modifier.padding(start = 8.dp),
            )
            Spacer(Modifier.height(5.dp))
            InputTextField(
                textFieldState = loginViewModel.email,
                placeholder = "Enter your email",
                isError = loginViewModel.emailError != null,
                errorMessage = loginViewModel.emailError.toString(),
                leadingIcon = Icons.Outlined.Email,
                enabled = !isLoading
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
                textFieldState = loginViewModel.password,
                placeholder = "Enter your password",
                isError = loginViewModel.passwordError != null,
                errorMessage = loginViewModel.passwordError.toString(),
                leadingIcon = Icons.Outlined.Lock,
                enabled = !isLoading
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = {}) {
                    Text(
                        "Forgot Password?",
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            PrimaryButton(
                title = if (isLoading) "Signing In..." else "Sign In",
                enabled = loginViewModel.isFormValid && !isLoading,
                isLoading = isLoading
            ) {
                loginViewModel.onEvent(UserLoginEvents.OnLoginClick)
            }
            Spacer(modifier = Modifier.height(16.dp))
            LinkButton(title ="Don't have an Account?", buttonTitle = "Register" ) {
                onNavigateToRegister()
            }
        }

    }
}


@Composable
@Preview(showBackground = true)
fun UserLoginScreenPre(modifier: Modifier = Modifier) {
    AppTheme {
        UserLoginScreen(onLoginSuccess = {}) {}
    }
}