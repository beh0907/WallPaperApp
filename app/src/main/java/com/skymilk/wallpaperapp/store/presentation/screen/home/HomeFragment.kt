package com.skymilk.wallpaperapp.store.presentation.screen.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.skymilk.wallpaperapp.databinding.FragmentHomeBinding
import com.skymilk.wallpaperapp.store.presentation.screen.main.adapter.WallPaperAdapter
import com.skymilk.wallpaperapp.store.presentation.screen.main.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {

    private val homeViewModel: HomeViewModel by viewModels()

    override var wallPaperAdapter: WallPaperAdapter = WallPaperAdapter()

    override fun initViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                homeViewModel.wallPapers.collectLatest {
                    wallPaperAdapter.submitData(it)
                }

            }
        }
    }

    override fun initRecyclerView() {
        wallPaperAdapter.onItemClick = {

        }

        binding.recyclerWallPaper.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = wallPaperAdapter
        }
    }
}