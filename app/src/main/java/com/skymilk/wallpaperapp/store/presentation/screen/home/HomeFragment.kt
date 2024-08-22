package com.skymilk.wallpaperapp.store.presentation.screen.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.skymilk.wallpaperapp.store.presentation.common.BaseFragment
import com.skymilk.wallpaperapp.store.presentation.common.WallPaperAdapter
import com.skymilk.wallpaperapp.store.presentation.screen.main.MainFragmentDirections
import com.skymilk.wallpaperapp.utils.ApiListCategory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private val homeViewModel: HomeViewModel by viewModels()

    override var wallPaperAdapter: WallPaperAdapter = WallPaperAdapter()


    //임시 테스트
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageData = arrayOf(ApiListCategory.list[8].imageUrl, ApiListCategory.list[8].imageUrl)

        findNavController()
            .navigate(
                MainFragmentDirections.actionMainFragmentToDownloadFragment(
                    imageData
                )
            )
    }

    override fun initViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.wallPapers.collectLatest {
                    wallPaperAdapter.submitData(it)
                }
            }
        }
    }
}