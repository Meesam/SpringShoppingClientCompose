package com.meesam.springshoppingclient.views.auth

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.meesam.springshoppingclient.R
import com.meesam.springshoppingclient.events.OtpEvents
import com.meesam.springshoppingclient.states.AppState
import com.meesam.springshoppingclient.viewmodel.OtpViewModel
import com.meesam.springshoppingclient.views.common.InputTextField
import com.meesam.springshoppingclient.views.common.LinkButton
import com.meesam.springshoppingclient.views.common.OtpInputField
import com.meesam.springshoppingclient.views.common.PrimaryButton
import com.meesam.springshoppingclient.views.theme.AppTheme
import com.meesam.springshoppingclient.views.theme.success
import kotlinx.coroutines.launch

private const val OTP_LENGTH = 6

@Composable
fun OtpScreen(modifier: Modifier = Modifier, onOtpSuccess: () -> Unit) {
    val otpViewModel: OtpViewModel = hiltViewModel()
    val otpState by otpViewModel.otpState.collectAsState()

    when (otpState) {
        is AppState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Something went wrong")
            }
        }

        is AppState.Idle, is AppState.Loading, is AppState.Success -> {
            OtpForm(modifier = modifier, otpViewModel = otpViewModel, otpState = otpState) {
                onOtpSuccess()
            }
        }

        //is AppState.Success<*> -> {
        //onOtpSuccess()
        //}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpForm(
    modifier: Modifier = Modifier,
    otpViewModel: OtpViewModel,
    otpState: AppState<*>,
    onOtpSuccess: () -> Unit
) {
    val isLoading = otpState is AppState.Loading
    val isSuccess = otpState is AppState.Success
    val tempEmail by otpViewModel.tempEmail.collectAsState()
    val sheetState = rememberModalBottomSheetState()

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(top = 80.dp, bottom = 20.dp, start = 16.dp, end = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary, shape = CircleShape
                    ), contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.MarkEmailRead,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Verification Code", style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily(Font(R.font.nunito_bold))
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "We have send the code on your registered email",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = FontFamily(Font(R.font.nunito_bold))
                    ),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                )
                Text(
                    tempEmail ?: "Email not found",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily(Font(R.font.nunito_bold))
                    ),
                )
            }

        }

        Column(
            modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 20.dp)
        ) {
            OtpInputField(
                otpText = otpViewModel.otp,
                onOtpTextChange = { newOtp ->
                    if (newOtp.length <= OTP_LENGTH && newOtp.all { it.isDigit() }) {
                        otpViewModel.onEvent(OtpEvents.OnOtpChange(newOtp))
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(
                title = if (!isLoading) "Verify" else "Activating Account...",
                enabled = otpViewModel.isFormValid && !isLoading,
                isLoading = isLoading
            ) {
                otpViewModel.onEvent(OtpEvents.onVerifyClick)
            }
            Spacer(modifier = Modifier.height(16.dp))
            LinkButton(title = "Don't receive the code?", buttonTitle = "Resend") { }
        }

        if (isSuccess) {
            ModalBottomSheet(
                onDismissRequest = {},
                sheetState = sheetState,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Sheet content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 50.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(
                                    color = success, shape = CircleShape
                                ), contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Register Success", style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Congratulations, Your account is activated, Please login to explore",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontFamily = FontFamily(Font(R.font.nunito_regular))
                                ),
                                color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                                textAlign = TextAlign.Center,
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            PrimaryButton(
                                title = "Go to Login",
                                enabled = true,
                                isLoading = false
                            ) {
                                onOtpSuccess()
                            }
                        }
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpScreenUI(modifier: Modifier = Modifier, onOtpSuccess: () -> Unit) {
    val isLoading = false
    val isSuccess = true
    val otpState: AppState<*> = AppState.Idle
    when (otpState) {
        is AppState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Something went wrong")
            }
        }

        AppState.Idle -> {
            val sheetState = rememberModalBottomSheetState()
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                    .padding(top = 80.dp, bottom = 20.dp, start = 16.dp, end = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary, shape = CircleShape
                            ), contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.MarkEmailRead,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Verification Code", style = MaterialTheme.typography.titleLarge.copy(
                                fontFamily = FontFamily(Font(R.font.nunito_bold))
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "We have send the code on your registered email",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = FontFamily(Font(R.font.nunito_bold))
                            ),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                        )
                        Text(
                            "Email not found",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = FontFamily(Font(R.font.nunito_bold))
                            ),
                        )
                    }

                }

                Column(
                    modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 20.dp)
                ) {

                    OtpInputField(
                        otpText = "",
                        onOtpTextChange = { newOtp ->
                            if (newOtp.length <= OTP_LENGTH && newOtp.all { it.isDigit() }) {
                                //otpViewModel.onEvent(OtpEvents.OnOtpChange(newOtp))
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    PrimaryButton(
                        title = if (!isLoading) "Verify" else "Activating Account...",
                        enabled = true,
                        isLoading = isLoading
                    ) {
                        //otpViewModel.onEvent(OtpEvents.onVerifyClick)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    LinkButton(title = "Don't receive the code?", buttonTitle = "Resend") { }
                }

                if (isSuccess) {
                    ModalBottomSheet(
                        onDismissRequest = {},
                        sheetState = sheetState,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Sheet content
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 50.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .background(
                                            color = success, shape = CircleShape
                                        ), contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Filled.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "Register Success",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        "Congratulations, Your account is activated, Please login to explore",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontFamily = FontFamily(Font(R.font.nunito_regular))
                                        ),
                                        color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                                        textAlign = TextAlign.Center,
                                    )
                                    Button(
                                        onClick = {
                                            onOtpSuccess()
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = MaterialTheme.shapes.medium,
                                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
                                    ) {
                                        Text(
                                            "Go to Login",
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }

        AppState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is AppState.Success<*> -> {
            onOtpSuccess()
        }
    }
}

@Composable
@Preview(showBackground = true)
fun OTPPreview() {
    AppTheme {
        OtpScreenUI(
            onOtpSuccess = { })
    }
}