package com.skymilk.wallpaperapp.store.domain.usecase.wallpaper

import androidx.paging.PagingData
import com.skymilk.wallpaperapp.store.domain.repository.WallPaperRepository
import com.skymilk.wallpaperapp.store.domain.model.Hit
import kotlinx.coroutines.flow.Flow

class GetHomeWallPapers (
    private val wallPaperRepository: WallPaperRepository
) {

    operator fun invoke(): Flow<PagingData<Hit>> {
        return wallPaperRepository.getHomeWallPapers()
    }
}