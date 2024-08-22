package com.skymilk.wallpaperapp.store.presentation.screen.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.skymilk.wallpaperapp.store.domain.Repository.WallPaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PopularViewModel @Inject constructor(
    private val wallPaperRepository: WallPaperRepository
) : ViewModel() {

    val popularPapers = wallPaperRepository.getPopularWallPaper().cachedIn(viewModelScope)

}