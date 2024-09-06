package com.skymilk.wallpaperapp.store.presentation.screen.download

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.skymilk.wallpaperapp.databinding.FragmentMyDownloadBinding
import com.skymilk.wallpaperapp.store.presentation.common.fragment.BottomSheetDownloadFragment
import com.skymilk.wallpaperapp.util.ImageUtil
import com.skymilk.wallpaperapp.util.ImageUtil.shareImage
import com.skymilk.wallpaperapp.util.MessageUtil
import jp.wasabeef.glide.transformations.BlurTransformation
import java.io.File


class MyDownloadFragment : Fragment() {

    private lateinit var binding: FragmentMyDownloadBinding
    private val myDownloadImageAdapter: MyDownloadImageAdapter = MyDownloadImageAdapter()

    private lateinit var imageList: List<File>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyDownloadBinding.inflate(layoutInflater, container, false)

        initLayout()
        setEvent()

        return binding.root
    }

    private fun initLayout() {
        //다운로드된 이미지 목록 가져오기
        imageList = ImageUtil.getSavedImages()

        //다운로드 이미지 목록 여부에 따라 레이아웃 가시 처리
        if (imageList.isEmpty()) {
            binding.layoutEmpty.visibility = View.VISIBLE
            binding.layoutDownloadedImage.visibility = View.GONE
        } else {
            binding.layoutEmpty.visibility = View.GONE
            binding.layoutDownloadedImage.visibility = View.VISIBLE

            //ViewPager 초기화
            initViewPager()
        }

    }

    private fun initViewPager() {
        myDownloadImageAdapter.differ.submitList(imageList)

        binding.viewPagerImage.apply {
            adapter = myDownloadImageAdapter
            offscreenPageLimit = 3
            setPageTransformer(SliderTransformer(3))
        }
    }

    private fun setEvent() {
        binding.apply {

            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnShared.setOnClickListener {
                shareImage()
            }

            btnSetBackGround.setOnClickListener {
                showDownloadBottomSheet()
            }

            btnEdit.setOnClickListener {
                editWallPaper()
            }

            viewPagerImage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    //선택한 이미지를 블러처리 해 백그라운드에 적용
                    Glide.with(requireContext())
                        .load(imageList[position].absolutePath)
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

    private fun getCurrentImageView(): ImageView {
        //현재 ViewPager가 위치의 이미지 뷰를 가져온다

        // ViewPager2 내부의 RecyclerView에 접근
        val recyclerView = binding.viewPagerImage.getChildAt(0) as? RecyclerView

        // 현재 위치의 ViewHolder를 가져옴
        val viewHolder =
            recyclerView?.findViewHolderForAdapterPosition(binding.viewPagerImage.currentItem) as? MyDownloadImageAdapter.MyDownloadImageViewHolder

        return viewHolder!!.binding.imageDownload
    }

    private fun getCurrentImageFile(): File = imageList[binding.viewPagerImage.currentItem]

    private fun shareImage() {
        val imageView = getCurrentImageView()

        if (imageView.drawable == null || imageView.drawable !is BitmapDrawable) {
            MessageUtil.showToast(requireContext(), "이미지 로딩을 기다려주세요.")
            return
        }

        ImageUtil.shareImage(
            requireContext(),
            (imageView.drawable as BitmapDrawable).bitmap
        )
    }


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

    private fun editWallPaper() {
        val file = getCurrentImageFile()

        findNavController().navigate(
            MyDownloadFragmentDirections.actionMyDownloadFragmentToEditFragment(
                file.absolutePath
            )
        )
    }
}