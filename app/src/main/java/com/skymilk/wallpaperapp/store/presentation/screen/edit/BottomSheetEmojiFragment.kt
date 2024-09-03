package com.skymilk.wallpaperapp.store.presentation.screen.edit

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skymilk.wallpaperapp.databinding.DialogStickerEmojiBottomSheetBinding

class BottomSheetEmojiFragment : BottomSheetDialogFragment() {

    private lateinit var binding: DialogStickerEmojiBottomSheetBinding

    private val bottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN)
                dismiss()
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            TODO("Not yet implemented")
        }

    }


}