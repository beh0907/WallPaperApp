package com.skymilk.wallpaperapp.store.domain.usecase.history

import androidx.paging.PagingData
import com.skymilk.wallpaperapp.store.domain.model.Hit
import com.skymilk.wallpaperapp.store.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow

class SaveSearchHistory(
    private val searchHistoryRepository: SearchHistoryRepository
) {

    suspend operator fun invoke(history: String) {
        searchHistoryRepository.saveSearchHistory(history)
    }
}