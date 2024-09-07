package com.skymilk.wallpaperapp.store.presentation.screen.download

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.skymilk.wallpaperapp.databinding.FragmentMyDownloadBinding
import com.skymilk.wallpaperapp.store.presentation.common.ConfirmDialog
import com.skymilk.wallpaperapp.store.presentation.common.fragment.BottomSheetDownloadFragment
import com.skymilk.wallpaperapp.store.presentation.util.ImageUtil
import com.skymilk.wallpaperapp.store.presentation.util.MessageUtil
import com.skymilk.wallpaperapp.util.PermissionUtil
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.coroutines.launch
import java.io.File


class MyDownloadFragment : Fragment() {

    private lateinit var binding: FragmentMyDownloadBinding
    private val myDownloadImageAdapter: MyDownloadImageAdapter = MyDownloadImageAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyDownloadBinding.inflate(layoutInflater, container, false)

        initLayout()
        setEvent()

        return binding.root
    }

    private fun initLayout() {
        //다운로드된 이미지 목록 가져오기
        val imageList = ImageUtil.getSavedImages()

        //다운로드 이미지 목록 여부에 따라 레이아웃 가시 처리
        if (imageList.isEmpty()) {
            initEmpty()
        } else {
            initViewPager(imageList)
        }
    }

    //빈 레이아웃 설정 초기화
    private fun initEmpty() {
        binding.layoutEmpty.visibility = View.VISIBLE
        binding.layoutDownloadedImage.visibility = View.GONE
    }

    //뷰페이저 초기화
    private fun initViewPager(imageList: List<File>) {
        //뷰 가시 설정
        binding.layoutEmpty.visibility = View.GONE
        binding.layoutDownloadedImage.visibility = View.VISIBLE

        myDownloadImageAdapter.differ.submitList(imageList)

        binding.viewPagerImage.apply {
            adapter = myDownloadImageAdapter
            offscreenPageLimit = 3
            setPageTransformer(SliderTransformer(3))
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

                    //조회중인 이미지를 블러처리 해 백그라운드에 적용
                    Glide.with(requireContext())
                        .load(myDownloadImageAdapter.differ.currentList[position].absolutePath)
                        .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
                        .into(object : CustomTarget<Drawable?>() {
                            override fun onResourceReady(
                                resource: Drawable,
                                transition: Transition<in Drawable?>?
                            ) {
                                binding.layoutConstraint.background = resource
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                            }
                        })
                }
            })
        }
    }

    //현재 ViewPager가 보고 있는 아이템 위치의 이미지 뷰 가져오기
    private fun getCurrentImageView(): ImageView {
        // ViewPager2 내부의 RecyclerView에 접근
        val recyclerView = binding.viewPagerImage.getChildAt(0) as? RecyclerView

        // 현재 위치의 ViewHolder를 가져옴
        val viewHolder =
            recyclerView?.findViewHolderForAdapterPosition(binding.viewPagerImage.currentItem) as? MyDownloadImageAdapter.MyDownloadImageViewHolder

        return viewHolder!!.binding.imageDownload
    }

    //현재 viewPager가 보고 있는 아이템 위치의 파일 객체 가져오기
    private fun getCurrentImageFile(): File =
        myDownloadImageAdapter.differ.currentList[binding.viewPagerImage.currentItem]

    //이미지 제거하기
    private fun deleteImage() {
        //저장 여부 확인 다이얼로그 출력
        ConfirmDialog(
            requireContext(),
            "이미지를 정말 삭제하시겠습니까?"
        ) {
            //확인 버튼 클릭 시 이벤트 처리
            val file = getCurrentImageFile()
            val currentItemPosition = binding.viewPagerImage.currentItem

            // 코루틴을 사용하여 이미지 제거를 비동기적으로 처리
            viewLifecycleOwner.lifecycleScope.launch {
                //이미지 제거 및 제거 여부 체크
                if (ImageUtil.deleteImageFile(file.absolutePath)) {

                    //새로고침 구현 필요

                    MessageUtil.showToast(requireContext(), "이미지가 제거되었습니다.")
                } else {
                    MessageUtil.showToast(requireContext(), "이미지 제거에 실패하였습니다.")
                }
            }
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
                val file = getCurrentImageFile()

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