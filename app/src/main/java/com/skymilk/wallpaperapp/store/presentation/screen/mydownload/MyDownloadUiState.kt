package com.skymilk.wallpaperapp.store.presentation.screen.mydownload

import java.io.File

data class MyDownloadUiState(
    val imageList: List<File> = emptyList(),
    val isEmptyState: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
)