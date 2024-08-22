package com.skymilk.wallpaperapp.store.presentation.screen.popular

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.skymilk.wallpaperapp.databinding.FragmentPopularBinding
import com.skymilk.wallpaperapp.store.presentation.screen.main.adapter.WallPaperAdapter
import com.skymilk.wallpaperapp.store.presentation.screen.main.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularFragment : BaseFragment<FragmentPopularBinding>(
    FragmentPopularBinding::inflate
) {

    private val popularViewModel: PopularViewModel by viewModels()

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