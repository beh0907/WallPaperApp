package com.skymilk.wallpaperapp.store.domain.usecase

data class WallPaperUseCases(
    val getHomeWallPapers: GetHomeWallPapers,
    val getPopularWallPapers: GetPopularWallPapers,
    val getRandomWallPapers: GetRandomWallPapers,
    val getCategoryWallPapers: GetCategoryWallPapers,
    val getSearchWallPapers: GetSearchWallPapers
)