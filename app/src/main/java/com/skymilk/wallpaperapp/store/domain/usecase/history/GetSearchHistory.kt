package com.skymilk.wallpaperapp.store.domain.usecase.history

import androidx.paging.PagingData
import com.skymilk.wallpaperapp.store.domain.model.Hit
import com.skymilk.wallpaperapp.store.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow

class GetSearchHistory(
    private val searchHistoryRepository: SearchHistoryRepository
) {

    operator fun invoke(): Flow<List<String>> {
        return searchHistoryRepository.getSearchHistory()
    }
}