package com.meesam.springshoppingclient.navigation

object AppDestinations {
    const val EDIT_USER_SCREEN_ROUTE = "edit_user"
    const val HOME_SEARCH_ROUTE ="search"
    const val LOGIN_ROUTE = "login"

    const val OTP_ROUTE = "otp"

    const val REGISTER_ROUTE = "register"
    const val HOME_ROUTE = "home"

    const val SEARCH_SUGGESTION_ROUTE ="search_suggestion"

    const val ONBOARDING_ROUTE ="on_boarding"
    const val DEMO_ROUTE ="demo_route"

    const val ADMIN_HOME = "admin_home"
    const val ADMIN_CATEGORY = "admin_category"
    const val ADMIN_PRODUCT = "admin_product"
    const val ADMIN_DASHBOARD ="admin_dashboard"

    const val FEED_ROUTE = "feed"
    const val PROFILE_ROUTE = "profile"

    const val FAVORITE_ROUTE ="favorite_route"

    const val CART_ROUTE = "cart_rote"

    const val PRODUCT_ROUTE="product_list"
    const val EDIT_USER_ID_KEY = "id"

    const val ADD_NEW_CARD_ROUTE="add_new_card"
    const val CHECKOUT_ROUTE = "checkout"
    const val EDIT_PROFILE_ROUTE = "edit_profile"
    const val PAYMENT_SETTING_ROUTE = "payment_setting"

    const val PRODUCT_ID_KEY = "id"
    const val PRODUCT_DETAIL_SCREEN_ROUTE = "product_detail"
    const val PRODUCT_DETAIL_ROUTE_PATTERN = "$PRODUCT_DETAIL_SCREEN_ROUTE/{$PRODUCT_ID_KEY}"
    fun editUserRoute(id: Long) = "$EDIT_USER_SCREEN_ROUTE/$id"

    fun productDetailRoute(id: String) = "$PRODUCT_DETAIL_SCREEN_ROUTE/$id"

}

object ProfileSubDestinations{
    const val PROFILE_DETAILS ="profile/details"
    const val EDIT_PROFILE = "profile/edit"
    const val CHANGE_PASSWORD = "profile/changePassword"
    const val PAYMENT_SETTINGS = "profile/paymentSetting"
    const val NOTIFICATION_SETTINGS = "profile/notificationSetting"
}