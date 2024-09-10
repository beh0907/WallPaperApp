package com.skymilk.wallpaperapp.store.presentation.screen.search

import androidx.paging.PagingData
import com.skymilk.wallpaperapp.store.domain.model.Hit
import kotlinx.coroutines.flow.Flow

data class SearchUiState(
    val isLoading: Boolean = false,
    val isSearchHistoryVisible: Boolean = true, // 검색 이력 목록 표시 여부
    val isFirstSearchFocus: Boolean = true, // 첫 화면 포커싱 여부
    val searchQuery: String = "", // 검색어를 저장하는 변수
    val searchHistories: List<String> = emptyList(), // 검색어 이력 목록
    val searchWallPapers: Flow<PagingData<Hit>>? = null, // 검색 월페이퍼 목록
    val error: String? = null
)