package com.skymilk.wallpaperapp.store.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.skymilk.wallpaperapp.store.domain.usecase.wallpaper.WallPaperUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val wallPaperUseCases: WallPaperUseCases
) : ViewModel() {

    val wallPapers = wallPaperUseCases.getHomeWallPapers().cachedIn(viewModelScope)

}