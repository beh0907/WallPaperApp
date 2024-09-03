package com.skymilk.wallpaperapp.store.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.skymilk.wallpaperapp.store.data.remote.WallPaperApi
import com.skymilk.wallpaperapp.store.data.remote.paging.CategoryPagingSource
import com.skymilk.wallpaperapp.store.data.remote.paging.HomePagingSource
import com.skymilk.wallpaperapp.store.data.remote.paging.PopularPagingSource
import com.skymilk.wallpaperapp.store.data.remote.paging.RandomPagingSource
import com.skymilk.wallpaperapp.store.data.remote.paging.SearchPagingSource
import com.skymilk.wallpaperapp.store.domain.Repository.WallPaperRepository
import com.skymilk.wallpaperapp.store.domain.model.Hit
import com.skymilk.wallpaperapp.util.Constants.PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class WallPaperRepositoryImpl @Inject constructor(
    private val wallPaperApi: WallPaperApi
): WallPaperRepository {
    override fun getHomeWallPaper(): Flow<PagingData<Hit>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = {
                HomePagingSource(wallPaperApi)
            }
        ).flow
    }

    override fun getPopularWallPaper(): Flow<PagingData<Hit>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = {
                PopularPagingSource(wallPaperApi)
            }
        ).flow
    }

    override fun getRandomWallPaper(): Flow<PagingData<Hit>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = {
                RandomPagingSource(wallPaperApi)
            }
        ).flow
    }

    override fun getCategoryWallPaper(category: String): Flow<PagingData<Hit>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = {
                CategoryPagingSource(wallPaperApi, category)
            }
        ).flow
    }

    override fun getSearchWallPaper(searchQuery: String): Flow<PagingData<Hit>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = {
                SearchPagingSource(wallPaperApi, searchQuery)
            }
        ).flow
    }
}