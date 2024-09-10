package com.skymilk.wallpaperapp.store.domain.usecase.history

data class SearchHistoryUseCases (
    val getSearchHistory: GetSearchHistory,
    val saveSearchHistory: SaveSearchHistory,
    val deleteSearchHistory: DeleteSearchHistory
)