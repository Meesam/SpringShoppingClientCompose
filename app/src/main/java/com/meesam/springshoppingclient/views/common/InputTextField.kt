package com.meesam.springshoppingclient.views.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.meesam.springshoppingclient.R
import com.meesam.springshoppingclient.views.theme.AppTheme
import com.meesam.springshoppingclient.views.theme.inputBackGround


@Composable
fun DemoTextField(modifier: Modifier = Modifier) {
    val emailState = remember { TextFieldState() }
    var isError by remember { mutableStateOf(false) }
    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        Alignment.CenterHorizontally
    ) {

        InputTextField(
            textFieldState = emailState,
            isError = isError,
            errorMessage = if (isError) "Please enter a valid email." else null,
            placeholder = "Enter your email",
            leadingIcon = Icons.Outlined.Email,
            enabled = true
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            // Simple validation: error if not empty and doesn't contain "@"
            isError = emailState.text.isNotBlank() && !emailState.text.contains("@")
        }) {
            Text("Validate")
        }
    }
}

@Composable
fun InputTextField(
    modifier: Modifier = Modifier,
    textFieldState: TextFieldState,
    isError: Boolean,
    errorMessage: String?,
    placeholder: String,
    leadingIcon: ImageVector?,
    enabled: Boolean? = true,
) {
    var isFocused by remember { mutableStateOf(false) }
    val borderColor = when {
        isError -> MaterialTheme.colorScheme.error
        isFocused -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.surfaceContainerLowest
    }
    val iconColor = when {
        isError -> MaterialTheme.colorScheme.error
        isFocused -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
    }

    BasicTextField(
        state = textFieldState,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        textStyle = LocalTextStyle.current.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontFamily = FontFamily(Font(R.font.nunito_regular))
        ),
        enabled = enabled ?: true,
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
            .border(
                1.dp,
                borderColor,
                RoundedCornerShape(12.dp)
            ),
        decorator = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (enabled == true) inputBackGround else Color(
                            0XFFCCCCCC
                        ), RoundedCornerShape(percent = 30)
                    )
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                if (leadingIcon != null) {
                    Icon(leadingIcon, contentDescription = null, tint = iconColor)
                }
                Spacer(Modifier.width(8.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (textFieldState.text.isEmpty()) {
                        Text(
                            placeholder, color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                    innerTextField()
                }
                if (!textFieldState.text.isEmpty()) {
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        Icons.Outlined.Clear,
                        contentDescription = null,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable {
                                textFieldState.edit {
                                    replace(0, textFieldState.text.length, "")
                                }
                            })
                }
            }
        })

    if (isError && errorMessage != null) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun InputPasswordField(
    modifier: Modifier = Modifier,
    textFieldState: TextFieldState,
    isError: Boolean,
    errorMessage: String?,
    placeholder: String,
    leadingIcon: ImageVector?,
    enabled: Boolean? = true,
) {
    var isFocused by remember { mutableStateOf(false) }
    val borderColor = when {
        isError -> MaterialTheme.colorScheme.error
        isFocused -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.surfaceContainerLowest
    }
    val iconColor = when {
        isError -> MaterialTheme.colorScheme.error
        isFocused -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
    }

    BasicSecureTextField(
        state = textFieldState,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        textStyle = LocalTextStyle.current.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        enabled = enabled ?: true,
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
            .border(
                1.dp,
                 borderColor,
                RoundedCornerShape(12.dp)
            ),
        decorator = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (enabled == true) inputBackGround else Color(
                            0XFFCCCCCC
                        ), RoundedCornerShape(percent = 30)
                    )
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                if (leadingIcon != null) {
                    Icon(leadingIcon, contentDescription = null, tint = iconColor)
                }
                Spacer(Modifier.width(8.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (textFieldState.text.isEmpty()) {
                        Text(
                            placeholder, color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                    innerTextField()
                }
                if (!textFieldState.text.isEmpty()) {
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        Icons.Outlined.Clear,
                        contentDescription = null,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable {
                                textFieldState.edit {
                                    replace(0, textFieldState.text.length, "")
                                }
                            })
                }
            }
        })

    if (isError && errorMessage != null) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun InputTextFieldPrev() {
    AppTheme {
        DemoTextField()
    }
}