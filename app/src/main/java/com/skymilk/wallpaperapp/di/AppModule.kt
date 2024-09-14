package com.skymilk.wallpaperapp.di

import android.content.Context
import com.ketch.DownloadConfig
import com.ketch.Ketch
import com.ketch.NotificationConfig
import com.skymilk.wallpaperapp.BuildConfig
import com.skymilk.wallpaperapp.R
import com.skymilk.wallpaperapp.store.data.remote.WallPaperApi
import com.skymilk.wallpaperapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
        OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
    } else {
        OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideWallPaperApi(
        okHttpClient: OkHttpClient
    ): WallPaperApi {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(WallPaperApi::class.java)
    }

    @Provides
    @Singleton
    fun provideKetchDownload(
        @ApplicationContext context: Context
    ): Ketch = Ketch.builder()
        .setDownloadConfig(
            //다운로드 시 타임아웃 설정
            DownloadConfig(
                connectTimeOutInMs = 15000L,
                readTimeOutInMs = 15000L
            )
        )
        .setNotificationConfig(
            //상단바 알림 설정
            NotificationConfig(
                enabled = true,
                smallIcon = R.mipmap.ic_launcher
            )
        )
        .build(context)
}