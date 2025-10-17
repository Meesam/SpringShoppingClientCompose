package com.meesam.springshoppingclient.views.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.meesam.springshoppingclient.R
import com.meesam.springshoppingclient.views.theme.AppTheme

@Composable
fun LinkButton(
    modifier: Modifier = Modifier,
    title: String,
    buttonTitle: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            title, style = MaterialTheme.typography.titleSmall.copy(
                fontFamily = FontFamily(Font(R.font.nunito_bold))
            ), color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            buttonTitle, style = MaterialTheme.typography.titleSmall.copy(
                fontFamily = FontFamily(Font(R.font.nunito_bold))
            ), color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                onClick()
            }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun LinkButtonPrev() {
    AppTheme {
        LinkButton(title = "Don't Recieve otp?", buttonTitle = "Resend"){}
    }
}