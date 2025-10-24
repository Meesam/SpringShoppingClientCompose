package com.meesam.springshoppingclient.views.feed

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.meesam.springshoppingclient.R
import com.meesam.springshoppingclient.events.FeedEvents
import com.meesam.springshoppingclient.states.AppState
import com.meesam.springshoppingclient.views.common.ProductItem
import com.meesam.springshoppingclient.viewmodel.FeedViewModel
import com.meesam.springshoppingclient.views.category.CategoryList
import com.meesam.springshoppingclient.views.category.CategoryListScreen
import com.meesam.springshoppingclient.views.common.AppTopBar
import com.meesam.springshoppingclient.views.common.ProductPager
import com.meesam.springshoppingclient.views.theme.AppTheme


@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    maxHeaderHeight: Dp,
    minHeaderHeight: Dp,
    currentOffset: Float,
    mainNavController: NavHostController,
    onProductClick: (String) -> Unit
) {
    val feedViewModel: FeedViewModel = hiltViewModel()
    // val categories by feedViewModel.categories.collectAsState()
    val products by feedViewModel.products.collectAsState()
    val loading by feedViewModel._isLoading.collectAsState()
    val productHistory by feedViewModel.productsHistory.collectAsState()
    val recommendedProduct by feedViewModel.recommendedProducts.collectAsState()
    val scrollState = rememberScrollState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        // Add some padding around the list
        contentPadding = PaddingValues(top = maxHeaderHeight),
        // Add spacing between each item
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // This is the simplest way to create a large number of items.
        // The `items` block will be repeated 1000 times.
        item {
            ProductPager(modifier = Modifier.padding(top = 16.dp))
        }
        items(100) { index ->
            Text(
                text = "This is a real Feed Item #$index",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onProductClick("productID_$index") }
                    .padding(vertical = 16.dp)
            )
        }
    }

    AppTopBar(
        mainNavController = mainNavController,
        currentOffset = currentOffset,
        maxHeaderHeight = maxHeaderHeight,
        minHeaderHeight = minHeaderHeight
    )

    // val categoryList =
    // listOf("All", "Mobile", "Electronics", "Books", "Clothes", "Fashion", "Home Appliances")

    /*val categoryList = mutableListOf<CategoryItem>(
        CategoryItem(
            id = 1,
            title = "Mobile",
            isSelected = false
        ),
        CategoryItem(
            id = 2,
            title = "Electronics",
            isSelected = false
        ),
        CategoryItem(
            id = 3,
            title = "Books",
            isSelected = false
        ),
        CategoryItem(
            id = 4,
            title = "Clothes",
            isSelected = false
        ),
        CategoryItem(
            id = 5,
            title = "Fashion",
            isSelected = false
        ),
        CategoryItem(
            id = 6,
            title = "Home Appliances",
            isSelected = false
        ),
    )*/

    /*if (loading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
                .verticalScroll(scrollState)
                .padding(vertical = 16.dp)
        ) {
            /*when(val category = categories){
                is AppState.Error ->{}
                AppState.Idle -> {

                }
                AppState.Loading ->{}
                is AppState.Success<*> ->{
                    CategoryList(
                        categoryList = category.data as List<CategoryItem>,
                        feedViewModel = feedViewModel
                    )
                }
            }*/

            Spacer(modifier = Modifier.height(16.dp))
            //Pager()
            when (val result = products) {
                is AppState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(result.errorMessage.toString())
                    }
                }

                is AppState.Idle -> {}
                is AppState.Loading -> {
                    /*Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }*/
                }

                is AppState.Success -> {
                    if (result.data.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Text("No Product available", fontFamily = NunitoFamily)
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    "Top Products",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.secondary
                                )

                                Text(
                                    "See All",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            LazyRow(
                                modifier = Modifier.padding(vertical = 6.dp),
                            ) {
                                items(result.data) { product ->
                                    ProductItem(item = product) {
                                        onProductClick(product.id)
                                    }
                                    Spacer(Modifier.width(10.dp))
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            when (val result = productHistory) {
                is AppState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(result.errorMessage.toString())
                    }
                }

                is AppState.Idle -> {}
                is AppState.Loading -> {
                    /*Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }*/
                }

                is AppState.Success -> {
                    if (result.data.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("No Category available")
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    "Recently bought Products",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.secondary
                                )

                                Text(
                                    "See All",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            LazyRow(
                                modifier = Modifier.padding(vertical = 6.dp),
                            ) {
                                items(result.data) { product ->
                                    ProductItem(item = product) {
                                        onProductClick(product.id)
                                    }
                                    Spacer(Modifier.width(10.dp))
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            when (val result = recommendedProduct) {
                is AppState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(result.errorMessage.toString())
                    }
                }

                is AppState.Idle -> {}
                is AppState.Loading -> {
                    /*Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }*/
                }

                is AppState.Success -> {
                    if (result.data.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("No Category available")
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    "Recommended deals for you",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            LazyRow(
                                modifier = Modifier.padding(vertical = 6.dp),
                            ) {
                                items(result.data) { product ->
                                    ProductItem(item = product) {
                                        onProductClick(product.id)
                                    }
                                    Spacer(Modifier.width(10.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }*/
}


@Composable
fun DummyProductItem(index: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Dummy Product Item #$index",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/*
@Composable
fun Pager() {
    val images = listOf(
        R.drawable.pager_image_1,
        R.drawable.pager_image_2,
        R.drawable.pager_image_3,
        R.drawable.pager_image_4 // Assuming you want to repeat pager_image_2 for the 4th item
    )
    val pagerState = rememberPagerState(pageCount = {
        images.size
    })
    HorizontalPager(state = pagerState) { page ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(200.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = images[page]),
                contentDescription = "pager_image_1",
                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxSize(),
                contentScale = ContentScale.FillWidth
            )
        }
    }
    Row(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else Color.LightGray
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(8.dp)
            )
        }
    }


}*/

/*
@Composable
fun CategoryList(modifier: Modifier = Modifier, categoryList: List<CategoryItem>,feedViewModel: FeedViewModel) {
    var indicatorOffset by remember { mutableStateOf(0.dp) }
    var indicatorWidth by remember { mutableStateOf(0.dp) }

    val animatedIndicatorOffset by animateDpAsState(targetValue = indicatorOffset, label = "indicatorOffset")
    val animatedIndicatorWidth by animateDpAsState(targetValue = indicatorWidth, label = "indicatorWidth")

    val density = LocalDensity.current


    LazyRow(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLowest
            )
            .padding(16.dp)
            .drawBehind {
                val strokeWidth = 3.dp.toPx()
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = Color(0xFF39349E),
                    start = Offset(animatedIndicatorOffset.toPx(), y),
                    end = Offset(animatedIndicatorOffset.toPx() + animatedIndicatorWidth.toPx(), y),
                    strokeWidth = strokeWidth
                )
            },
    ) {
        items(categoryList) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .onGloballyPositioned { layoutCoordinates ->
                        if (it.isSelected) {
                            // This is the position of the item within the LazyRow
                            val itemOffset = layoutCoordinates.positionInParent().x
                            val itemWidth = layoutCoordinates.size.width

                            with(density) {
                                indicatorOffset = itemOffset.toDp()
                                indicatorWidth = itemWidth.toDp()
                            }
                        }
                    }
                    .clickable(
                        indication = null,
                        interactionSource = null
                    ) {
                        feedViewModel.onEvent(FeedEvents.onCategorySelect(it.title))
                    }

            ) {
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier

                        .background(
                            brush = if (it.isSelected) Brush.verticalGradient(
                                listOf(Color.LightGray, Color.Transparent)
                            ) else Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    Color.Transparent
                                )
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(8.dp)
                ) {
                    Icon(
                        Icons.Outlined.ShoppingCart, contentDescription = "",
                        modifier = Modifier
                            .size(25.dp)
                    )
                }

                Text(
                    it.title,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .align(Alignment.CenterHorizontally)
                        .width(50.dp),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily(Font(if (it.isSelected) R.font.nunito_bold else R.font.nunito_semibold))
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.width(20
                .dp))
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PagePreview() {
    val categoryList = mutableListOf<CategoryItem>(
        CategoryItem(
            id = 1,
            title = "Mobile",
            isSelected = false
        ),
        CategoryItem(
            id = 2,
            title = "Electronics",
            isSelected = true
        ),
        CategoryItem(
            id = 3,
            title = "Books",
            isSelected = false
        ),
        CategoryItem(
            id = 4,
            title = "Clothes",
            isSelected = false
        ),
        CategoryItem(
            id = 5,
            title = "Fashion",
            isSelected = false
        ),
        CategoryItem(
            id = 6,
            title = "Home Appliances",
            isSelected = false
        ),
    )
    AppTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
          //  CategoryList(categoryList = categoryList)
        }

    }

}*/