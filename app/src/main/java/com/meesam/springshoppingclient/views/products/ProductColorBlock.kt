package com.meesam.springshoppingclient.views.products

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.meesam.springshoppingclient.R
import com.meesam.springshoppingclient.views.theme.AppTheme
import com.meesam.springshoppingclient.views.theme.subHeading

@Composable
fun ProductColorBlock(modifier: Modifier = Modifier, productColors: List<Long>) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Selected Color :", style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily(Font(R.font.nunito_bold))
                )
            )
            Spacer(Modifier.width(4.dp))
            Text(
                "Red", style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily(Font(R.font.nunito_regular)),
                    color = subHeading
                )
            )
        }
        Spacer(Modifier.height(4.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            items(productColors) { product ->
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color(product), shape = CircleShape)
                ) { }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ProductColorBlockPrev(modifier: Modifier = Modifier) {
    val colors = listOf(0XFF000000, 0xFFF10C0C, 0xFF0C4DF1)
    AppTheme {
        ProductColorBlock(productColors = colors)
    }
}