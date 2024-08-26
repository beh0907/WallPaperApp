package com.skymilk.wallpaperapp.store.presentation.common.fragment

import android.app.DownloadManager
import android.app.WallpaperManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.imageview.ShapeableImageView
import com.skymilk.wallpaperapp.R
import com.skymilk.wallpaperapp.databinding.DialogBottomSheetBinding
import com.skymilk.wallpaperapp.store.presentation.common.ImageDownloadManager
import com.skymilk.wallpaperapp.utils.Constants

class BottomSheetFragment(private val imageUrl: String) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogBottomSheetBinding.inflate(inflater)
        setClick()
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
        ImageDownloadManager.downloadImageFromUrl(url, requireContext(), object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                //다운로드 완료 체크
                if (p1?.action != DownloadManager.ACTION_DOWNLOAD_COMPLETE) return


            }

        })
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