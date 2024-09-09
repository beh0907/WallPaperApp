package com.skymilk.wallpaperapp.store.presentation.screen.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.skymilk.wallpaperapp.store.domain.Repository.WallPaperRepository
import com.skymilk.wallpaperapp.store.domain.usecase.WallPaperUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PopularViewModel @Inject constructor(
    private val wallPaperUseCases: WallPaperUseCases
) : ViewModel() {

    val popularPapers = wallPaperUseCases.getPopularWallPapers().cachedIn(viewModelScope)

}