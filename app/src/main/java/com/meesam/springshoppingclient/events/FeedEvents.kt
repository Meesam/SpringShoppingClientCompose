package com.meesam.springshoppingclient.events

sealed class FeedEvents {
    data class onCategorySelect(val categoryName: String): FeedEvents()
}