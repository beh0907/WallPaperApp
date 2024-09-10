package com.skymilk.wallpaperapp.store.domain.usecase.wallpaper

import androidx.paging.PagingData
import com.skymilk.wallpaperapp.store.domain.repository.WallPaperRepository
import com.skymilk.wallpaperapp.store.domain.model.Hit
import kotlinx.coroutines.flow.Flow

class GetCategoryWallPapers (
    private val wallPaperRepository: WallPaperRepository
) {

    operator fun invoke(category: String): Flow<PagingData<Hit>> {
        return wallPaperRepository.getCategoryWallPapers(category)
    }
}