package com.skymilk.wallpaperapp.store.presentation.screen.search

data class SearchUiState(
    val searchQuery: String = "", // 검색어를 저장하는 변수
    val isSearchHistoryVisible: Boolean = true, // 검색 이력 목록 표시 여부
    val isFirstSearchFocus: Boolean = true // 첫 화면 포커싱 여부
)