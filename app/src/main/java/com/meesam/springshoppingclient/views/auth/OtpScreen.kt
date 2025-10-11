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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.meesam.springshoppingclient.events.OtpEvents
import com.meesam.springshoppingclient.states.AppState
import com.meesam.springshoppingclient.viewmodel.OtpViewModel
import com.meesam.springshoppingclient.views.theme.AppTheme
import kotlinx.coroutines.launch

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
            OtpForm(modifier = modifier, otpViewModel = otpViewModel, otpState = otpState){
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
fun OtpForm(modifier: Modifier = Modifier, otpViewModel: OtpViewModel, otpState: AppState<*>, onOtpSuccess:()->Unit) {
    val isLoading = otpState is AppState.Loading
    val isSuccess = otpState is AppState.Success
    val tempEmail by otpViewModel.tempEmail.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 50.dp)
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
                Text("Verification Code", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "We have send the code on your registered email",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                )
                Text(
                    tempEmail ?:"Email not found",
                    style = MaterialTheme.typography.titleMedium
                )
            }

        }

        Column(
            modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 20.dp)
        ) {
            OutlinedTextField(
                value = otpViewModel.otp, onValueChange = {
                    otpViewModel.onEvent(OtpEvents.OnOtpChange(it))
                }, placeholder = {
                    Text("Enter Otp")
                }, label = {
                    Text("OTP")
                },
                isError = otpViewModel.otpError != null && !isLoading,
                shape = MaterialTheme.shapes.medium, modifier = Modifier.fillMaxWidth()
            )
            if (otpViewModel.otpError != null) {
                Text(
                    otpViewModel.otpError.toString(),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    otpViewModel.onEvent(OtpEvents.onVerifyClick)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
                enabled = otpViewModel.isFormValid && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    if (!isLoading) "Verify" else "Activating Account...",
                    style = MaterialTheme.typography.titleSmall
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Don't receive the code?", style = MaterialTheme.typography.bodySmall)
                TextButton(onClick = {}) {
                    Text("Resend", style = MaterialTheme.typography.titleSmall)
                }
            }
        }

        if (isSuccess) {
            ModalBottomSheet(
                onDismissRequest = {},
                sheetState = sheetState,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Sheet content
                Column(modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp)
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
                                style = MaterialTheme.typography.bodySmall,
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

@Composable
fun OtpScreenUI(modifier: Modifier = Modifier, onOtpSuccess: () -> Unit) {
    //val otpViewModel: OtpViewModel = hiltViewModel()
    //val otpState by otpViewModel.otpState.collectAsState()
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
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(top = 50.dp)
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
                        Text("Verification Code", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "We have send the code on your registered email",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            "meesam.engineer@gmail.com",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                }

                Column(
                    modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 20.dp)
                ) {

                    OutlinedTextField(
                        value = "", onValueChange = {
                            //otpViewModel.onEvent(OtpEvents.OnOtpChange(it))
                        }, placeholder = {
                            Text("Enter Otp")
                        }, label = {
                            Text("OTP")
                        },
                        // isError = otpViewModel.otpError != null,
                        shape = MaterialTheme.shapes.medium, modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            // otpViewModel.onEvent(OtpEvents.onVerifyClick)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
                        //  enabled = otpViewModel.isFormValid
                    ) {
                        Text("Verify", style = MaterialTheme.typography.titleSmall)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Don't receive the code?", style = MaterialTheme.typography.bodySmall)
                        TextButton(onClick = {}) {
                            Text("Resend", style = MaterialTheme.typography.titleSmall)
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