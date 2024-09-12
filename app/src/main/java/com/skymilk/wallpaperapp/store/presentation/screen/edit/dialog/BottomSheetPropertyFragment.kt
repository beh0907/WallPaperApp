package com.skymilk.wallpaperapp.store.presentation.screen.edit.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skymilk.wallpaperapp.databinding.DialogPropertiesBottomSheetBinding
import com.skymilk.wallpaperapp.store.presentation.screen.edit.adapter.ColorPickerAdapter

class BottomSheetPropertyFragment : BottomSheetDialogFragment(), OnSeekBarChangeListener {

    private lateinit var binding: DialogPropertiesBottomSheetBinding

    var onColorChangedListener: ((Int) -> Unit)? = null // 색상 선택 리스너
    var onOpacityChangedListener: ((Int) -> Unit)? = null // 투명도 선택 리스너
    var onSizeChangedListener: ((Int) -> Unit)? = null // 크기 선택 리스너

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogPropertiesBottomSheetBinding.inflate(inflater)

        initRecyclerViewColor()

        setEvent()

        return binding.root
    }

    private fun initRecyclerViewColor() {
        val colorAdapter = ColorPickerAdapter(requireContext())

        colorAdapter.onItemClick = { color ->
            onColorChangedListener?.invoke(color)
            dismissAllowingStateLoss()
        }

        binding.recyclerColor.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = colorAdapter
            setHasFixedSize(true) //고정 사이즈이기 때문에 최적화
        }
    }

    private fun setEvent() {
        binding.apply {
            seekSize.setOnSeekBarChangeListener(this@BottomSheetPropertyFragment)
            seekOpacity.setOnSeekBarChangeListener(this@BottomSheetPropertyFragment)
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar?.id) {
            binding.seekSize.id -> {
                onSizeChangedListener?.invoke(progress)
            }

            binding.seekOpacity.id -> {
                onOpacityChangedListener?.invoke(progress)
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }
}