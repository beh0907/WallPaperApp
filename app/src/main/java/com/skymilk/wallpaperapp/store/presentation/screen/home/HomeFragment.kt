package com.skymilk.wallpaperapp.store.presentation.screen.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.skymilk.wallpaperapp.store.presentation.common.adapter.WallPaperAdapter
import com.skymilk.wallpaperapp.store.presentation.common.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private val homeViewModel: HomeViewModel by viewModels()

    override var wallPaperAdapter: WallPaperAdapter = WallPaperAdapter()

    override fun setObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.wallPapers.collectLatest {
                wallPaperAdapter.submitData(it)
            }
        }
    }
}