package com.skymilk.wallpaperapp.store.presentation.common

import android.content.Context
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.skymilk.wallpaperapp.store.presentation.util.MessageUtil

object LoadStateHandleError {

    fun Context.handleError(loadState: CombinedLoadStates) {
        val errorState = loadState.source.append as? LoadState.Error
            ?: loadState.source.prepend as? LoadState.Error

        errorState?.let {
            MessageUtil.showToast(this, "다시 시도해주세요")
        }
    }

}