package com.skymilk.wallpaperapp.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment

object KeyboardUtil {
    //키보드 보여주기
    fun View.showKeyboard() {
        this.requestFocus()  // 뷰에 포커스를 설정하여 키보드 표시
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)  // 키보드를 표시합니다.
    }

    //키보드 숨기기
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)  // 키보드를 숨깁니다.
        this.clearFocus()  // 포커스를 제거합니다.
    }
}