package com.skymilk.wallpaperapp.store.presentation.screen.edit.dialog

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skymilk.wallpaperapp.databinding.DialogStickerEmojiBottomSheetBinding
import com.skymilk.wallpaperapp.store.presentation.screen.edit.adapter.StickerAdapter

class BottomSheetStickerFragment : BottomSheetDialogFragment() {

    private lateinit var binding: DialogStickerEmojiBottomSheetBinding

    var onStickerSelectedListener: ((Bitmap) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogStickerEmojiBottomSheetBinding.inflate(inflater)

        initRecyclerViewEmoji()

        return binding.root
    }

    private fun initRecyclerViewEmoji() {
        val stickerAdapter = StickerAdapter()

        //어댑터 -> 바텀시트 -> 편집 프래그먼트
        stickerAdapter.onItemClick = { emoji ->
            onStickerSelectedListener?.invoke(emoji)
            dismiss()
        }

        binding.recyclerEmoji.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = stickerAdapter
            setHasFixedSize(true) //고정 사이즈이기 때문에 최적화
        }
    }
}