package com.skymilk.wallpaperapp.store.domain.Repository

import androidx.paging.PagingData
import com.skymilk.wallpaperapp.store.domain.model.Hit
import kotlinx.coroutines.flow.Flow

interface WallPaperRepository {

    fun getHomeWallPapers(): Flow<PagingData<Hit>>

    fun getPopularWallPapers(): Flow<PagingData<Hit>>

    fun getRandomWallPapers(): Flow<PagingData<Hit>>

    fun getCategoryWallPapers(category: String): Flow<PagingData<Hit>>

    fun getSearchWallPapers(searchQuery: String): Flow<PagingData<Hit>>
}