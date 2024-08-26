package com.skymilk.wallpaperapp.store.presentation.screen.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.skymilk.wallpaperapp.databinding.FragmentSearchBinding
import com.skymilk.wallpaperapp.store.presentation.common.adapter.LoaderStateAdapter
import com.skymilk.wallpaperapp.store.presentation.common.adapter.WallPaperAdapter
import com.skymilk.wallpaperapp.store.presentation.screen.main.MainFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by viewModels()

    private val wallPaperAdapter: WallPaperAdapter = WallPaperAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)

        initRecyclerView()

        setObserve()
        setEvent()

        return binding.root
    }

    private fun setObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            searchViewModel.searchState.value.wallPapers?.collectLatest {
                wallPaperAdapter.submitData(it)
            }
        }
    }

    private fun initRecyclerView() {
        //이미지 아이템 클릭 이벤트
        wallPaperAdapter.onItemClick = { data ->
            val imageData = arrayOf(data.fullImageUrl.toString(), data.blurHash.toString())

            //이미지 URL 정보와 함꼐 다운로드 화면으로 이동
            findNavController()
                .navigate(
                    MainFragmentDirections.actionMainFragmentToDownloadFragment(
                        imageData
                    )
                )
        }

        //이미지 로드 리스너
        wallPaperAdapter.addLoadStateListener { loadState ->
            binding.apply {
                recyclerWallPaper.isVisible = loadState.source.refresh is LoadState.NotLoading
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                handleError(loadState)
            }
        }

        binding.recyclerWallPaper.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = wallPaperAdapter.withLoadStateHeaderAndFooter(
                header = LoaderStateAdapter { wallPaperAdapter.retry() },
                footer = LoaderStateAdapter { wallPaperAdapter.retry() }
            )
        }
    }

    private fun setEvent() {
        binding.txtSearch.onActionViewExpanded()

        binding.apply {
            txtSearch.setOnQueryTextListener(object : OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {

                    Log.d("ddd", p0.toString())
                    searchViewModel.searchWallPapers()
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    searchViewModel.updateSearchQuery(p0.toString())
                    return true
                }
            })
        }
    }

    private fun handleError(loadState: CombinedLoadStates) {
        val errorState = loadState.source.append as? LoadState.Error
            ?: loadState.source.prepend as? LoadState.Error

        errorState?.let {
            Toast.makeText(requireContext(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
        }
    }
}