package com.skymilk.wallpaperapp.store.domain.usecase

import androidx.paging.PagingData
import com.skymilk.wallpaperapp.store.domain.Repository.WallPaperRepository
import com.skymilk.wallpaperapp.store.domain.model.Hit
import kotlinx.coroutines.flow.Flow

class GetSearchWallPapers(
    private val wallPaperRepository: WallPaperRepository
) {

    operator fun invoke(searchQuery: String): Flow<PagingData<Hit>> {
        return wallPaperRepository.getSearchWallPapers(searchQuery)
    }
}