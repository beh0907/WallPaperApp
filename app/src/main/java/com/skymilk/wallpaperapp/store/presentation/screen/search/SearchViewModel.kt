package com.skymilk.wallpaperapp.store.presentation.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.skymilk.wallpaperapp.store.domain.model.Hit
import com.skymilk.wallpaperapp.store.domain.usecase.history.SearchHistoryUseCases
import com.skymilk.wallpaperapp.store.domain.usecase.wallpaper.WallPaperUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val wallPaperUseCases: WallPaperUseCases,
    private val searchHistoryUseCases: SearchHistoryUseCases
) : ViewModel() {

    //UI 상태 정보
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    // 검색어를 저장하는 변수
    private val _searchQuery = MutableStateFlow("")
    var searchQuery = _searchQuery.asStateFlow()

    // 검색어 이력 목록
    val searchHistories = searchHistoryUseCases.getSearchHistory()

    // 외부에서 접근 가능한 검색 결과 Flow
    val searchWallPapers: Flow<PagingData<Hit>> = searchQuery
        .filter {
            it.isNotBlank()
        } // null인 경우 무시
        .flatMapLatest {
            wallPaperUseCases.getSearchWallPapers(it)
        }
        .cachedIn(viewModelScope)

    //검색어 저장 -> 검색 시도
    fun searchWallPapers(searchQuery: String) {
        //검색어 저장
        _searchQuery.update {
            searchQuery
        }
    }

    //검색어 이력 저장
    fun saveSearchHistory(searchQuery: String) {
        viewModelScope.launch {
            searchHistoryUseCases.saveSearchHistory(searchQuery)
        }
    }

    //검색어 삭제
    fun deleteSearchHistory(searchQuery: String) {
        viewModelScope.launch {
            searchHistoryUseCases.deleteSearchHistory(searchQuery)
        }
    }

    //검색 이력 목록 표시 여부 저장
    fun setSearchHistoryVisibility(isVisible: Boolean) {
        //검색어 저장
        _uiState.update {
            it.copy(isSearchHistoryVisible = isVisible)
        }
    }

    //첫 화면 포커싱 여부 처리
    fun setFirstSearchFocus(isFirstSearchFocus: Boolean) {
        _uiState.update {
            it.copy(isFirstSearchFocus = isFirstSearchFocus)
        }
    }
}