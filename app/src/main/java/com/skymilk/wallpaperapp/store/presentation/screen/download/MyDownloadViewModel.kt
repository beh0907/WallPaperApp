package com.skymilk.wallpaperapp.store.presentation.screen.download

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skymilk.wallpaperapp.store.presentation.util.ImageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MyDownloadViewModel @Inject constructor() : ViewModel() {
    private val _MyDownload_uiState = MutableStateFlow(MyDownloadUiState())
    val myDownloadUiState: StateFlow<MyDownloadUiState> = _MyDownload_uiState.asStateFlow()

    init {
        loadImages()
    }

    private fun loadImages() {
        viewModelScope.launch {
            _MyDownload_uiState.update { it.copy(isLoading = true) }
            try {
                val images = ImageUtil.getSavedImages()
                _MyDownload_uiState.update {
                    it.copy(
                        imageList = images,
                        isEmptyState = images.isEmpty(),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _MyDownload_uiState.update {
                    it.copy(
                        error = "이미지를 불러오는 데 실패했습니다: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun deleteImage(file: File) {
        viewModelScope.launch {
            _MyDownload_uiState.update { it.copy(isLoading = true) }
            try {
                if (ImageUtil.deleteImageFile(file.absolutePath)) {
                    loadImages() // 이미지 목록 새로고침
                } else {
                    _MyDownload_uiState.update {
                        it.copy(
                            error = "이미지 삭제에 실패했습니다.",
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _MyDownload_uiState.update {
                    it.copy(
                        error = "이미지 삭제 중 오류가 발생했습니다: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun clearError() {
        _MyDownload_uiState.update { it.copy(error = null) }
    }

}