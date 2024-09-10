package com.skymilk.wallpaperapp.store.presentation.common

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ConfirmDialog(
    private val context: Context,
    private val message: String,
    private val onConfirm: () -> Unit // 확인 버튼 클릭 시 호출될 콜백 함수
) {
    fun show() {
        val builder = MaterialAlertDialogBuilder(context)
        builder.setMessage(message)
            .setCancelable(true)
            .setPositiveButton("확인") { dialog, _ ->
                onConfirm.invoke() // 확인 버튼을 클릭하면 콜백 함수 호출
                dialog.dismiss() // 다이얼로그 닫기
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.cancel() // 다이얼로그 취소
            }

        val alert = builder.create()
        alert.show()
    }
}