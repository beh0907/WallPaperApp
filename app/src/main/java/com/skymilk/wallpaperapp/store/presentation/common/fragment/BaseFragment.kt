package com.skymilk.wallpaperapp.store.presentation.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.skymilk.wallpaperapp.databinding.FragmentHomeBinding
import com.skymilk.wallpaperapp.store.presentation.common.adapter.LoaderStateAdapter
import com.skymilk.wallpaperapp.store.presentation.common.adapter.WallPaperAdapter
import com.skymilk.wallpaperapp.store.presentation.screen.main.MainFragmentDirections
import com.skymilk.wallpaperapp.store.presentation.util.MessageUtil

abstract class BaseFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    abstract val wallPaperAdapter: WallPaperAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)

        initRecyclerView()

        setObserve()
        setEvent()

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
                //LoadState.NotLoading : 활성 로드 작업이 없고 오류가 없음
                //LoadState.Loading : 활성 로드 작업이 있음
                //LoadState.Error : 오류가 있음
                recyclerWallPaper.isVisible = loadState.source.refresh is LoadState.NotLoading
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                handleError(loadState)
            }
        }

        val headerAdapter = LoaderStateAdapter { wallPaperAdapter.retry() }
        val footerAdapter = LoaderStateAdapter { wallPaperAdapter.retry() }

        //헤더/풋터 span 사이즈 조정
        val gridLayoutManager = GridLayoutManager(context, 3)
        gridLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if ((position == wallPaperAdapter.itemCount) && footerAdapter.itemCount > 0) {
                    3
                } else if (wallPaperAdapter.itemCount == 0 && headerAdapter.itemCount > 0) {
                    3
                } else {
                    1
                }
            }
        }

        binding.recyclerWallPaper.apply {
            layoutManager = gridLayoutManager
            adapter = wallPaperAdapter.withLoadStateHeaderAndFooter(
                header = headerAdapter,
                footer = footerAdapter
            )
        }
    }

    private fun setEvent() {
        binding.apply {
            btnRetry.setOnClickListener {
                wallPaperAdapter.retry()
            }

            layoutSwipeRefresh.setOnRefreshListener {
                wallPaperAdapter.refresh()
                layoutSwipeRefresh.isRefreshing = false
            }
        }
    }

    private fun handleError(loadState: CombinedLoadStates) {
        val errorState = loadState.source.append as? LoadState.Error
            ?: loadState.source.prepend as? LoadState.Error

        errorState?.let {
            MessageUtil.showToast(requireContext(), "다시 시도해주세요")
        }
    }
}