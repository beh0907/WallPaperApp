package com.skymilk.wallpaperapp.store.presentation.screen.random

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.skymilk.wallpaperapp.databinding.FragmentRandomBinding
import com.skymilk.wallpaperapp.store.presentation.common.WallPaperAdapter
import com.skymilk.wallpaperapp.store.presentation.common.BaseFragment
import com.skymilk.wallpaperapp.utils.Constants
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