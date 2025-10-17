package com.meesam.springshoppingclient.views.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.meesam.springshoppingclient.views.theme.AppTheme
import kotlinx.coroutines.delay

@Composable
fun AppErr(modifier: Modifier = Modifier, errorMessage: String) {
    var isVisible by remember { mutableStateOf(true) }


    LaunchedEffect(Unit) {
        delay(10_000L)
        isVisible = false
    }

    AnimatedVisibility(
        visible = isVisible
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.error,
                    MaterialTheme.shapes.small
                )
                .border(
                    1.dp,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    shape = MaterialTheme.shapes.small
                )
                .size(60.dp)

        ) {
            Text(
                errorMessage,
                color = MaterialTheme.colorScheme.onError,
                modifier = Modifier
                    .align(
                        Alignment.CenterStart
                    )
                    .padding(start = 16.dp)
            )
            Icon(
                Icons.Filled.Clear,
                contentDescription = "Clear",
                tint = MaterialTheme.colorScheme.onError,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(24.dp)
                    .padding(end = 8.dp, top = 8.dp)
                    .clickable {
                        isVisible = false
                    }
            )
        }
    }
}

@Composable
@Preview
fun AppErrPrev() {
    AppTheme {
        AppErr(errorMessage = "Something went wrong")
    }

}