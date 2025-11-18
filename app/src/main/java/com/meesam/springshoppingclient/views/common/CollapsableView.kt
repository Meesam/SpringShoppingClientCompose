package com.meesam.springshoppingclient.views.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.meesam.springshoppingclient.R
import com.meesam.springshoppingclient.views.theme.subHeading

@Composable
fun CollapsableView(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String,
    content: @Composable () -> Unit
) {
    var isExpanded by rememberSaveable { mutableStateOf(true) }
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                title, style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily(Font(R.font.nunito_semibold)),
                )
            )
            Icon(
                if (!isExpanded) Icons.Outlined.KeyboardArrowDown else Icons.Outlined.KeyboardArrowUp,
                contentDescription = null,
                modifier = Modifier.clickable(indication = null, interactionSource = null) {
                    isExpanded = !isExpanded
                }
            )

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                subTitle, style = MaterialTheme.typography.titleSmall.copy(
                    fontFamily = FontFamily(Font(R.font.nunito_regular)),
                    color = subHeading
                )
            )
        }
        Divider(color = subHeading, startIndent = 10.dp, modifier = Modifier.padding(end = 16.dp))
        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth(),
            visible = isExpanded
        ) {
            content()
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CollapsableViewPrev(modifier: Modifier = Modifier) {
    CollapsableView(
        title = "Product highlights",
        subTitle = "Key product attributes and feature details"
    ) {
        Text(
            "asdasdasdasdasdasdasdasdasdasdasasdasdasdasdasdasdasdasd",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}