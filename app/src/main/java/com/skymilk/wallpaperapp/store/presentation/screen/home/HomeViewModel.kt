package com.skymilk.wallpaperapp.store.presentation.screen.home

import androidx.lifecycle.ViewModel
import com.skymilk.wallpaperapp.store.domain.Repository.WallPaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val wallPaperRepository: WallPaperRepository
):ViewModel() {
}