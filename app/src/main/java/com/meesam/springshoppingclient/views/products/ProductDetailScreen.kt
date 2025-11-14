package com.meesam.springshoppingclient.views.products


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meesam.springshoppingclient.R
import com.meesam.springshoppingclient.events.ProductEvent
import com.meesam.springshoppingclient.model.Product
import com.meesam.springshoppingclient.model.ProductResponse
import com.meesam.springshoppingclient.states.AppState


import com.meesam.springshoppingclient.viewmodel.ProductsViewModel
import com.meesam.springshoppingclient.views.theme.AppTheme

@Composable
fun ProductDetailScreen(productId: Long, onGoBack: () -> Unit) {
    val productsViewModel: ProductsViewModel = hiltViewModel()
    val productDetail by productsViewModel.productDetail.collectAsState()
    val productCounter by productsViewModel._productCounter.collectAsState()

    LaunchedEffect(productId) {
        if (productId != 0.toLong()) {
            productsViewModel.onEvent(ProductEvent.LoadProductById(productId))
        }
    }

    when (val result = productDetail) {
        is AppState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(result.errorMessage.toString())
            }
        }

        AppState.Idle -> {}
        AppState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        is AppState.Success -> {
            ProductDetail(
                productDetail = result.data,
                productCounter = productCounter,
                onIncreaseCount = {
                    productsViewModel.onEvent(
                        ProductEvent.ProductCountIncrement
                    )
                },
                onDecreaseCount = {
                    productsViewModel.onEvent(
                        ProductEvent.ProductCountDecrement
                    )
                },
            ) {
                onGoBack()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetail(
    modifier: Modifier = Modifier,
    productDetail: ProductResponse?,
    productCounter: Int,
    onDecreaseCount: () -> Unit,
    onIncreaseCount: () -> Unit,
    onClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 50.dp)
                .verticalScroll(scrollState)
        ) {
            Box {
                Image(
                    painter = painterResource(id = R.drawable.onboarding_image),
                    contentDescription = "Logo",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                )
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 40.dp, start = 20.dp)
                        .background(
                            brush = SolidColor(
                                value = Color.LightGray
                            ), shape = CircleShape, alpha = 0.2f
                        )
                        .size(40.dp)
                        .clickable { onClick() }

                ) {
                    Icon(
                        Icons.Default.ChevronLeft,
                        contentDescription = "Favorite",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .size(25.dp)
                            .graphicsLayer {
                                alpha = 2f
                            }
                    )
                }
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 40.dp, end = 20.dp)
                        .background(
                            brush = SolidColor(
                                value = Color.LightGray
                            ), shape = CircleShape, alpha = 0.2f
                        )
                        .size(40.dp)

                ) {
                    Icon(
                        Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .size(20.dp)
                            .graphicsLayer {
                                alpha = 2f
                            }
                    )
                }
            }
            ProductProperties(
                productCounter = productCounter,
                onDecreaseCount = onDecreaseCount,
                onIncreaseCount = onIncreaseCount,
                productDetail = productDetail
            )
            ProductProperties(
                productCounter = productCounter,
                onDecreaseCount = onDecreaseCount,
                onIncreaseCount = onIncreaseCount,
                productDetail = productDetail
            )
            ProductProperties(
                productCounter = productCounter,
                onDecreaseCount = onDecreaseCount,
                onIncreaseCount = onIncreaseCount,
                productDetail = productDetail
            )
            ProductProperties(
                productCounter = productCounter,
                onDecreaseCount = onDecreaseCount,
                onIncreaseCount = onIncreaseCount,
                productDetail = productDetail
            )
            ProductProperties(
                productCounter = productCounter,
                onDecreaseCount = onDecreaseCount,
                onIncreaseCount = onIncreaseCount,
                productDetail = productDetail
            )
        }

        ProductBuyBlock(modifier = Modifier.align(Alignment.BottomCenter))
    }

}

@Composable
fun ProductProperties(
    modifier: Modifier = Modifier,
    productCounter: Int,
    onIncreaseCount: () -> Unit,
    onDecreaseCount: () -> Unit,
    productDetail: ProductResponse?
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    productDetail?.title.toString(), style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(Modifier.height(12.dp))

                Row {
                    Text(
                        "$" + productDetail?.price.toString(), style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                Spacer(Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = "Review", tint = Color.Yellow)
                    Spacer(Modifier.width(10.dp))
                    Text("4.8", style = TextStyle(fontWeight = FontWeight.Bold))
                    Spacer(Modifier.width(3.dp))
                    Text(
                        "(320 Review)",
                        style = TextStyle(
                            fontWeight = FontWeight.Normal,
                            color = Color.LightGray,
                            fontSize = 14.sp
                        )
                    )
                }
            }
            Column {
                ProductCounter(
                    productCounter = productCounter,
                    onIncreaseCount = onIncreaseCount,
                    onDecreaseCount = onDecreaseCount
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    "Available in stock",
                    style = TextStyle(fontWeight = FontWeight.SemiBold),
                    fontSize = 14.sp
                )
            }
        }

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Colors", style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold))
            Spacer(Modifier.height(10.dp))
            Row {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Red, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Check",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                Spacer(Modifier.width(15.dp))

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Black, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {

                }
                Spacer(Modifier.width(15.dp))

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.primary, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {

                }
                Spacer(Modifier.width(15.dp))

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Green, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {

                }
            }
        }

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Description", style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold))
            Spacer(Modifier.height(10.dp))
            Text(productDetail?.description.toString())
        }
    }
}

@Composable
fun ProductCounter(
    modifier: Modifier = Modifier,
    productCounter: Int,
    onIncreaseCount: () -> Unit,
    onDecreaseCount: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .width(100.dp)
            .background(
                MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(3.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
                .background(
                    brush = SolidColor(
                        value = Color.LightGray
                    ), shape = CircleShape, alpha = 0.5f
                )
                .size(30.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { onDecreaseCount() })
        ) {
            Icon(
                Icons.Filled.Remove, contentDescription = "Decrease",
                tint = MaterialTheme.colorScheme.primary,

                modifier = Modifier
                    .size(25.dp)
                    .graphicsLayer {
                        alpha = 2f
                    }
            )
        }

        Text(productCounter.toString())
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
                .background(
                    brush = SolidColor(
                        value = Color.LightGray
                    ), shape = CircleShape, alpha = 0.5f
                )
                .size(30.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { onIncreaseCount() })
        ) {
            Icon(
                Icons.Filled.Add, contentDescription = "Decrease",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(25.dp)
                    .graphicsLayer {
                        alpha = 2f
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailPreview() {
    AppTheme {
        //ProductCounter()
    }
}

@Composable
fun ProductBuyBlock(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
            .drawBehind {
                drawLine(
                    color = Color(0xFFB7BDCC),
                    start = Offset(x = 0f, y = -10f),
                    end = Offset(x = size.width, -10f),
                    strokeWidth = 1f
                )
            }
            .padding(bottom = 50.dp, start = 16.dp, end = 16.dp, top = 16.dp)

    ) {
        Button(onClick = {}, modifier = Modifier.weight(1f)) {
            Text("Add to Cart")
        }

        Spacer(Modifier.width(16.dp))

        Button(onClick = {},modifier = Modifier.weight(1f)) {
            Text("Buy")
        }
    }
}