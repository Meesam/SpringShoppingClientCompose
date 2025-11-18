package com.meesam.springshoppingclient.views.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meesam.springshoppingclient.views.products.ProductCounter
import com.meesam.springshoppingclient.R
import com.meesam.springshoppingclient.events.ProductEvent
import com.meesam.springshoppingclient.viewmodel.ProductDetailViewModel
import com.meesam.springshoppingclient.views.theme.AppTheme
import com.meesam.springshoppingclient.views.products.ProductCounter
import com.meesam.springshoppingclient.viewmodel.ProductsViewModel

@Composable
fun CartScreen() {
    val productDetailViewModel: ProductDetailViewModel = hiltViewModel()
    val productCounter by productDetailViewModel.productCounter.collectAsState()

    LazyColumn {
        items(10) {
            CartItem(
                productCounter = productCounter, onIncreaseCount = {
                    productDetailViewModel.onEvent(
                        ProductEvent.ProductCountIncrement
                    )
                },
                onDecreaseCount = {
                    productDetailViewModel.onEvent(
                        ProductEvent.ProductCountDecrement
                    )
                })
        }
    }
}


@Composable
fun CartItem(
    modifier: Modifier = Modifier,
    productCounter: Int,
    onIncreaseCount: () -> Unit,
    onDecreaseCount: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Box() {
            Image(
                painter = painterResource(id = R.drawable.onboarding_image),
                contentDescription = "Logo",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(20.dp))
            )

            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
                    .size(30.dp)
                    .background(
                        Color.Red,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    Icons.Default.DeleteForever,
                    contentDescription = "Remove",
                    modifier = Modifier.size(18.dp)
                )
            }

            ProductCounter(
                productCounter = productCounter,
                onIncreaseCount = onIncreaseCount,
                onDecreaseCount = onDecreaseCount,
                modifier = Modifier
                    .align(
                        Alignment.BottomStart
                    )
                    .padding(10.dp)
            )

        }

        Spacer(Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "Box headphone 345",
                    style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                )
                Row {
                    Text(
                        "Color:",
                        style = TextStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = Color.LightGray
                        )
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        "Brown",
                        style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    )
                }

            }

            Text("$32.89", style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 22.sp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CartItemPreview() {
    AppTheme {
        //CartItem()
    }
}

@Preview(showBackground = true)
@Composable
fun CartScreenPreview() {
    AppTheme {
        CartScreen()
    }
}