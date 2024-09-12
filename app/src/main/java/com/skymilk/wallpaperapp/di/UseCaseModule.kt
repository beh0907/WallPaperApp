package com.skymilk.wallpaperapp.di

import com.skymilk.wallpaperapp.store.domain.repository.SearchHistoryRepository
import com.skymilk.wallpaperapp.store.domain.repository.WallPaperRepository
import com.skymilk.wallpaperapp.store.domain.usecase.history.DeleteSearchHistory
import com.skymilk.wallpaperapp.store.domain.usecase.history.GetSearchHistory
import com.skymilk.wallpaperapp.store.domain.usecase.history.SaveSearchHistory
import com.skymilk.wallpaperapp.store.domain.usecase.history.SearchHistoryUseCases
import com.skymilk.wallpaperapp.store.domain.usecase.wallpaper.GetCategoryWallPapers
import com.skymilk.wallpaperapp.store.domain.usecase.wallpaper.GetHomeWallPapers
import com.skymilk.wallpaperapp.store.domain.usecase.wallpaper.GetPopularWallPapers
import com.skymilk.wallpaperapp.store.domain.usecase.wallpaper.GetRandomWallPapers
import com.skymilk.wallpaperapp.store.domain.usecase.wallpaper.GetSearchWallPapers
import com.skymilk.wallpaperapp.store.domain.usecase.wallpaper.WallPaperUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideWallPaperUseCases(
        wallPaperRepository: WallPaperRepository
    ) = WallPaperUseCases(
        GetHomeWallPapers(wallPaperRepository),
        GetPopularWallPapers(wallPaperRepository),
        GetRandomWallPapers(wallPaperRepository),
        GetCategoryWallPapers(wallPaperRepository),
        GetSearchWallPapers(wallPaperRepository)
    )


    @Provides
    @Singleton
    fun provideSearchHistoryUseCases(
        searchHistoryRepository: SearchHistoryRepository
    ) = SearchHistoryUseCases(
        GetSearchHistory(searchHistoryRepository),
        SaveSearchHistory(searchHistoryRepository),
        DeleteSearchHistory(searchHistoryRepository)
    )
}