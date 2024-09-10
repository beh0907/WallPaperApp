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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val wallPaperUseCases: WallPaperUseCases,
    private val searchHistoryUseCases: SearchHistoryUseCases
) : ViewModel() {

    // 검색 쿼리를 감지하는 StateFlow
    private val _searchQuery = MutableStateFlow("")

    // 검색어 이력 목록
    val searchHistories = searchHistoryUseCases.getSearchHistory()

    // 외부에서 접근 가능한 검색 결과 Flow
    val searchWallPapers: Flow<PagingData<Hit>> = _searchQuery
        .filter {
            it.isBlank().not()
        } // null인 경우 무시
        .flatMapLatest { query ->
            wallPaperUseCases.getSearchWallPapers(query)
        }
        .cachedIn(viewModelScope)

    //검색 페이징
    fun searchWallPapers(searchQuery: String) {
        //검색쿼리 적용 -> search Flow 업데이트
        _searchQuery.value = searchQuery
    }

    //검색어 저장
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
}