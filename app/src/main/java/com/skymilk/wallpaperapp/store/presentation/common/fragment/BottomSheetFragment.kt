package com.skymilk.wallpaperapp.store.presentation.common.fragment

import android.app.DownloadManager
import android.app.WallpaperManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skymilk.wallpaperapp.databinding.DialogBottomSheetBinding
import com.skymilk.wallpaperapp.store.presentation.common.ImageDownloadManager

class BottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: DialogBottomSheetBinding
    private var imageUrl: String? = null
    private lateinit var bitmap: Bitmap

    companion object {
        private const val ARG_IMAGE_URL = "image_url"
        private const val ARG_BITMAP = "bitmap"

        fun newInstance(imageUrl: String? = null, bitmap: Bitmap): BottomSheetFragment {
            val fragment = BottomSheetFragment()
            val args = Bundle().apply {
                putString(ARG_IMAGE_URL, imageUrl)
                putParcelable(ARG_BITMAP, bitmap)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageUrl = it.getString(ARG_IMAGE_URL)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bitmap = it.getParcelable(ARG_BITMAP, Bitmap::class.java)!!
            } else {
                bitmap = it.getParcelable(ARG_BITMAP)!!
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogBottomSheetBinding.inflate(inflater)

        setVisible()
        setClick()

        return binding.root
    }

    private fun setVisible() {
        if (imageUrl == null) binding.btnDownload.visibility = View.GONE
    }

    private fun setClick() {
        binding.apply {
            btnDownload.setOnClickListener { downloadImageFromUrl(imageUrl!!) }

            btnSetBackGround.setOnClickListener { setBackGround(WallpaperManager.FLAG_SYSTEM) }

            btnSetLockScreen.setOnClickListener { setBackGround(WallpaperManager.FLAG_LOCK) }
        }
    }

    private fun downloadImageFromUrl(url: String) {
        ImageDownloadManager.downloadImageFromUrl(
            url,
            requireContext(),
            object : BroadcastReceiver() {
                override fun onReceive(p0: Context?, p1: Intent?) {
                    //다운로드 완료 체크
                    if (p1?.action != DownloadManager.ACTION_DOWNLOAD_COMPLETE) return
                }

            })
    }

    private fun setBackGround(flag: Int) {
        try {
            val wallPaperManager = WallpaperManager.getInstance(requireContext())
            wallPaperManager.setBitmap(bitmap, null, true, flag)

            Toast.makeText(
                requireContext(),
                "이미지가 적용되었습니다.",
                Toast.LENGTH_SHORT
            ).show()

            //종료
            dismiss()
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