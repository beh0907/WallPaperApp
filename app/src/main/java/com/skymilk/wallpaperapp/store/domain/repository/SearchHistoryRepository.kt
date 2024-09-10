package com.skymilk.wallpaperapp.store.domain.repository

import androidx.paging.PagingData
import com.skymilk.wallpaperapp.store.domain.model.Hit
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query

interface SearchHistoryRepository {

    suspend fun saveSearchHistory(history: String)

    suspend fun deleteSearchHistory(history: String)

    fun getSearchHistory(): Flow<List<String>>

}