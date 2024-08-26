package com.skymilk.wallpaperapp.store.presentation.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.skymilk.wallpaperapp.store.domain.Repository.WallPaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: WallPaperRepository
) : ViewModel() {

    private val _searchState = MutableStateFlow(SearchState())
    var searchState = _searchState.asStateFlow()

    //검색어 업데이트 변경
    fun updateSearchQuery(searchQuery: String) {
        _searchState.update {
            it.copy(searchQuery = searchQuery)
        }
    }

    //검색 페이징
    fun searchWallPapers() {
        val wallPapers = repository.getSearchWallPaper(
            _searchState.value.searchQuery
        ).cachedIn(viewModelScope) // paging3 캐싱

        _searchState.update {
            it.copy(wallPapers = wallPapers)
        }
    }
}