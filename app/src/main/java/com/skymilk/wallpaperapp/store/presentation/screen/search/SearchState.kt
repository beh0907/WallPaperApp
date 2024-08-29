package com.skymilk.wallpaperapp.store.presentation.screen.search

import androidx.paging.PagingData
import com.skymilk.wallpaperapp.store.domain.model.Hit
import kotlinx.coroutines.flow.Flow

data class SearchState(
    val searchQuery: String = "",
    val wallPapers: Flow<PagingData<Hit>>? = null
)