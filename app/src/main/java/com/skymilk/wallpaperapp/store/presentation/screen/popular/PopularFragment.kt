package com.skymilk.wallpaperapp.store.presentation.screen.popular

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.skymilk.wallpaperapp.store.presentation.common.WallPaperAdapter
import com.skymilk.wallpaperapp.store.presentation.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PopularFragment : BaseFragment() {

    private val popularViewModel: PopularViewModel by viewModels()

    override var wallPaperAdapter: WallPaperAdapter = WallPaperAdapter()

    override fun initViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                popularViewModel.popularPapers.collectLatest {
                    wallPaperAdapter.submitData(it)
                }
            }
        }
    }
}