package com.meesam.springshoppingclient.views.products


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BedroomChild
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.meesam.springshoppingclient.views.common.CollapsableView
import com.meesam.springshoppingclient.views.theme.AppTheme
import com.meesam.springshoppingclient.views.theme.subHeading

@Composable
fun ProductHighLights(modifier: Modifier = Modifier) {
    CollapsableView(
        modifier = modifier.fillMaxWidth(),
        title = "Product highlights",
        subTitle = "Key product attributes and feature details"
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)) {
            (1..5).map {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                ) {
                    Box(contentAlignment = Alignment.Center ,modifier = Modifier.size(40.dp).background(subHeading, shape = RoundedCornerShape(12.dp))) {
                        Icon(Icons.Outlined.BedroomChild, contentDescription = null)
                    }
                    Spacer(Modifier.width(18.dp))
                    Text("Content $it")
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ProductHighLightsPrev(modifier: Modifier = Modifier) {
    AppTheme {
        ProductHighLights()
    }
}

