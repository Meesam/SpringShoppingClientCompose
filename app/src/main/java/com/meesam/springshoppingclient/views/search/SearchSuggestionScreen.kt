package com.meesam.springshoppingclient.views.search


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.meesam.springshoppingclient.R
import com.meesam.springshoppingclient.views.common.InputTextField

val searchResults = listOf("Hell", "Hello", "Hello world")

@Composable
fun SearchSuggestionScreen(modifier: Modifier = Modifier, onBackToHome: () -> Unit) {
    val searchFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        searchFocusRequester.requestFocus()
    }

    Column(modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
        .padding(top = 80.dp, start = 16.dp, end = 16.dp)) {
        InputTextField(
            textFieldState = rememberTextFieldState(),
            placeholder = "Search",
            leadingIcon = Icons.Outlined.ChevronLeft,
            isError = false,
            enabled = true,
            errorMessage = null,
            modifier = Modifier.focusRequester(searchFocusRequester)
        ){
            onBackToHome()
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Last Search",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily(Font(R.font.nunito_bold))
                ),
            )
            TextButton(onClick = {}) {
                Text(
                    "Clear all", style = MaterialTheme.typography.titleSmall.copy(
                        fontFamily = FontFamily(Font(R.font.nunito_bold))
                    )
                )
            }

        }
        LazyColumn {
            items(count = searchResults.size) { index ->
                val resultText = searchResults[index]
                ListItem(
                    headlineContent = {
                        Text(
                            resultText, style = MaterialTheme.typography.titleSmall.copy(
                                fontFamily = FontFamily(Font(R.font.nunito_semibold))
                            )
                        )
                    },
                    supportingContent = {
                        Text(
                            "30 minute ago", style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily(Font(R.font.nunito_regular))
                            )
                        )
                    },
                    trailingContent = {
                        Icon(
                            Icons.Outlined.Clear,
                            contentDescription = "Clear this",
                            tint = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    leadingContent = {
                        Icon(
                            Icons.Filled.History,
                            contentDescription = "Localized description",
                            tint = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                    modifier = Modifier
                        .clickable {
                            //onResultClick(resultText)
                            // expanded = false
                        }
                        .fillMaxWidth()

                )
            }
        }
        AppCategory()
        Text(
            "Popular Search",
            style = MaterialTheme.typography.titleMedium.copy(
                fontFamily = FontFamily(Font(R.font.nunito_bold))
            ),
        )
    }
}

@Composable
fun ChipItem(text: String, isSelected: Boolean? = false, onClick: () -> Unit = {}) {
    FilterChip(
        modifier = Modifier.padding(end = 4.dp),
        onClick = onClick,
        leadingIcon = {},
        border = BorderStroke(
            1.dp,
            if (isSelected == true) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outlineVariant
        ),
        label = {
            Text(
                text, style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = FontFamily(Font(R.font.nunito_semibold)),
                )
            )
        },
        selected = false,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = if (isSelected == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerLowest,
            selectedLabelColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            labelColor = if (isSelected == true) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface.copy(
                0.5f
            )
        )
    )
}

@Composable
fun AppCategory() {
    Column {
        Text(
            "Categories",
            style = MaterialTheme.typography.titleMedium.copy(
                fontFamily = FontFamily(Font(R.font.nunito_bold))
            ),
        )
        FlowRow(modifier = Modifier.padding(8.dp)) {
            ChipItem("Price: High to Low", isSelected = true)
            ChipItem("Avg rating: 4+")
            ChipItem("Free breakfast")
            ChipItem("Free cancellation")
            ChipItem("£50 pn")
        }
    }
}