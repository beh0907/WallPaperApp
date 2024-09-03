package com.skymilk.wallpaperapp.store.presentation.screen.edit

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageContract
import com.skymilk.wallpaperapp.R
import com.skymilk.wallpaperapp.databinding.FragmentEditImageBinding
import com.skymilk.wallpaperapp.store.presentation.screen.edit.adapter.FilterAdapter
import com.skymilk.wallpaperapp.store.presentation.screen.edit.adapter.ToolAdapter
import com.skymilk.wallpaperapp.util.ImageUtil
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.SaveFileResult
import kotlinx.coroutines.launch

class EditImageFragment : Fragment() {

    private lateinit var binding: FragmentEditImageBinding

    private val args by navArgs<EditImageFragmentArgs>()

    private lateinit var photoEditor: PhotoEditor

    private lateinit var toolAdapter: ToolAdapter
    private lateinit var filterAdapter: FilterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditImageBinding.inflate(layoutInflater, container, false)

        initRecyclerViewTool()
        initRecyclerViewFilter()

        loadImage(args.imagePath)
        setClick()

        return binding.root
    }

    //필터 목록 초기화
    private fun initRecyclerViewFilter() {
        filterAdapter = FilterAdapter()

        filterAdapter.onItemClick = { photoFilter ->

        }

        binding.recyclerFilters.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = filterAdapter
        }
    }

    //편집툴 목록 초기화
    private fun initRecyclerViewTool() {
        toolAdapter = ToolAdapter()

        toolAdapter.onItemClick = { toolType ->

        }

        binding.recyclerTools.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = toolAdapter
        }
    }

    //선택한 이미지 불러오기
    private fun loadImage(imagePath: String) {
        //선택한 이미지 적용
        Glide.with(this)
            .load(imagePath)
            .placeholder(ImageUtil.getShimmerDrawable())
            .into(binding.photoEditorView.source)

        val textFont = ResourcesCompat.getFont(requireContext(), R.font.bm_hanna_pro)
        val emojiTypeFace = resources.getFont(R.font.emojione_android)


        photoEditor = PhotoEditor.Builder(requireContext(), binding.photoEditorView)
            .setPinchTextScalable(true) // set flag to make text scalable when pinch
            .setDefaultTextTypeface(textFont)
            .setDefaultEmojiTypeface(emojiTypeFace)
            .build() // build photo editor sdk

//        cropImage.launch(
//            CropImageContractOptions(
//                uri = ImageUtil.getImageUri(requireContext(), imagePath),
//                cropImageOptions = CropImageOptions(
//                    guidelines = CropImageView.Guidelines.ON,
//                    outputCompressFormat = Bitmap.CompressFormat.JPEG
//                )
//            )
//        )
    }

    //버튼 터치 이벤트 초기화
    private fun setClick() {
        binding.apply {
            btnClose.setOnClickListener {
                findNavController().popBackStack()
            }

            btnUndo.setOnClickListener {
                photoEditor.undo()
            }

            btnRedo.setOnClickListener {
                photoEditor.redo()
            }

            btnDone.setOnClickListener {
                saveImage()
            }
        }
    }

    //편집한 이미지 저장
    @SuppressLint("MissingPermission")
    private fun saveImage() {
        viewLifecycleOwner.lifecycleScope.launch {
            val result = photoEditor.saveAsFile(args.imagePath)
            if (result is SaveFileResult.Success) {
                Toast.makeText(requireContext(), "이미지가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "이미지 저장에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //이미지 자르기
    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // Use the cropped image URI.
            val croppedImageUri = result.uriContent
            val croppedImageFilePath = result.getUriFilePath(requireContext()) // optional usage
            // Process the cropped image URI as needed.

            //선택한 이미지 적용
            Glide.with(this)
                .load(croppedImageUri)
                .placeholder(ImageUtil.getShimmerDrawable())
                .fitCenter()
                .into(binding.photoEditorView.source)
        } else {
            val exception = result.error
        }
    }
}