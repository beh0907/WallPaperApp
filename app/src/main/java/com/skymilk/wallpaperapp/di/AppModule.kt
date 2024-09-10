package com.skymilk.wallpaperapp.di

import com.skymilk.wallpaperapp.BuildConfig
import com.skymilk.wallpaperapp.store.data.remote.WallPaperApi
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
import com.skymilk.wallpaperapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Retrofit 객체 생성
    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        //디버그 모드일 경우에만 로그를 남긴다
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideWallPaperApi(
        okHttpClient: OkHttpClient
    ): WallPaperApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WallPaperApi::class.java)
    }


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