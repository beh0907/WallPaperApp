package com.skymilk.wallpaperapp.store.domain.usecase.history

import com.skymilk.wallpaperapp.store.domain.repository.SearchHistoryRepository

class DeleteSearchHistory(
    private val searchHistoryRepository: SearchHistoryRepository
) {

    suspend operator fun invoke(history: String) {
        searchHistoryRepository.deleteSearchHistory(history)
    }
}