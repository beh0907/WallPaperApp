package com.skymilk.wallpaperapp.store.presentation.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.skymilk.wallpaperapp.databinding.FragmentHomeBinding
import com.skymilk.wallpaperapp.store.presentation.common.adapter.LoaderStateAdapter
import com.skymilk.wallpaperapp.store.presentation.common.adapter.WallPaperAdapter
import com.skymilk.wallpaperapp.store.presentation.screen.main.MainFragmentDirections

abstract class BaseFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    abstract var wallPaperAdapter: WallPaperAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)

        initRecyclerView()

        setObserve()
        setClick()

        return binding.root
    }

    abstract fun setObserve()

    private fun initRecyclerView() {
        //이미지 아이템 클릭 이벤트
        wallPaperAdapter.onItemClick = { hit ->
            //이미지 URL 정보와 함꼐 다운로드 화면으로 이동
            findNavController()
                .navigate(
                    MainFragmentDirections.actionMainFragmentToDownloadFragment(
                        hit.largeImageURL
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

    private fun setClick() {
        binding.apply {
            btnRetry.setOnClickListener {
                wallPaperAdapter.retry()
            }
        }
    }

    private fun handleError(loadState:CombinedLoadStates) {
        val errorState = loadState.source.append as? LoadState.Error
            ?: loadState.source.prepend as? LoadState.Error

        errorState?.let {
            Toast.makeText(requireContext(), "다시 시도해주세요",Toast.LENGTH_SHORT).show()
        }
    }
}