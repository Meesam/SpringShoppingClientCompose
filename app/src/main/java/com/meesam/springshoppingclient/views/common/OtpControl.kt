package com.meesam.springshoppingclient.views.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meesam.springshoppingclient.views.theme.AppTheme
import kotlinx.coroutines.delay

private const val OTP_LENGTH = 6
private val CELL_SIZE = 54.dp
private val BORDER_WIDTH = 2.dp

@Composable
fun OtpInputField(
    modifier: Modifier = Modifier,
    otpText: String,
    onOtpTextChange: (String) -> Unit
) {
    // 1. Blinking Cursor State: Used for visual focus indication
    var isCursorVisible by remember { mutableStateOf(false) }

    // Coroutine to toggle the cursor visibility for blinking effect
    LaunchedEffect(key1 = otpText.length) {
        if (otpText.length < OTP_LENGTH) {
            isCursorVisible = true // Show cursor when focused on an empty slot
            while (true) {
                delay(500)
                isCursorVisible = !isCursorVisible
            }
        } else {
            isCursorVisible = false // Hide cursor when all digits are entered
        }
    }

    // 2. Focus Requester: Used to manually request focus on the underlying BasicTextField
    val focusRequester = remember { FocusRequester() }

    // Use BasicTextField as the core input handler
    BasicTextField(
        modifier = modifier.focusRequester(focusRequester),
        value = otpText,
        onValueChange = onOtpTextChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        singleLine = true,
        textStyle = TextStyle.Default.copy(color = Color.Transparent), // Hide the actual input text

        // 3. The decorationBox is where the custom UI rendering happens
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.width(CELL_SIZE * OTP_LENGTH + 8.dp * (OTP_LENGTH - 1)) // Calculate required width
            ) {
                // Iterate to draw 6 individual digit cells
                repeat(OTP_LENGTH) { index ->
                    val char = when {
                        index < otpText.length -> otpText[index].toString()
                        else -> ""
                    }

                    val isCurrentCellFocused = index == otpText.length && otpText.length < OTP_LENGTH

                    OtpCell(
                        char = char,
                        isFocused = isCurrentCellFocused,
                        isCursorVisible = isCursorVisible && isCurrentCellFocused
                    )

                    if (index < OTP_LENGTH - 1) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }
    )

    // Optional: Request initial focus when the composable is first displayed
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun OtpCell(
    char: String,
    isFocused: Boolean,
    isCursorVisible: Boolean,
    size: Dp = CELL_SIZE
) {
    val borderColor = when {
        isFocused -> MaterialTheme.colorScheme.primary // Highlight color for active cell
        char.isNotEmpty() -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f) // Filled cell
        else -> Color.LightGray.copy(alpha = 0.8f) // Empty cell
    }

    Box(
        modifier = Modifier
            .size(size)
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(
                width = BORDER_WIDTH,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = char,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        // Draw a blinking cursor if focused and visible
        if (isFocused && isCursorVisible) {
            Box(
                modifier = Modifier
                    .size(width = 2.dp, height = 24.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun OtpInputFieldPrev(modifier: Modifier = Modifier) {
    AppTheme {
        OtpInputField(otpText = "", ) {

        }
    }
}