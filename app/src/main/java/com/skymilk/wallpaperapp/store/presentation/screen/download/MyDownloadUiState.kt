package com.skymilk.wallpaperapp.store.presentation.screen.download

import java.io.File

data class MyDownloadUiState(
    val imageList: List<File> = emptyList(),
    val isEmptyState: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
)