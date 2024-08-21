package com.skymilk.wallpaperapp.store.data.repository

import com.skymilk.wallpaperapp.store.data.remote.WallPaperApi
import com.skymilk.wallpaperapp.store.domain.Repository.WallPaperRepository
import javax.inject.Inject


class WallPaperRepositoryImpl @Inject constructor(
    private val wallPaperApi: WallPaperApi
): WallPaperRepository {



}