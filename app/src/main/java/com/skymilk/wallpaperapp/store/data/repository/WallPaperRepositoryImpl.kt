package com.skymilk.wallpaperapp.store.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.skymilk.wallpaperapp.store.data.remote.WallPaperApi
import com.skymilk.wallpaperapp.store.data.remote.paging.HomePagingSource
import com.skymilk.wallpaperapp.store.domain.Repository.WallPaperRepository
import com.skymilk.wallpaperapp.store.domain.model.Data
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class WallPaperRepositoryImpl @Inject constructor(
    private val wallPaperApi: WallPaperApi
): WallPaperRepository {
    override fun getHomeWallPaper(): Flow<PagingData<Data>> {
        return Pager(
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = {
                HomePagingSource(wallPaperApi)
            }
        ).flow
    }
}