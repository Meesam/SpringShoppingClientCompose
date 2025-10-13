package com.meesam.springshoppingclient.views.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.meesam.springshoppingclient.R
import com.meesam.springshoppingclient.model.Product
import com.meesam.springshoppingclient.views.theme.AppTheme


@Composable
fun ProductItem(modifier: Modifier = Modifier, item: Product?, onClick:(String)-> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        modifier = modifier
            .width(200.dp)
            .clickable{
                onClick(item?.title ?: "")
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.onboarding_image),
            contentDescription = "Product Image",
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .height(200.dp),
            contentScale = ContentScale.FillWidth
        )
        Column( horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Text(
                text = item?.title ?: "",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                "$" + item?.price.toString(),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun ProductItemPreview() {

    val data = Product(
        title = "IPhone",
        description = "This is Iphone",
        price = 12.55,
        category = "Mobile"
    )
    AppTheme {
        ProductItem(item= data){}
    }

}