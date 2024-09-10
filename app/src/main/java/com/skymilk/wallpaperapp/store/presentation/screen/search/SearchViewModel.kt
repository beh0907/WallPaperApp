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

    init {
        viewModelScope.launch {
            //검색어 목록 가져오기
            searchHistoryUseCases.getSearchHistory().collect { histories ->
                _uiState.update { it.copy(searchHistories = histories) }
            }
        }
    }

    //검색 페이징
    fun searchWallPapers(searchQuery: String) {
        //검색어 저장
        _uiState.update { it.copy(searchQuery = searchQuery, isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                val searchWallPapers = wallPaperUseCases.getSearchWallPapers(searchQuery).cachedIn(viewModelScope)
                _uiState.update { it.copy(searchWallPapers = searchWallPapers, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
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