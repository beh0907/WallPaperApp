package com.skymilk.wallpaperapp.store.presentation.screen.random

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.skymilk.wallpaperapp.store.presentation.common.BaseFragment
import com.skymilk.wallpaperapp.store.presentation.common.WallPaperAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RandomFragment : BaseFragment() {

    private val randomViewModel: RandomViewModel by viewModels()

    override var wallPaperAdapter: WallPaperAdapter = WallPaperAdapter()

    override fun initViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                randomViewModel.randomPapers.collectLatest {
                    wallPaperAdapter.submitData(it)
                }
            }
        }
    }

}