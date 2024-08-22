package com.skymilk.wallpaperapp.store.presentation.screen.popular

import androidx.lifecycle.ViewModel
import com.skymilk.wallpaperapp.store.domain.Repository.WallPaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PopularViewModel @Inject constructor(
    private val wallPaperRepository: WallPaperRepository
):ViewModel() {
}