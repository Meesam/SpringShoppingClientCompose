package com.meesam.springshoppingclient.views.feed


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.meesam.springshoppingclient.R
import com.meesam.springshoppingclient.model.ProductResponse
import com.meesam.springshoppingclient.navigation.AppDestinations
import com.meesam.springshoppingclient.states.AppState
import com.meesam.springshoppingclient.viewmodel.ProductsViewModel
import com.meesam.springshoppingclient.views.common.AppTopBar
import com.meesam.springshoppingclient.views.common.ProductItem
import com.meesam.springshoppingclient.views.common.ProductPager


@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    maxHeaderHeight: Dp,
    minHeaderHeight: Dp,
    currentOffset: Float,
    mainNavController: NavHostController,
    onProductClick: (String) -> Unit
) {
    val productsViewModel: ProductsViewModel = hiltViewModel()
    val productHistory by productsViewModel.productsHistory.collectAsState()
    val recommendedProduct by productsViewModel.recommendedProducts.collectAsState()
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationY = currentOffset * 1f
                },
        ) {
            item {
                Spacer(modifier = Modifier.height(maxHeaderHeight))
            }
            item {
                ProductPager()
            }
            item {
                ProductHeading(title = "Recommended for you") {}
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                when (val result = recommendedProduct) {
                    is AppState.Success -> {
                        RecommendedProductList(
                            recommendedProducts = result.data,
                            mainNavController = mainNavController
                        )
                    }

                    is AppState.Loading, AppState.Idle -> {}
                    else -> {}
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                ProductHeading(title = "Recently viewed") {}
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                when (val result = recommendedProduct) {
                    is AppState.Success -> {
                        RecommendedProductList(
                            recommendedProducts = result.data,
                            mainNavController = mainNavController
                        )
                    }

                    is AppState.Loading, AppState.Idle -> {}
                    else -> {}
                }
            }
        }

        AppTopBar(
            mainNavController = mainNavController,
            currentOffset = currentOffset,
            maxHeaderHeight = maxHeaderHeight,
            minHeaderHeight = minHeaderHeight
        )
    }
}

@Composable
fun RecommendedProductList(
    modifier: Modifier = Modifier,
    recommendedProducts: List<ProductResponse>,
    mainNavController: NavHostController
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(recommendedProducts) { product ->
            ProductItem(item = product) {
                val route = AppDestinations.PRODUCT_DETAIL_ROUTE_PATTERN.replace(
                    "{id}",
                    product.id.toString()
                )
                mainNavController.navigate(route)
            }
        }
    }
}

@Composable
fun ProductHeading(modifier: Modifier = Modifier, title: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            title, style = MaterialTheme.typography.titleMedium.copy(
                fontFamily = FontFamily(Font(R.font.nunito_bold))
            )
        )
        IconButton(
            onClick = {
                onClick()
            },
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Black),
            modifier = Modifier
                .height(25.dp)
                .width(55.dp)
        ) {
            Icon(
                Icons.Default.ArrowForward,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ProductHeadingPrev(modifier: Modifier = Modifier) {
    ProductHeading(title = "Recommended for you") {

    }
}



