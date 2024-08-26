package com.skymilk.wallpaperapp.store.presentation.screen.download

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.skymilk.wallpaperapp.BuildConfig
import com.skymilk.wallpaperapp.databinding.FragmentDownloadBinding
import com.skymilk.wallpaperapp.store.presentation.common.fragment.BottomSheetFragment
import jp.wasabeef.glide.transformations.BlurTransformation
import java.io.File
import java.io.FileOutputStream


class DownloadFragment : Fragment() {

    private lateinit var binding: FragmentDownloadBinding
    private val args: DownloadFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDownloadBinding.inflate(inflater, container, false)

        loadImage(args.imageData[0])
        setClick()

        return binding.root
    }

    private fun loadImage(url: String) {
        //선택한 이미지 적용
        Glide.with(this)
            .load(url)
            .centerCrop()
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
                findNavController().popBackStack()
            }

            btnShared.setOnClickListener {
                shareImage()
            }

            btnDownload.setOnClickListener {
                showBottomSheet()
            }

            btnEdit.setOnClickListener {
            }
        }
    }

    private fun shareImage() {
        try {
            if (binding.imageDownload.drawable == null) {
                Toast.makeText(
                    requireContext(),
                    "이미지 로딩을 기다려주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val bitmap = (binding.imageDownload.drawable as BitmapDrawable).bitmap

                // 비트맵을 캐시 디렉토리에 파일로 저장
                val file = File(requireContext().externalCacheDir, File.separator + "image.jpg")
                val fileOutputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                fileOutputStream.flush()
                fileOutputStream.close()
                file.setReadable(true, false)

                //저장한 이미지를 공유하기 위해 Intent 구성
                val imageUri = FileProvider.getUriForFile(
                    requireContext(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    file
                )
                val intent = Intent(Intent.ACTION_SEND).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    type = "image/jpg"
                    putExtra(Intent.EXTRA_STREAM, imageUri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                //이미지 공유 시작
                requireContext().startActivity(Intent.createChooser(intent, "이미지 공유"))
            }


        } catch (e: Exception) {
            e.printStackTrace()

            Toast.makeText(
                requireContext(),
                "공유 실패 - ${e.message.toString()}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showBottomSheet() {
        val bottomSheet = BottomSheetFragment(args.imageData[0])
        bottomSheet.show(requireActivity().supportFragmentManager, "bottomSheet")
    }
}