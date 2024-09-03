package com.skymilk.wallpaperapp.store.presentation.screen.random

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.skymilk.wallpaperapp.store.presentation.common.adapter.WallPaperAdapter
import com.skymilk.wallpaperapp.store.presentation.common.fragments.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RandomFragment : BaseFragment() {

    private val randomViewModel: RandomViewModel by viewModels()

    override var wallPaperAdapter: WallPaperAdapter = WallPaperAdapter()

    override fun setObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            randomViewModel.randomPapers.collectLatest {
                wallPaperAdapter.submitData(it)
            }
        }
    }

}