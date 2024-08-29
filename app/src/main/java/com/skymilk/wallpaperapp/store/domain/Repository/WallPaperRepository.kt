package com.skymilk.wallpaperapp.store.domain.Repository

import androidx.paging.PagingData
import com.skymilk.wallpaperapp.store.domain.model.Hit
import kotlinx.coroutines.flow.Flow

interface WallPaperRepository {

    fun getHomeWallPaper(): Flow<PagingData<Hit>>

    fun getPopularWallPaper(): Flow<PagingData<Hit>>

    fun getRandomWallPaper(): Flow<PagingData<Hit>>

    fun getCategoryWallPaper(category: String): Flow<PagingData<Hit>>

    fun getSearchWallPaper(searchQuery: String): Flow<PagingData<Hit>>
}