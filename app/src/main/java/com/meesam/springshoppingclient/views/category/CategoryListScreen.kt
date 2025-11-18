package com.meesam.springshoppingclient.views.category

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Boy
import androidx.compose.material.icons.outlined.CameraIndoor
import androidx.compose.material.icons.outlined.Face4
import androidx.compose.material.icons.outlined.Man3
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.RemoveFromQueue
import androidx.compose.material.icons.outlined.SelectAll
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.meesam.springshoppingclient.R
import com.meesam.springshoppingclient.events.FeedEvents
import com.meesam.springshoppingclient.states.AppState
import com.meesam.springshoppingclient.viewmodel.CategoryViewModel
import com.meesam.springshoppingclient.views.theme.AppTheme
import kotlinx.coroutines.launch
import kotlin.math.abs


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

    var selectedItemIndex by remember { mutableIntStateOf(0) }
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // 1. Animatable for line's offset and width
    val lineOffsetX = remember { Animatable(0f) }
    val lineWidth = remember { Animatable(0f) }

    val density = LocalDensity.current
    val lineHeight = 4.dp
    val itemWidthDp = 85.dp

    LaunchedEffect(selectedItemIndex) {
        // --- 1. SETUP THE CUSTOM SCROLL ---
        val scrollAnimatable = Animatable(0f)
        var prevValue = 0f

        // The item's desired position after scrolling (e.g., centered or near the start)
        val targetOffsetPx = with(density) { itemWidthDp.toPx() } * 2 // Adjust this for centering

        val selectedItemInfo = lazyListState.layoutInfo.visibleItemsInfo
            .find { it.index == selectedItemIndex }

        val scrollDistance = selectedItemInfo?.let { it.offset - targetOffsetPx } ?: 0f

        // --- 2. CREATE AN OBSERVER using snapshotFlow ---
        // This coroutine will watch the scrollAnimatable's value.
        // When the value changes, it will scroll the list by the difference.
        val scrollJob = launch {
            snapshotFlow { scrollAnimatable.value }
                .collect { currentValue ->
                    val delta = currentValue - prevValue
                    lazyListState.scrollBy(delta) // This is now in a valid CoroutineScope!
                    prevValue = currentValue
                }
        }

        // --- 3. START THE ANIMATIONS ---

        // A. Start the scroll animation. This will trigger the observer above.
        if (abs(scrollDistance) > 1f) {
            scrollAnimatable.animateTo(
                targetValue = scrollDistance,
                animationSpec = tween(durationMillis = 300, easing = EaseInOut)
            )
        }

        // B. Once the scroll animation is finished, cancel the observer job to save resources.
        scrollJob.cancel()

        // C. Animate the indicator line to the item's FINAL position.
        val finalItemInfo = lazyListState.layoutInfo.visibleItemsInfo
            .find { it.index == selectedItemIndex }

        finalItemInfo?.let {
            val animationSpec = tween<Float>(durationMillis = 300, easing = EaseInOut)
            launch { lineOffsetX.animateTo(it.offset.toFloat(), animationSpec) }
            launch { lineWidth.animateTo(it.size.toFloat(), animationSpec) }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(65.dp) // Height for the LazyRow and the line
    ) {

        LazyRow(
            state = lazyListState,
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            itemsIndexed(categoryList) { index, item ->
                ItemCard(
                    text = item.title,
                    isSelected = index == selectedItemIndex,
                    progress = progress,
                    onClick = {
                        selectedItemIndex = index
                        categoryViewModel.onEvent(FeedEvents.onCategorySelect(item.title))
                    })
            }
        }

        // 2. The Line Indicator Box
        // We use a Box modifier with drawWithContent, which is triggered on every scroll/layout change.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(lineHeight) // The height of the line itself
                .align(Alignment.BottomStart)
                .padding(horizontal = 10.dp, vertical = 3.dp)
                .drawBehind {
                    // Find the real-time position of the selected item.
                    val selectedItemInfo =
                        lazyListState.layoutInfo.visibleItemsInfo.find { it.index == selectedItemIndex }

                    // --- THIS IS THE KEY CHANGE ---
                    // ONLY update and draw the line IF the selected item is currently visible.
                    selectedItemInfo?.let { itemInfo ->
                        // Handle manual scrolling: If no animation is running, snap the line
                        // to the item's current position.
                        if (!lineOffsetX.isRunning) {
                            coroutineScope.launch {
                                lineOffsetX.animateTo(
                                    targetValue = itemInfo.offset.toFloat(),
                                    // A spring is great for tracking changes smoothly.
                                    // Play with dampingRatio and stiffness to get the feel you want.
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        easing = EaseInOut
                                    )
                                )
                                lineWidth.animateTo(
                                    targetValue = itemInfo.size.toFloat(),
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        easing = EaseInOut
                                    )
                                )
                            }
                        }

                        // Always draw the line using the current value from Animatable.
                        // This block is now only reached when the item is visible.
                        val y = size.height / 2
                        drawLine(
                            color = Color(0xFF39349E),
                            start = Offset(x = lineOffsetX.value, y = y),
                            end = Offset(x = lineOffsetX.value + lineWidth.value, y = y),
                            strokeWidth = 12f,
                            cap = StrokeCap.Round
                        )
                    }
                }
        )
    }
}

