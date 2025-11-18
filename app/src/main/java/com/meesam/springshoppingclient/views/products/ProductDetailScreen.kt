package com.meesam.springshoppingclient.views.products


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
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
import com.meesam.springshoppingclient.viewmodel.ProductDetailViewModel


import com.meesam.springshoppingclient.viewmodel.ProductsViewModel
import com.meesam.springshoppingclient.views.common.RatingBlock
import com.meesam.springshoppingclient.views.theme.AppTheme
import com.meesam.springshoppingclient.views.theme.cardBackGround
import com.meesam.springshoppingclient.views.theme.inputBackGround
import com.meesam.springshoppingclient.views.theme.subHeading
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProductDetailScreen(productId: Long, onGoBack: () -> Unit) {
    val productsDetailViewModel: ProductDetailViewModel = hiltViewModel()
    val productDetail by productsDetailViewModel.productDetail.collectAsState()
    val productCounter by productsDetailViewModel.productCounter.collectAsState()

    LaunchedEffect(productId) {
        if (productId != 0.toLong()) {
            productsDetailViewModel.onEvent(ProductEvent.LoadProductById(productId))
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
    onClick: () -> Unit
) {
    val colors = listOf(0XFF000000, 0xFFF10C0C, 0xFF0C4DF1)
    val variants = listOf("64GB", "124GB", "256GB")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
                .drawBehind {
                    drawLine(
                        color = Color(0xFFB7BDCC),
                        start = Offset(x = 0f, y = size.height),
                        end = Offset(x = size.width, size.height),
                        strokeWidth = 2.5f
                    )
                }
                .padding(top = 50.dp, start = 8.dp)
        ) {
            IconButton(onClick = {
                onClick()
            }, shape = CircleShape, interactionSource = null) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                ProductImageBlock()
            }
            item { ProductTitleAndPriceBlock(modifier = modifier.padding(horizontal = 16.dp), productDetail = productDetail) }
            item { ProductColorBlock(modifier = modifier.padding(horizontal = 16.dp), productColors = colors) }
            item { ProductVariantBlock(modifier = modifier.padding(horizontal = 16.dp),productVariant = variants) }
            item { ProductDeliveryDetailBlock(modifier = modifier.padding(horizontal = 16.dp)) }
            item {
                ProductHighLights()
            }
            item {
                ProductHighLights()
            }
            item {
                ProductHighLights()
            }
            item {
                ProductHighLights()
            }
        }

        ProductBuyBlock()

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

    Column(modifier = modifier
        .fillMaxWidth()
        .padding(16.dp)) {

    }
}

@Composable
fun ProductTitleAndPriceBlock(modifier: Modifier = Modifier, productDetail: ProductResponse?) {
    productDetail?.let { product ->
        Column(modifier.fillMaxWidth()) {
            Text(
                product.title, style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily(Font(R.font.nunito_semibold))
                )
            )
            Text(
                product.description, style = MaterialTheme.typography.titleSmall.copy(
                    fontFamily = FontFamily(Font(R.font.nunito_regular)),
                    color = subHeading
                )
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "$" + product.price.toString(), style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FontFamily(Font(R.font.nunito_bold)),
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductTitleAndPriceBlockPrev() {
    val data = ProductResponse(
        title = "IPhone 16 Pro max",
        description = "This is Iphone",
        price = 12.55,
        categoryName = "Mobile",
        quantity = 32,
        isActive = true
    )
    AppTheme {
        ProductTitleAndPriceBlock(productDetail = data)
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
    val data = ProductResponse(
        title = "IPhone 16 Pro max",
        description = "This is Iphone",
        price = 12.55,
        categoryName = "Mobile",
        quantity = 32,
        isActive = true
    )
    AppTheme {
        ProductProperties(
            productCounter = 1,
            onIncreaseCount = {},
            onDecreaseCount = {},
            productDetail = data
        )
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

        Button(onClick = {}, modifier = Modifier.weight(1f)) {
            Text("Buy")
        }
    }
}

@Composable
fun ProductImageBlock(modifier: Modifier = Modifier) {
    val images = listOf(
        R.drawable.pager_image_1,
        R.drawable.pager_image_2,
        R.drawable.pager_image_3,
        R.drawable.pager_image_4
    )
    val pagerState = rememberPagerState(pageCount = {
        images.size
    })
    val pagerIsDragged by pagerState.interactionSource.collectIsDraggedAsState()

    val pageInteractionSource = remember { MutableInteractionSource() }
    val pageIsPressed by pageInteractionSource.collectIsPressedAsState()

    val autoAdvance = !pagerIsDragged && !pageIsPressed

    if (autoAdvance) {
        LaunchedEffect(pagerState, pageInteractionSource) {
            while (true) {
                delay(2000)
                val nextPage = (pagerState.currentPage + 1) % images.size
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }
    val coroutineScope = rememberCoroutineScope()

    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxSize()
            .background(cardBackGround)
    ) {
        HorizontalPager(state = pagerState) { page ->
            Image(
                painter = painterResource(id = images[page]),
                contentDescription = "pager_image_1",
                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxWidth()
                    .height(300.dp),
            )
        }

        RatingBlock(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 8.dp, start = 8.dp)
        )
        ShareAndWishListBlock(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp)
        )
    }

    Row(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val isActive = pagerState.currentPage == iteration

            // Animate the color
            val color by animateColorAsState(
                targetValue = if (isActive) MaterialTheme.colorScheme.primary else Color.LightGray,
                label = "Indicator Color",
                animationSpec = tween(durationMillis = 1000, easing = EaseIn)
            )
            if (pagerState.currentPage == iteration) {
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(height = 8.dp, width = 40.dp)
                        .clickable {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(iteration)
                            }
                        }
                )
            } else {
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                        .clickable {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(iteration)
                            }
                        }
                )
            }
        }
    }
}

@Composable
fun ShareAndWishListBlock(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        IconButton(
            onClick = {},
            shape = RoundedCornerShape(8.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.White.copy(0.5f),
                contentColor = Color.DarkGray.copy(0.5f)
            )
        ) {
            Icon(Icons.Outlined.FavoriteBorder, contentDescription = null)
        }
        Spacer(Modifier.height(16.dp))
        IconButton(
            onClick = {},
            shape = RoundedCornerShape(8.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.White.copy(0.5f),
                contentColor = Color.DarkGray.copy(0.5f)
            )
        ) {
            Icon(Icons.Outlined.Share, contentDescription = null)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ProductImageBlockPrev(modifier: Modifier = Modifier) {
    AppTheme {
        // ProductImageBlock()
    }
}