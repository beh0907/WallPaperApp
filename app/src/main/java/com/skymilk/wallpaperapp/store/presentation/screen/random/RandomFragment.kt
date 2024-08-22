package com.skymilk.wallpaperapp.store.presentation.screen.random

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.skymilk.wallpaperapp.databinding.FragmentRandomBinding
import com.skymilk.wallpaperapp.store.presentation.screen.main.adapter.WallPaperAdapter
import com.skymilk.wallpaperapp.store.presentation.screen.main.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RandomFragment : BaseFragment<FragmentRandomBinding>(
    FragmentRandomBinding::inflate
) {

    private val randomBinding: RandomViewModel by viewModels()

    override var wallPaperAdapter: WallPaperAdapter = WallPaperAdapter()

    override fun initViewModel() {
        TODO("Not yet implemented")
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