@Composable
private fun ItemCard(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    progress: Float,
    onClick: () -> Unit,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable(indication = null, interactionSource = null) { onClick() }) {
        Row(modifier = Modifier.graphicsLayer {
            scaleY = progress
            scaleX = progress
        }) {
            Box(
                contentAlignment = Alignment.Center, // This centers the Icon
                modifier = Modifier
                    .size(40.dp) // Define the total area for the background and icon
                    .then(
                        if (isSelected) {
                            // 2. Use drawBehind to draw the background for the Box
                            Modifier
                                .drawBehind {
                                    // We use `this.size` to get the responsive size of the Box (40.dp x 40.dp in pixels)
                                    val cornerRadius = CornerRadius(8.dp.toPx())
                                    drawRoundRect(
                                        brush = Brush.verticalGradient(
                                            listOf(
                                                Color(0xFF39349E).copy(
                                                    0.5f
                                                ), Color.White
                                            )
                                        ),
                                        size = this.size, // Draw the rect to fill the entire Box
                                        cornerRadius = cornerRadius
                                    )
                                }

                        } else {
                            Modifier // If not selected, do nothing
                        }
                    )

            ) {
                // 3. The Icon is placed inside the Box and automatically centered
                Icon(
                    when(text){
                        "All" -> {
                           Icons.Outlined.SelectAll
                        }
                        "Electronics" -> {
                            Icons.Outlined.RemoveFromQueue
                        }
                        "Mobile" -> {
                            Icons.Outlined.PhoneAndroid
                        }
                        "Clothes" -> {
                            Icons.Outlined.Man3
                        }
                        "Books" -> {
                            Icons.Outlined.AutoStories
                        }
                        "Fashion" -> {
                            Icons.Outlined.Face4
                        }
                        "Home Appliances" -> {
                            Icons.Outlined.CameraIndoor
                        }
                        "Kid's Wear" -> {
                            Icons.Outlined.Boy
                        }
                        "Toys" -> {
                            Icons.Outlined.SmartToy
                        }
                        else -> Icons.Outlined.AutoStories
                    },
                    contentDescription = "",
                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer.copy(
                        0.5f
                    )
                )
            }
        }


        Row {
            Text(
                text,
                maxLines = 1,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer.copy(
                    0.5f
                ),
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = FontFamily(Font(R.font.nunito_bold))
                ),
                modifier = Modifier.width(80.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemCardAnimationPreview() {
    // This creates an infinitely looping animation value between 0f and 1f
    val infiniteTransition = rememberInfiniteTransition(label = "progressAnimation")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse // It will go 0 -> 1 -> 0 -> 1 ...
        ),
        label = "progress"
    )

    AppTheme {
        // Now we test the ItemCard with a progress value that we KNOW is changing
        ItemCard(
            text = "Animation Test",
            isSelected = true,
            progress = progress, // Use the animated progress value here
            onClick = {}
        )
    }
}


