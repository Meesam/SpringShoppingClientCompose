package com.meesam.springshoppingclient.views.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.meesam.springshoppingclient.R
import com.meesam.springshoppingclient.model.Product
import com.meesam.springshoppingclient.model.ProductResponse
import com.meesam.springshoppingclient.views.theme.AppTheme
import com.meesam.springshoppingclient.views.theme.cardBackGround
import com.meesam.springshoppingclient.views.theme.inputBackGround
import com.meesam.springshoppingclient.views.theme.subHeading


@Composable
fun ProductItem(modifier: Modifier = Modifier, item: ProductResponse?, onClick: (Long) -> Unit) {
    Column(
        modifier = Modifier
            .width(200.dp)
            .height(300.dp)
            .background(color = cardBackGround, shape = RoundedCornerShape(10.dp))
            .clickable {
                onClick(item?.id ?: 0)
            }
    ) {
        Box(Modifier.fillMaxWidth()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                modifier = modifier
                    .padding(5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.onboarding_image),
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    contentScale = ContentScale.FillWidth
                )
            }
            Icon(
                Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                tint = subHeading,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 16.dp)
            )
            RatingBlock(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 8.dp, start = 8.dp)
            )

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Brand name",
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontFamily = FontFamily(Font(R.font.nunito_bold))
                    )
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = item?.title ?: "",
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily(Font(R.font.nunito_regular))
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }


            Text(
                "$" + item?.price.toString(),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontFamily = FontFamily(Font(R.font.nunito_bold))
                )
            )
        }
    }

}


@Composable
fun RatingBlock(modifier: Modifier = Modifier) {
    Row(
        modifier.background(Color.White, RoundedCornerShape(5.dp))
            .padding(horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Filled.Star,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(3.dp))
        Text(
            "4.3", style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = FontFamily(Font(R.font.nunito_regular))
            )
        )
        Spacer(Modifier.width(3.dp))
        Text(
            "(233)", style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = FontFamily(Font(R.font.nunito_regular))
            )
        )
    }
}

@Preview(showBackground = false)
@Composable
fun ProductItemPreview() {
    val data = ProductResponse(
        title = "IPhone 16 Pro max",
        description = "This is Iphone",
        price = 12.55,
        categoryName = "Mobile",
        quantity = 32,
        isActive = true
    )
    AppTheme {
        ProductItem(item = data) {}
    }
}