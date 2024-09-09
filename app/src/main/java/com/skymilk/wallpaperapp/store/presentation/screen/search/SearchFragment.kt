package com.skymilk.wallpaperapp.store.presentation.screen.search

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.skymilk.wallpaperapp.R
import com.skymilk.wallpaperapp.databinding.FragmentSearchBinding
import com.skymilk.wallpaperapp.store.presentation.common.LoadStateHandleError.handleError
import com.skymilk.wallpaperapp.store.presentation.common.adapter.LoaderStateAdapter
import com.skymilk.wallpaperapp.store.presentation.common.adapter.WallPaperAdapter
import com.skymilk.wallpaperapp.store.presentation.util.KeyboardUtil.showKeyboard
import com.skymilk.wallpaperapp.store.presentation.util.MessageUtil
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
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)

        initRecyclerView()
        initSearchView()

        setObserve()
        setEvent()

        return binding.root
    }

    private fun initSearchView() {
        binding.txtSearch.apply {
            //텍스트 색상 및 폰트 설정
            val searchEditText: EditText = findViewById(androidx.appcompat.R.id.search_src_text)
            searchEditText.setTextColor(Color.WHITE)
            searchEditText.setHintTextColor(Color.WHITE)
            val typeface =
                ResourcesCompat.getFont(requireContext(), R.font.bm_hanna_pro)
            searchEditText.typeface = typeface

            //검색 버튼 색상 설정
            val searchIcon: ImageView = findViewById(androidx.appcompat.R.id.search_mag_icon)
            searchIcon.setColorFilter(Color.WHITE)

            //초기화 버튼 색상 설정
            val closeIcon: ImageView = findViewById(androidx.appcompat.R.id.search_close_btn)
            closeIcon.setColorFilter(Color.WHITE)

            // 기본 확장 상태 설정 및 키보드 표시
            setOnQueryTextFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    searchEditText.showKeyboard()
                }
            }
            onActionViewExpanded()  // 검색 뷰 확장
        }
    }

    private fun setObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.searchWallPapers.collectLatest {
                    wallPaperAdapter.submitData(it)
                }
            }
        }
    }

    private fun initRecyclerView() {
        //이미지 아이템 클릭 이벤트
        wallPaperAdapter.onItemClick = { hit ->
            //이미지 URL 정보와 함꼐 다운로드 화면으로 이동
            findNavController()
                .navigate(
                    SearchFragmentDirections.actionSearchFragmentToDownloadFragment(
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

                requireContext().handleError(loadState)
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
            txtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {

                    if (p0.isNullOrEmpty()) {
                        MessageUtil.showToast(requireContext(), "검색어를 입력해주세요.")
                        return true
                    }

                    searchViewModel.searchWallPapers(p0)
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return true
                }
            })
        }
    }
}