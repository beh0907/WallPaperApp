package com.skymilk.wallpaperapp.store.presentation.screen.random

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.skymilk.wallpaperapp.store.domain.Repository.WallPaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RandomViewModel @Inject constructor(
    private val wallPaperRepository: WallPaperRepository
):ViewModel() {

    val randomPapers = wallPaperRepository.getRandomWallPaper().cachedIn(viewModelScope)
}