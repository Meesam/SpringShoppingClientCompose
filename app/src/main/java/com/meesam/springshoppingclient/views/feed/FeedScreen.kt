package com.meesam.springshoppingclient.views.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meesam.springshoppingclient.R
import com.meesam.springshoppingclient.events.FeedEvents
import com.meesam.springshoppingclient.model.Product
import com.meesam.springshoppingclient.states.AppState
import com.meesam.springshoppingclient.views.common.ProductItem
import com.meesam.springshoppingclient.viewmodel.FeedViewModel
import com.meesam.springshoppingclient.views.theme.AppTheme


@Composable
fun FeedScreen(onProductClick: (String) -> Unit) {
    val feedViewModel: FeedViewModel = hiltViewModel()
    val categories by feedViewModel.categories.collectAsState()
    val products by feedViewModel.products.collectAsState()
    val loading by feedViewModel._isLoading.collectAsState()
    val productHistory by feedViewModel.productsHistory.collectAsState()
    val recommendedProduct by feedViewModel.recommendedProducts.collectAsState()
    val scrollState = rememberScrollState()

    if (loading) {
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
                .verticalScroll(scrollState)
        ) {
            when (val result = categories) {
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
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            LazyRow(
                                modifier = Modifier.padding(vertical = 6.dp),
                            ) {
                                items(result.data) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .background(
                                                if (it.isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary,
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            .padding(10.dp)
                                            .clickable {
                                                feedViewModel.onEvent(FeedEvents.onCategorySelect(it.title))
                                            }
                                    ) {
                                        Text(
                                            it.title,
                                            color = if (it.isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                                            //style = TextStyle(fontWeight = FontWeight.Bold, fontFamily = NunitoFamily)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                }
                            }
                        }
                    }
                }
            }
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
                                    style = MaterialTheme.typography.titleSmall
                                    ,color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            LazyRow(
                                modifier = Modifier.padding(vertical = 6.dp),
                            ) {
                                items(result.data) {product->
                                    ProductItem( item = product){
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
                                    style = MaterialTheme.typography.titleSmall
                                    ,color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            LazyRow(
                                modifier = Modifier.padding(vertical = 6.dp),
                            ) {
                                items(result.data) {product->
                                    ProductItem( item = product){
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
                                items(result.data) {product->
                                    ProductItem( item = product){
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

@Preview(showBackground = true)
@Composable
fun PagePreview() {
    AppTheme {
        //ProductGrid()
       // Pager()
    }

}