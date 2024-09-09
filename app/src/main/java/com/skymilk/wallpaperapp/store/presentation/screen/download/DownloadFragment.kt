package com.skymilk.wallpaperapp.store.presentation.screen.download

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.skymilk.wallpaperapp.databinding.FragmentDownloadBinding
import com.skymilk.wallpaperapp.store.presentation.common.fragment.BottomSheetDownloadFragment
import com.skymilk.wallpaperapp.store.presentation.util.ImageUtil
import com.skymilk.wallpaperapp.store.presentation.util.MessageUtil
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.coroutines.launch


class DownloadFragment : Fragment() {

    private lateinit var binding: FragmentDownloadBinding
    private val args: DownloadFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDownloadBinding.inflate(inflater, container, false)

        loadImage(args.imagePath)
        setClick()

        return binding.root
    }

    private fun loadImage(url: String) {
        //선택한 이미지 적용
        Glide.with(this)
            .load(url)
            .optionalCenterCrop()
            .placeholder(ImageUtil.getShimmerDrawable())
            .into(binding.imageDownload)

        //백그라운드 블러처리
        Glide.with(this)
            .load(url)
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

    private fun setClick() {

        binding.apply {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnDownload.setOnClickListener {
                showDownloadBottomSheet()
            }
        }
    }

    private fun showDownloadBottomSheet() {
        if (binding.imageDownload.drawable == null || binding.imageDownload.drawable !is BitmapDrawable) {
            MessageUtil.showToast(requireContext(), "이미지 로딩을 기다려주세요.")
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            //공유를 위한 임시 이미지 저장
            val cacheFile = ImageUtil.saveCacheImage(
                requireContext(),
                (binding.imageDownload.drawable as BitmapDrawable).bitmap
            )

            //이미지 다운로드를 위한 이미지 URL 전달
            //임시 이미지 저장 후 경로 전달
            val bottomSheet = BottomSheetDownloadFragment.newInstance(
                downloadImageUrl = args.imagePath,
                currentImageUrl = cacheFile.absolutePath
            )

            bottomSheet.show(childFragmentManager, "download bottomSheet")
        }
    }
}