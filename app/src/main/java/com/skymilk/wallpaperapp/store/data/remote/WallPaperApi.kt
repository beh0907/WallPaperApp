package com.skymilk.wallpaperapp.store.data.remote

import com.skymilk.wallpaperapp.BuildConfig
import com.skymilk.wallpaperapp.store.domain.model.WallPaperPixaBay
import com.skymilk.wallpaperapp.utils.Constants
import com.skymilk.wallpaperapp.utils.Constants.PAGE_SIZE
import retrofit2.http.GET
import retrofit2.http.Query

interface WallPaperApi {

    // API로부터 최신 이미지 가져오기 (Pixabay에서는 최신 이미지를 특정해서 가져오는 엔드포인트가 없으므로, 기본 이미지 검색을 사용)
    @GET("api/")
    suspend fun getHomeWallPaper(
        @Query("page") page: Int,
        @Query("order") order: String = "latest",
        @Query("key") key: String = BuildConfig.PIXABAY_API_KEY,
        @Query("per_page") perPage: Int = PAGE_SIZE
    ): WallPaperPixaBay

    // API로부터 인기있는 이미지 가져오기
    @GET("api/")
    suspend fun getPopularWallPaper(
        @Query("page") page: Int,
        @Query("order") order: String = "popular",
        @Query("key") key: String = BuildConfig.PIXABAY_API_KEY,
        @Query("per_page") perPage: Int = PAGE_SIZE
    ): WallPaperPixaBay

    // API로부터 랜덤 이미지 가져오기 (Pixabay API에는 랜덤 이미지 엔드포인트가 없으므로 order를 "popular"로 설정하고 페이지를 무작위로 지정)
    @GET("api/")
    suspend fun getRandomWallPaper(
        @Query("page") page: Int,
        @Query("order") order: String = "popular",
        @Query("key") key: String = BuildConfig.PIXABAY_API_KEY,
        @Query("per_page") perPage: Int = PAGE_SIZE
    ): WallPaperPixaBay

    // API로부터 특정 카테고리의 이미지 가져오기
    @GET("api/")
    suspend fun getCategoryWallPaper(
        @Query("page") page: Int,
        @Query("category") category: String,
        @Query("key") key: String = BuildConfig.PIXABAY_API_KEY,
        @Query("per_page") perPage: Int = PAGE_SIZE
    ): WallPaperPixaBay

    // API로부터 키워드 검색
    @GET("api/")
    suspend fun getSearchWallPaper(
        @Query("page") page: Int,
        @Query("q") search: String,
        @Query("key") key: String = BuildConfig.PIXABAY_API_KEY,
        @Query("per_page") perPage: Int = PAGE_SIZE
    ): WallPaperPixaBay
}