package com.meesam.springshoppingclient.views.products

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
fun ProductVariantBlock(modifier: Modifier = Modifier, productVariant: List<String>) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Selected Variant :", style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily(Font(R.font.nunito_bold))
                )
            )
            Spacer(Modifier.width(4.dp))
            Text(
                "64GB", style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily(Font(R.font.nunito_regular)),
                    color = subHeading
                )
            )
        }
        Spacer(Modifier.height(4.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            items(productVariant) { product ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(12.dp))
                        .border(1.dp, color = subHeading, shape = RoundedCornerShape(12.dp))
                ) {
                    Text(product)
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ProductVariantBlockPrev(modifier: Modifier = Modifier) {
    val variants = listOf("64GB", "124GB", "256GB")
    AppTheme {
        ProductVariantBlock(productVariant = variants)
    }
}