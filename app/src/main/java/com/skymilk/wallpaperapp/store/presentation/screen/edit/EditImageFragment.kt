package com.skymilk.wallpaperapp.store.presentation.screen.edit

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.skymilk.wallpaperapp.R
import com.skymilk.wallpaperapp.databinding.FragmentEditImageBinding
import com.skymilk.wallpaperapp.store.presentation.common.ConfirmDialog
import com.skymilk.wallpaperapp.store.presentation.screen.edit.adapter.FilterAdapter
import com.skymilk.wallpaperapp.store.presentation.screen.edit.adapter.ToolAdapter
import com.skymilk.wallpaperapp.store.presentation.screen.edit.dialog.BottomSheetEmojiFragment
import com.skymilk.wallpaperapp.store.presentation.screen.edit.dialog.BottomSheetPropertyFragment
import com.skymilk.wallpaperapp.store.presentation.screen.edit.dialog.BottomSheetStickerFragment
import com.skymilk.wallpaperapp.store.presentation.screen.edit.dialog.DialogTextEditorFragment
import com.skymilk.wallpaperapp.store.presentation.util.ImageUtil
import com.skymilk.wallpaperapp.store.presentation.util.MessageUtil
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoFilter
import ja.burhanrashid52.photoeditor.SaveFileResult
import ja.burhanrashid52.photoeditor.SaveSettings
import ja.burhanrashid52.photoeditor.ViewType
import ja.burhanrashid52.photoeditor.shape.ShapeBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditImageFragment : Fragment() {

    private lateinit var binding: FragmentEditImageBinding

    private val args by navArgs<EditImageFragmentArgs>()

    private lateinit var photoEditor: PhotoEditor

    private lateinit var toolAdapter: ToolAdapter
    private lateinit var filterAdapter: FilterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditImageBinding.inflate(layoutInflater, container, false)

        initRecyclerViewTool()
        initRecyclerViewFilter()
        initPhoto(args.imagePath, args.imageLastModified)

        setEvent()

        return binding.root
    }

    //필터 목록 초기화
    private fun initRecyclerViewFilter() {
        filterAdapter = FilterAdapter()

        //필터 적용
        filterAdapter.onItemClick = { photoFilter ->
            photoEditor.setFilterEffect(photoFilter)
        }

        binding.recyclerFilters.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = filterAdapter
            setHasFixedSize(true) //고정 사이즈이기 때문에 최적화
        }
    }

    //편집툴 목록 초기화
    private fun initRecyclerViewTool() {
        toolAdapter = ToolAdapter()

        toolAdapter.onItemClick = { toolType ->

            when (toolType) {
                ToolType.BRUSH -> {
                    photoEditor.setBrushDrawingMode(true)
                    binding.txtCurrentTool.text = "그리기"
                    showBrushBottomSheet()
                }

                ToolType.TEXT -> {
                    showTextEditorDialog()
                    binding.txtCurrentTool.text = "텍스트"
                }

                ToolType.ERASER -> {
                    photoEditor.brushEraser()
                    binding.txtCurrentTool.text = "지우개"
                }

                ToolType.FILTER -> {
                    showFilterRecyclerView(true)
                    binding.txtCurrentTool.text = "필터링"
                }

                ToolType.EMOJI -> {
                    showEmojiBottomSheet()
                    binding.txtCurrentTool.text = "이모지"
                }

                ToolType.STICKER -> {
                    showStickerBottomSheet()
                    binding.txtCurrentTool.text = "스티커"
                }

                ToolType.CROP -> {
                    cropImage.launch(
                        CropImageContractOptions(
                            uri = ImageUtil.getImageUri(requireContext(), args.imagePath),
                            cropImageOptions = CropImageOptions(
                                guidelines = CropImageView.Guidelines.ON,
                                outputCompressFormat = Bitmap.CompressFormat.JPEG,
                                allowRotation = false,
                                allowCounterRotation = false,
                                allowFlipping = false
                            )
                        )
                    )
                    binding.txtCurrentTool.text = "자르기"
                }
            }

        }

        binding.recyclerTools.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = toolAdapter
            setHasFixedSize(true) //고정 사이즈이기 때문에 최적화
        }
    }

    //선택한 이미지 불러오기
    private fun initPhoto(imagePath: String, imageLastModified: String) {
        //선택한 이미지 적용
        Glide.with(this)
            .load(imagePath)
            .placeholder(ImageUtil.getShimmerDrawable())
            .signature(ObjectKey(imageLastModified))
            .into(binding.photoEditorView.source)

        val textFont = ResourcesCompat.getFont(requireContext(), R.font.bm_hanna_pro)
        val emojiTypeFace = resources.getFont(R.font.emojione_android)

        photoEditor = PhotoEditor.Builder(requireContext(), binding.photoEditorView)
            .setPinchTextScalable(true) // set flag to make text scalable when pinch
            .setDefaultTextTypeface(textFont)
            .setDefaultEmojiTypeface(emojiTypeFace)
            .build() // build photo editor sdk

        //기본 필터 설정
        photoEditor.setFilterEffect(PhotoFilter.NONE)
    }

    //이벤트 초기화
    private fun setEvent() {
        //버튼 클릭 이벤트
        binding.apply {
            btnClose.setOnClickListener {
                findNavController().navigateUp()
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

        //프래그먼트에서 뒤로가기 버튼 콜백 생성
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, // 생명주기를 관리해 종료될 때 자동으로 콜백 해제 처리
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // 백 버튼을 눌렀을 때 수행할 작업
                    // 예: 이전 화면으로 이동하지 않고 다른 작업을 수행하고 싶을 때
                    // requireActivity().onBackPressedDispatcher.onBackPressed()을 호출하면 원래 동작(뒤로 가기)이 실행됨.
                    if (binding.recyclerFilters.isVisible) {
                        showFilterRecyclerView(false)
                        binding.txtCurrentTool.text = resources.getString(R.string.app_name)
                    } else if (!photoEditor.isCacheEmpty) {
                        //이미지를 편집한 이력이 있다면 편집 뒤로가기
                        photoEditor.undo()
                    } else {
                        //화면 닫기
                        findNavController().navigateUp()
                    }
                }
            })

        //사진 편집 툴 이벤트
        photoEditor.setOnPhotoEditorListener(object : OnPhotoEditorListener {
            override fun onAddViewListener(viewType: ViewType, numberOfAddedViews: Int) {
            }

            //텍스트 수정 이벤트
            override fun onEditTextChangeListener(rootView: View, text: String, colorCode: Int) {
                showTextEditorDialog(text, colorCode)
                binding.txtCurrentTool.text = "텍스트"
            }

            override fun onRemoveViewListener(viewType: ViewType, numberOfAddedViews: Int) {
            }

            override fun onStartViewChangeListener(viewType: ViewType) {
            }

            override fun onStopViewChangeListener(viewType: ViewType) {
            }

            override fun onTouchSourceImage(event: MotionEvent) {
            }
        })
    }

    //그리기 바텀시트 띄우기
    private fun showBrushBottomSheet() {
        val emojiBottomSheet = BottomSheetPropertyFragment()

        //브러쉬 사이즈 설정 이벤트
        emojiBottomSheet.onSizeChangedListener = { size ->
            photoEditor.setShape(ShapeBuilder().withShapeSize(size.toFloat()))
            binding.txtCurrentTool.text = "그리기"
        }

        //브러쉬 색상 설정 이벤트
        emojiBottomSheet.onColorChangedListener = { color ->
            photoEditor.setShape(ShapeBuilder().withShapeColor(color))
            binding.txtCurrentTool.text = "그리기"
        }

        //브러쉬 투명도 설정 이벤트
        emojiBottomSheet.onOpacityChangedListener = { opacity ->
            photoEditor.setShape(ShapeBuilder().withShapeOpacity(opacity))
            binding.txtCurrentTool.text = "그리기"
        }

        //그리기 바텀시트 보여주기
        emojiBottomSheet.show(childFragmentManager, "BottomSheetPropertyFragment")
    }

    //텍스트 바텀시트 띄우기
    private fun showTextEditorDialog(text: String = "", colorCode: Int = Color.WHITE) {
        val textEditorDialog = DialogTextEditorFragment.newInstance(text, colorCode)

        //텍스트 수정 이벤트
        //완료 버튼을 클릭했을 때 1번만 호출됨
        textEditorDialog.onTextStateChangeListener = { text, color ->
            photoEditor.addText(text, color)
            binding.txtCurrentTool.text = "텍스트"
        }

        //텍스트 추가 바텀시트 보여주기
        textEditorDialog.show(childFragmentManager, "DialogTextEditorFragment")
    }

    //이모지 바텀시트 띄우기
    private fun showEmojiBottomSheet() {
        val emojiBottomSheet = BottomSheetEmojiFragment()

        //이모지 선택 이벤트
        emojiBottomSheet.onEmojiSelectedListener = { emoji ->
            photoEditor.addEmoji(emoji)
            binding.txtCurrentTool.text = "이모지"
        }

        //이모지 추가 바텀시트 보여주기
        emojiBottomSheet.show(childFragmentManager, "BottomSheetEmojiFragment")
    }

    //스티커 바텀시트 띄우기
    private fun showStickerBottomSheet() {
        val stickerBottomSheet = BottomSheetStickerFragment()

        //스티커 선택 이벤트
        stickerBottomSheet.onStickerSelectedListener = { sticker ->
            photoEditor.addImage(sticker)
            binding.txtCurrentTool.text = "스티커"
        }

        //스티커 추가 바텀시트 보여주기
        stickerBottomSheet.show(childFragmentManager, "BottomSheetStickerFragment")
    }

    //필터링 목록 출력
    private fun showFilterRecyclerView(visibility: Boolean) {
        binding.recyclerFilters.isVisible = visibility
        binding.recyclerTools.isInvisible = visibility
    }

    //편집한 이미지 저장
    @SuppressLint("MissingPermission")
    private fun saveImage() {
        //저장 여부 확인 다이얼로그 출력
        ConfirmDialog(
            requireContext(),
            "이미지를 저장하시겠습니까?"
        ) {
            //확인 이벤트 정의
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                //기존 이미지의 경로를 활용해 덮어쓰기
                val result = photoEditor.saveAsFile(
                    args.imagePath,
                    SaveSettings.Builder()
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .setTransparencyEnabled(false) // JPEG 타입이기 때문에 투명도는 설정될 필요가 없다
                        .setClearViewsEnabled(false) //편집된 뷰를 초기화하지 않는다
                        .build()
                )

                //다운로드 결과 처리
                withContext(Dispatchers.Main) {
                    if (result is SaveFileResult.Success) {
                        MessageUtil.showToast(requireContext(), "이미지가 저장되었습니다.")
                        findNavController().navigateUp()
                    } else {
                        MessageUtil.showToast(requireContext(), "이미지 저장에 실패하였습니다.")

                    }
                }
            }
        }.show()

    }

    //이미지 자르기
    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // 잘라낸 이미지 결과물
            val croppedImageUri = result.uriContent

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