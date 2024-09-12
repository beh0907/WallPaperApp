package com.skymilk.wallpaperapp.store.presentation.screen.edit.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.skymilk.wallpaperapp.databinding.DialogEditAddTextBinding
import com.skymilk.wallpaperapp.store.presentation.screen.edit.adapter.ColorPickerAdapter
import com.skymilk.wallpaperapp.store.presentation.util.KeyboardUtil.hideKeyboard
import com.skymilk.wallpaperapp.store.presentation.util.KeyboardUtil.showKeyboard
import com.skymilk.wallpaperapp.store.presentation.util.MessageUtil

class DialogTextEditorFragment : DialogFragment() {

    private lateinit var binding: DialogEditAddTextBinding

    var onTextStateChangeListener: ((String, Int) -> Unit)? = null // 텍스트와 색상 상태 리스너

    companion object {
        private const val ARG_INPUT_TEXT = "edit_input_text"
        private const val ARG_INPUT_COLOR = "edit_input_color"

        fun newInstance(inputText: String, inputColor: Int): DialogTextEditorFragment {
            val fragment = DialogTextEditorFragment()
            val args = Bundle().apply {
                putString(ARG_INPUT_TEXT, inputText)
                putInt(ARG_INPUT_COLOR, inputColor)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onResume() {
        super.onResume()
        // 전체화면 다이얼로그 설정
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogEditAddTextBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerViewColor()
        initEditText()

        setEvent()
    }

    private fun initRecyclerViewColor() {
        val colorAdapter = ColorPickerAdapter(requireContext())

        colorAdapter.onItemClick = { color ->
            binding.editAdd.setTextColor(color)
        }

        binding.recyclerColor.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = colorAdapter
            setHasFixedSize(true) //고정 사이즈이기 때문에 최적화
        }
    }

    private fun initEditText() {
        binding.editAdd.apply {
            setText(requireArguments().getString(ARG_INPUT_TEXT, ""))
            setTextColor(requireArguments().getInt(ARG_INPUT_COLOR, Color.WHITE))

            // 뷰가 레이아웃에 추가된 후 포커스 요청 및 키보드 표시
            postDelayed({
                showKeyboard()
            }, 200)
        }
    }

    private fun setEvent() {
        binding.apply {
            btnClose.setOnClickListener {
                findNavController().navigateUp()
            }

            btnDone.setOnClickListener {
                val inputText = editAdd.text.toString()
                if (inputText.isEmpty()) {
                    MessageUtil.showToast(requireContext(), "텍스트를 입력해주세요")
                    return@setOnClickListener
                }

                // 수정된 텍스트와 선택된 색상 전달
                onTextStateChangeListener?.invoke(inputText, editAdd.currentTextColor)

                //키보드 숨기기
                binding.editAdd.hideKeyboard()

                //시트 숨기기
                dismissAllowingStateLoss()
            }

        }
    }
}