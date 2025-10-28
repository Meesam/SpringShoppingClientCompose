package com.meesam.springshoppingclient.views.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.meesam.springshoppingclient.R
import com.meesam.springshoppingclient.navigation.AppDestinations
import com.meesam.springshoppingclient.navigation.BottomNavigationItem

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    isAdminLoggedIn: Boolean,
    onTabSelected: (String) -> Unit,
) {
    val items = if (!isAdminLoggedIn) {
        listOf(
            BottomNavigationItem("Home", Icons.Outlined.Home, AppDestinations.FEED_ROUTE),
            BottomNavigationItem(
                "Explore",
                Icons.Filled.Animation,
                AppDestinations.PRODUCT_ROUTE
            ),
            BottomNavigationItem(
                "Favorite",
                Icons.Outlined.Favorite,
                AppDestinations.FAVORITE_ROUTE
            ),
            BottomNavigationItem("Cart", Icons.Outlined.ShoppingCart, AppDestinations.CART_ROUTE),
            BottomNavigationItem("Profile", Icons.Outlined.Person, AppDestinations.PROFILE_ROUTE)
        )
    } else {
        listOf(
            BottomNavigationItem("Home", Icons.Outlined.Home, AppDestinations.ADMIN_DASHBOARD),
            BottomNavigationItem(
                "Product",
                Icons.Filled.ShoppingCart,
                AppDestinations.ADMIN_PRODUCT
            ),
            BottomNavigationItem("Category", Icons.Outlined.Menu, AppDestinations.ADMIN_CATEGORY),
            BottomNavigationItem("Profile", Icons.Outlined.Person, AppDestinations.PROFILE_ROUTE)
        )
    }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        //contentColor = Color(0XFF31488E),
        //containerColor = MaterialTheme.colorScheme.surface,
        // contentColor = MaterialTheme.colorScheme.onSurface,
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
                                        .drawBehind{
                                            drawLine(
                                                color = Color(0xFF39349E),
                                                start = Offset(x= 0f,y= -10f),
                                                end = Offset(x= size.width, -10f),
                                                strokeWidth = 12f
                                            )
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
                onClick = { onTabSelected(item.route) },
                colors = NavigationBarItemColors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    selectedIndicatorColor = Color.Transparent,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                    disabledIconColor = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                    disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                ),
                interactionSource = null,
            )


        }
    }
}