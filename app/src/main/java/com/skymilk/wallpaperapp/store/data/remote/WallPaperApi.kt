package com.skymilk.wallpaperapp.store.data.remote

import com.skymilk.wallpaperapp.store.domain.model.WallPaper
import retrofit2.http.GET
import retrofit2.http.Query

interface WallPaperApi {

    //API로부터 랜덤 이미지 가져오기
    @GET("latest")
    suspend fun getHomeWallPaper(@Query("page") page: Int): WallPaper

    //API로부터 인기있는 이미지 가져오기
    @GET("Popular")
    suspend fun getPopularWallPaper(@Query("page") page: Int): WallPaper

    //API로부터 마지막 이미지 가져오기
    @GET("Random")
    suspend fun getRandomWallPaper(@Query("page") page: Int): WallPaper

    //API로부터 특정 카테고리의 이미지 가져오기
    @GET("category")
    suspend fun getCategoryWallPaper(
        @Query("page") page: Int,
        @Query("category") category: String
    ): WallPaper

    //API로부터 키워드 검색
    @GET("search")
    suspend fun getSearchWallPaper(
        @Query("page") page: Int,
        @Query("search") search: String
    ): WallPaper
}