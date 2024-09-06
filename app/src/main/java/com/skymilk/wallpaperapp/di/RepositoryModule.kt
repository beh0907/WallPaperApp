package com.skymilk.wallpaperapp.di

import com.skymilk.wallpaperapp.store.data.repository.WallPaperRepositoryImpl
import com.skymilk.wallpaperapp.store.domain.Repository.WallPaperRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    //@Binds는 반환 타입이 interface
    //파라미터는 interface의 구현체 클래스로 활용
    @Binds
    @Singleton
    abstract fun bindWallPaperRepository(impl: WallPaperRepositoryImpl): WallPaperRepository
}