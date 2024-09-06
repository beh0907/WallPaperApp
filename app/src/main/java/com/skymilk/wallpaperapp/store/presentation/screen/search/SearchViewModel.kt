package com.skymilk.wallpaperapp.store.presentation.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.skymilk.wallpaperapp.store.domain.Repository.WallPaperRepository
import com.skymilk.wallpaperapp.store.domain.model.Hit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: WallPaperRepository
) : ViewModel() {

    // 검색 쿼리를 감지하는 StateFlow
    private val _searchQuery = MutableStateFlow("")

    // 외부에서 접근 가능한 검색 결과 Flow
    val searchWallPapers: Flow<PagingData<Hit>> = _searchQuery
        .filter {
            it.isBlank().not()
        } // null인 경우 무시
        .flatMapLatest { query ->
            repository.getSearchWallPaper(query)
        }
        .cachedIn(viewModelScope)

    //검색 페이징
    fun searchWallPapers(searchQuery: String) {
        _searchQuery.value = searchQuery
    }
}