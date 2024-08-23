package com.skymilk.wallpaperapp.store.domain.Repository

import androidx.paging.PagingData
import com.skymilk.wallpaperapp.store.domain.model.Data
import kotlinx.coroutines.flow.Flow

interface WallPaperRepository {

    fun getHomeWallPaper(): Flow<PagingData<Data>>

    fun getPopularWallPaper(): Flow<PagingData<Data>>

    fun getRandomWallPaper(): Flow<PagingData<Data>>

    fun getCategoryWallPaper(category: String): Flow<PagingData<Data>>
}