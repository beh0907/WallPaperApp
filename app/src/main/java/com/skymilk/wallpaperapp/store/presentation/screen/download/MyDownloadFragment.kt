package com.skymilk.wallpaperapp.store.presentation.screen.download

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import com.skymilk.wallpaperapp.R
import com.skymilk.wallpaperapp.databinding.FragmentMyDownloadBinding
import com.skymilk.wallpaperapp.store.presentation.common.ConfirmDialog
import com.skymilk.wallpaperapp.store.presentation.common.fragment.BottomSheetDownloadFragment
import com.skymilk.wallpaperapp.store.presentation.util.MessageUtil
import com.skymilk.wallpaperapp.util.PermissionUtil
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File


class MyDownloadFragment : Fragment() {

    private lateinit var binding: FragmentMyDownloadBinding

    private val myDownloadImageAdapter: MyDownloadImageAdapter = MyDownloadImageAdapter()

    private val viewModel: MyDownloadViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyDownloadBinding.inflate(layoutInflater, container, false)

        initViewPager()
        setObserve()
        setEvent()

        return binding.root
    }

    //뷰페이저 초기화
    private fun initViewPager() {
        binding.viewPagerImage.apply {
            adapter = myDownloadImageAdapter
            offscreenPageLimit = 5 // 메모리에 유지할 페이지 수
            setPageTransformer(SliderTransformer(5)) // 페이지 전환 애니메이션 설정
        }
    }

    private fun setObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            // 뷰의 생명주기에 맞춰 상태 수집
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    when {
                        state.isLoading -> showLoading()
                        state.error != null -> showError(state.error)
                        state.isEmptyState -> showEmptyState()
                        else -> showImageList(state.imageList)
                    }
                }
            }
        }
    }

    // 로딩 상태 표시
    private fun showLoading() {
        // 로딩 UI 표시
    }

    // 에러 상태 표시
    private fun showError(error: String) {
        MessageUtil.showToast(requireContext(), error)
        viewModel.clearError() // 에러 메시지 표시 후 초기화
    }

    // 이미지 목록이 비어있을 때의 상태 표시
    private fun showEmptyState() {
        binding.layoutEmpty.visibility = View.VISIBLE
        binding.layoutDownloadedImage.visibility = View.GONE

        // 빈 상태일 때 기본 배경으로 설정
        binding.layoutConstraint.background =
            ContextCompat.getDrawable(requireContext(), R.color.main_background)
    }

    // 이미지 목록 표시
    private fun showImageList(imageList: List<File>) {
        binding.layoutEmpty.visibility = View.GONE
        binding.layoutDownloadedImage.visibility = View.VISIBLE

        // 첫 초기화에서는 0이지만 삭제 후 불러올 땐 그 위치로 설정된다
        val currentPosition = binding.viewPagerImage.currentItem
        myDownloadImageAdapter.differ.submitList(imageList) {
            // 리스트 업데이트 완료 후 실행되는 콜백
            if (imageList.isNotEmpty()) {
                // 현재 위치가 새 리스트 크기를 초과하지 않도록 조정
                val newPosition = minOf(currentPosition, imageList.size - 1)
                binding.viewPagerImage.setCurrentItem(newPosition, false)
                updateBackground(newPosition) // 배경 업데이트

                //레이아웃 재적용
                //삭제 후 이동 시 뷰가 그려지지 않는 오류 발생
                binding.viewPagerImage.post {
                    binding.viewPagerImage.requestTransform()
                }
            }
        }
    }

    private fun setEvent() {
        binding.apply {
            //화면 뒤로가기 이벤트
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            //삭제 버튼 이벤트
            btnDelete.setOnClickListener {
                deleteImage()
            }

            //배경화면 설정 버튼 이벤트
            btnSetBackGround.setOnClickListener {
                showDownloadBottomSheet()
            }

            //이미지 편집 버튼 이벤트
            btnEdit.setOnClickListener {
                editWallPaper()
            }

            //ViewPager 콜백 이벤트
            viewPagerImage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    updateBackground(position)
                }
            })
        }
    }

    //특정 위치의 이미지를 화면 백그라운드에 블러처리 후 반영
    private fun updateBackground(position: Int) {
        //특정 위치 이미지 파일 가져오기
        val currentFile = getImageFile(position)

        //이미지 반영
        Glide.with(requireContext())
            .load(currentFile.absolutePath)
            .signature(
                ObjectKey(
                    currentFile.lastModified().toString()
                )
            ) // 캐싱으로 인해 삭제된 이미지 출력 문제가 생겨 signature 설정
            .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
            .into(object : CustomTarget<Drawable?>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable?>?
                ) {
                    //블러처리된 이미지 반영 적용
                    binding.layoutConstraint.background = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    // 현재 표시 중인 이미지 뷰 가져오기
    private fun getCurrentImageView(): ImageView {
        // ViewPager2 내부의 RecyclerView에 접근
        val recyclerView = binding.viewPagerImage.getChildAt(0) as? RecyclerView

        // 현재 위치의 ViewHolder를 가져옴
        val viewHolder =
            recyclerView?.findViewHolderForAdapterPosition(binding.viewPagerImage.currentItem) as? MyDownloadImageAdapter.MyDownloadImageViewHolder

        return viewHolder?.binding?.imageDownload
            ?: throw IllegalStateException("현재 이미지 뷰를 찾을 수 없습니다.")
    }

    // 이미지 파일 가져오기
    // 파라미터가 없다면 현재 표시된 이미지를 가져온다
    private fun getImageFile(position: Int = binding.viewPagerImage.currentItem): File =
        myDownloadImageAdapter.differ.currentList[position]

    //이미지 삭제 처리
    private fun deleteImage() {
        //삭제 여부 확인 다이얼로그 출력
        ConfirmDialog(
            requireContext(),
            "이미지를 정말 삭제하시겠습니까?"
        ) {
            //현재 선택한 이미지(파일)을 삭제처리한다
            viewModel.deleteImage(getImageFile())
        }.show()
    }

    //다운로드 바텀 시트 띄우기
    private fun showDownloadBottomSheet() {
        val imageView = getCurrentImageView()

        if (imageView.drawable == null || imageView.drawable !is BitmapDrawable) {
            MessageUtil.showToast(requireContext(), "이미지 로딩을 기다려주세요.")
            return
        }

        //이미 다운로드한 이미지이기 떄문에 URL은 넘겨주지 않는다
        val bottomSheet = BottomSheetDownloadFragment.newInstance(
            bitmap = (imageView.drawable as BitmapDrawable).bitmap
        )
        bottomSheet.show(childFragmentManager, "myDownload bottomSheet")
    }

    //이미지 편집 화면으로 이동
    private fun editWallPaper() {
        viewLifecycleOwner.lifecycleScope.launch {
            //권한 여부 처리
            if (PermissionUtil.requestStoragePermissions()) {
                val file = getImageFile()

                findNavController().navigate(
                    MyDownloadFragmentDirections.actionMyDownloadFragmentToEditFragment(
                        file.absolutePath, // 이미지 파일 절대 경로
                        file.lastModified().toString() // 캐싱 처리의 시그니처 값으로 활용될 마지막 수정 날짜
                    )
                )
            } else {
                MessageUtil.showToast(requireContext(), "권한을 허용해주세요.")
            }
        }
    }
}