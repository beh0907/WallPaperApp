package com.skymilk.wallpaperapp.store.presentation.screen.category

import androidx.lifecycle.ViewModel
import com.skymilk.wallpaperapp.store.domain.Repository.WallPaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val wallPaperRepository: WallPaperRepository
):ViewModel() {
}