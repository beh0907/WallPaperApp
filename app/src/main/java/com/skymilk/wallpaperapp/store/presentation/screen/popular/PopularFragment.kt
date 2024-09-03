package com.skymilk.wallpaperapp.store.presentation.screen.popular

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.skymilk.wallpaperapp.store.presentation.common.adapter.WallPaperAdapter
import com.skymilk.wallpaperapp.store.presentation.common.fragments.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PopularFragment : BaseFragment() {

    private val popularViewModel: PopularViewModel by viewModels()

    override var wallPaperAdapter: WallPaperAdapter = WallPaperAdapter()

    override fun setObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            popularViewModel.popularPapers.collectLatest {
                wallPaperAdapter.submitData(it)
            }
        }
    }
}