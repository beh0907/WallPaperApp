package com.skymilk.wallpaperapp.store.presentation.screen.search

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
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
import androidx.recyclerview.widget.LinearLayoutManager
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
    private val searchHistoryAdapter: SearchHistoryAdapter = SearchHistoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSearchView()
        initRecyclerViewSearchHistory()
        initRecyclerViewWallPaper()

        setObserve()
        setEvent()
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

            // 서치뷰에 포커싱될 때 검색 이력과 키보드 표시
            setOnQueryTextFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    searchEditText.showKeyboard()
                    searchViewModel.setSearchHistoryVisible(true)
                }
            }

            //첫 화면 초기화 시에만 포커스 설정
            if (searchViewModel.uiState.value.isFirstSearchFocus) {
                searchEditText.requestFocus()
                searchViewModel.setFirstFocus(false)
            }
        }
    }

    private fun setObserve() {
        //검색 월페이퍼 페이징 데이터 수집
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.searchWallPapers.collectLatest {
                    // 페이징 데이터 목록 어댑터 적용
                    wallPaperAdapter.submitData(it)
                }
            }
        }

        //검색 이력 목록 데이터 수집
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.searchHistories.collectLatest {
                    // 검색 이력 목록 어댑터 적용
                    searchHistoryAdapter.differ.submitList(it)
                }
            }
        }

        //UI 상태 변화 수집
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.uiState.collectLatest {
                    // UI 설정 반영

                    //검색 이력 목록 가시 설정
                    binding.recyclerSearchHistory.isVisible = it.isSearchHistoryVisible

                    //보여질때는 항상 최상단에 위치하여 최신 이력이 보이도록 설정
                    if (it.isSearchHistoryVisible) {
                        binding.recyclerSearchHistory.scrollToPosition(0)
                    }
                }
            }
        }
    }

    private fun initRecyclerViewSearchHistory() {
        searchHistoryAdapter.onItemClickSearch = { history ->
            //선택한 검색 이력 텍스트로 검색 시도
            searchViewModel.searchWallPapers(history)

            //searchView 텍스트 적용 및 검색
            binding.txtSearch.setQuery(history, true)

            //검색 이력 목록 숨기기
            searchViewModel.setSearchHistoryVisible(false)
        }

        searchHistoryAdapter.onItemClickDelete = { history ->
            //검색어 삭제
            searchViewModel.deleteSearchHistory(history)
        }

        binding.recyclerSearchHistory.apply {
            adapter = searchHistoryAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initRecyclerViewWallPaper() {
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
        //검색 뷰 이벤트
        binding.txtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            //검색어 입력 후 엔터를 눌렀을 때
            override fun onQueryTextSubmit(p0: String?): Boolean {

                if (p0.isNullOrEmpty()) {
                    MessageUtil.showToast(requireContext(), "검색어를 입력해주세요.")
                    return true
                }

                //검색 시도
                searchViewModel.searchWallPapers(p0)

                //검색어 저장
                searchViewModel.saveSearchHistory(p0)

                //검색 실행 시 검색 기록 숨기기
                searchViewModel.setSearchHistoryVisible(false)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })

        //프래그먼트에서 뒤로가기 버튼 콜백 생성
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, // 생명주기를 관리해 종료될 때 자동으로 콜백 해제 처리
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // 백 버튼을 눌렀을 때 수행할 작업
                    // 예: 이전 화면으로 이동하지 않고 다른 작업을 수행하고 싶을 때
                    // requireActivity().onBackPressedDispatcher.onBackPressed()을 호출하면 원래 동작(뒤로 가기)이 실행됨.
                    if (binding.recyclerSearchHistory.isVisible && binding.txtSearch.query.isNotEmpty()) {
                        searchViewModel.setSearchHistoryVisible(false)
                        return
                    }

                    findNavController().navigateUp()
                }
            })
    }


}