package com.skymilk.wallpaperapp.store.presentation.screen.edit.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skymilk.wallpaperapp.databinding.DialogStickerEmojiBottomSheetBinding
import com.skymilk.wallpaperapp.store.presentation.screen.edit.adapter.EmojiAdapter

class BottomSheetEmojiFragment : BottomSheetDialogFragment() {

    private lateinit var binding: DialogStickerEmojiBottomSheetBinding

    var onEmojiSelectedListener: ((String) -> Unit)? = null // 이모지 선택 리스너

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
        val emojiAdapter = EmojiAdapter()

        //어댑터 -> 바텀시트 -> 편집 프래그먼트
        emojiAdapter.onItemClick = { emoji ->
            onEmojiSelectedListener?.invoke(emoji)
            dismiss()
        }

        binding.recyclerEmoji.apply {
            layoutManager = GridLayoutManager(requireContext(), 5)
            adapter = emojiAdapter
            setHasFixedSize(true) //고정 사이즈이기 때문에 최적화
        }
    }
}