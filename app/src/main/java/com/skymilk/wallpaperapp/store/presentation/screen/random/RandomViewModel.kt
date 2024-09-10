package com.skymilk.wallpaperapp.store.presentation.screen.random

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.skymilk.wallpaperapp.store.domain.usecase.wallpaper.WallPaperUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RandomViewModel @Inject constructor(
    private val wallPaperUseCases: WallPaperUseCases
) : ViewModel() {

    val randomPapers = wallPaperUseCases.getRandomWallPapers().cachedIn(viewModelScope)
}