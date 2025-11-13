package com.meesam.springshoppingclient.views.common


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.meesam.springshoppingclient.R
import com.meesam.springshoppingclient.navigation.AppDestinations
import com.meesam.springshoppingclient.navigation.BottomNavigationItem
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

private object NoRippleInteractionSource : MutableInteractionSource {
    override val interactions: Flow<Interaction> = emptyFlow()
    override suspend fun emit(interaction: Interaction) {}
    override fun tryEmit(interaction: Interaction) = true
}

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    isAdminLoggedIn: Boolean,
    onTabSelected: (String) -> Unit,
) {
    val items =
        listOf(
            BottomNavigationItem(
                "Home",
                if (currentRoute == AppDestinations.FEED_ROUTE) Icons.Filled.Home else Icons.Outlined.Home,
                AppDestinations.FEED_ROUTE
            ),
            BottomNavigationItem(
                "Explore",
                if (currentRoute == AppDestinations.PRODUCT_ROUTE) Icons.Filled.CardGiftcard else Icons.Outlined.CardGiftcard,
                AppDestinations.PRODUCT_ROUTE
            ),
            BottomNavigationItem(
                "Favorite",
                if (currentRoute == AppDestinations.FAVORITE_ROUTE) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                AppDestinations.FAVORITE_ROUTE
            ),
            BottomNavigationItem(
                "Cart",
                if (currentRoute == AppDestinations.CART_ROUTE) Icons.Filled.ShoppingCart else Icons.Outlined.ShoppingCart,
                AppDestinations.CART_ROUTE
            ),
            BottomNavigationItem(
                "Profile",
                if (currentRoute == AppDestinations.PROFILE_ROUTE) Icons.Filled.Person else Icons.Outlined.Person,
                AppDestinations.PROFILE_ROUTE
            )
        )

    val animatedSize = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        animatedSize.animateTo(
            1.2f, animationSpec = tween(
                durationMillis = 500,
                delayMillis = 50,
                easing = LinearOutSlowInEasing
            )
        )
    }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        modifier = Modifier.border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(0.1f)
        )

    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                icon = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(
                                if (selected) {
                                    Modifier
                                        .background(
                                            brush = Brush.verticalGradient(
                                                listOf(
                                                    Color.Transparent,
                                                    MaterialTheme.colorScheme.primaryContainer.copy(
                                                        0.3f
                                                    ),
                                                )
                                            ),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .drawBehind {
                                            drawLine(
                                                color = Color(0xFF39349E),
                                                start = Offset(x = 0f, y = -10f),
                                                end = Offset(x = size.width, -10f),
                                                strokeWidth = 12f
                                            )
                                        }
                                        .graphicsLayer {
                                            scaleX = animatedSize.value
                                            scaleY = animatedSize.value
                                            clip = true
                                        }
                                } else Modifier
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            item.icon,
                            contentDescription = item.title,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                },
                label = {
                    Text(
                        item.title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontFamily = FontFamily(Font(R.font.nunito_bold))
                        )
                    )
                },
                selected = selected,
                onClick = {
                    scope.launch {
                        animatedSize.snapTo(0f)
                        animatedSize.animateTo(
                            1.2f, animationSpec = tween(
                                durationMillis = 500,
                                delayMillis = 50,
                                easing = LinearOutSlowInEasing
                            )
                        )
                    }
                    onTabSelected(item.route)
                },
                colors = NavigationBarItemColors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    selectedIndicatorColor = Color.Transparent,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                    disabledIconColor = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                    disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                ),
                interactionSource = NoRippleInteractionSource,
            )
        }
    }
}