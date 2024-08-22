package com.skymilk.wallpaperapp.store.presentation.common

import android.app.DownloadManager
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.imageview.ShapeableImageView
import com.skymilk.wallpaperapp.R
import com.skymilk.wallpaperapp.databinding.DialogBottomSheetBinding
import com.skymilk.wallpaperapp.utils.Constants
import java.io.File

class BottomSheetFragment(private val imageUrl: String) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogBottomSheetBinding.inflate(inflater)

        return binding.root
    }

    private fun setClick() {
        binding.apply {
            btnDownload.setOnClickListener { downloadImageFromUrl(imageUrl) }

            btnSetBackGround.setOnClickListener { setBackGround(Constants.Background.HOME_SCREEN) }

            btnSetLockScreen.setOnClickListener { setBackGround(Constants.Background.LOCK_SCREEN) }
        }
    }

    private fun downloadImageFromUrl(url: String) {
        try {
            val downloadManager =
                requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

            val imageUrl = Uri.parse(url)
            val request = DownloadManager.Request(imageUrl).apply {
                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                    .setMimeType("image/*")
                    .setAllowedOverRoaming(false)
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setTitle("이미지 다운로드")
                    .setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_PICTURES,
                        File.separator + "image" + ".jpg"
                    )
            }

            downloadManager.enqueue(request)
            Toast.makeText(requireContext(), "이미지 다운로드 중......", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()

            Toast.makeText(
                requireContext(),
                "이미지 다운로드 실패 - ${e.message.toString()}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setBackGround(LockOrBackground: Int) {
        try {
            val wallPaperManager = WallpaperManager.getInstance(requireContext())
            val image = requireActivity().findViewById<ShapeableImageView>(R.id.imageDownload)

            if (image?.drawable == null) {
                Toast.makeText(
                    requireContext(),
                    "이미지 로딩을 기다려주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val bitmap = (image.drawable as BitmapDrawable).bitmap
                wallPaperManager.setBitmap(bitmap, null, true, LockOrBackground)

                Toast.makeText(
                    requireContext(),
                    "이미지가 적용되었습니다.",
                    Toast.LENGTH_SHORT
                ).show()

            }


        } catch (e: Exception) {
            e.printStackTrace()

            Toast.makeText(
                requireContext(),
                "적용 실패 - ${e.message.toString()}",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}