package com.meesam.springshoppingclient.views.category

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.meesam.springshoppingclient.R
import com.meesam.springshoppingclient.events.FeedEvents
import com.meesam.springshoppingclient.states.AppState
import com.meesam.springshoppingclient.viewmodel.CategoryViewModel


data class CategoryItem(val id: Int, val title: String, val isSelected: Boolean)

@Composable
fun CategoryListScreen(modifier: Modifier = Modifier, progress: Float) {
    val categoryViewModel: CategoryViewModel = hiltViewModel()
    val categoriesData = categoryViewModel.categories.collectAsState()
    when (val category = categoriesData.value) {
        AppState.Idle -> {}
        AppState.Loading -> {

        }

        is AppState.Success<*> -> {
            CategoryList(modifier, category.data as List<CategoryItem>, categoryViewModel, progress)
        }

        is AppState.Error -> {}
    }

}

@Composable
fun CategoryList(
    modifier: Modifier = Modifier,
    categoryList: List<CategoryItem>,
    categoryViewModel: CategoryViewModel,
    progress: Float
) {

    var indicatorOffset by remember { mutableStateOf(0.dp) }
    var indicatorWidth by remember { mutableStateOf(0.dp) }

    val animatedIndicatorOffset by animateDpAsState(
        targetValue = indicatorOffset,
        label = "indicatorOffset"
    )
    val animatedIndicatorWidth by animateDpAsState(
        targetValue = indicatorWidth,
        label = "indicatorWidth"
    )

    val density = LocalDensity.current

    LazyRow(
        modifier = modifier
            /*.background(
                color = MaterialTheme.colorScheme.surfaceContainerLowest
            )*/
            //.padding(top = 16.dp)
            .drawBehind {
                val strokeWidth = 3.dp.toPx()
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = Color(0xFFFFFFFF),
                    start = Offset(animatedIndicatorOffset.toPx(), y),
                    end = Offset(
                        animatedIndicatorOffset.toPx() + animatedIndicatorWidth.toPx(),
                        y
                    ),
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
                        categoryViewModel.onEvent(FeedEvents.onCategorySelect(it.title))
                    }

            ) {
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier
                        .background(
                            brush = if (it.isSelected && progress > 0.0f) Brush.verticalGradient(
                                listOf(Color.DarkGray.copy(0.4f * progress), Color.Transparent)
                            ) else Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    Color.Transparent
                                )
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(8.dp)
                    //.alpha(progress)
                ) {
                    val maxSelectedSize = 26.dp
                    val minSelectedSize =
                        22.dp // The size when header is collapsed and item is selected
                    val maxUnselectedSize = 25.dp
                    val minUnselectedSize =
                        21.dp // The size when header is collapsed and item is not selected

                    // 2. Determine which set of sizes to use based on the selection state
                    val startSize = if (it.isSelected) minSelectedSize else minUnselectedSize
                    val endSize = if (it.isSelected) maxSelectedSize else maxUnselectedSize

                    // 3. Calculate the current size using linear interpolation (lerp)
                    // When progress is 1.0 (expanded), size will be endSize.
                    // When progress is 0.0 (collapsed), size will be startSize.
                    val currentIconSize = lerp(
                        start = startSize,
                        stop = endSize,
                        fraction = progress
                    )
                    if (progress > 0.0f) {
                        Icon(
                            Icons.Outlined.ShoppingCart, contentDescription = "",
                            modifier = Modifier
                                .animateContentSize()
                                .size(currentIconSize),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                }

                Text(
                    it.title,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .align(Alignment.CenterHorizontally)
                        .width(50.dp),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily(Font(if (it.isSelected) R.font.nunito_bold else R.font.nunito_regular))
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(
                modifier = Modifier.width(
                    20
                        .dp
                )
            )
        }
    }


}


