package com.skymilk.wallpaperapp.store.presentation.common.fragments

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skymilk.wallpaperapp.databinding.DialogDownloadBottomSheetBinding
import com.skymilk.wallpaperapp.store.presentation.common.ImageDownloadManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BottomSheetDownloadFragment : BottomSheetDialogFragment() {

    private lateinit var binding: DialogDownloadBottomSheetBinding
    private var imageUrl: String? = null
    private lateinit var bitmap: Bitmap

    companion object {
        private const val ARG_IMAGE_URL = "image_url"
        private const val ARG_BITMAP = "bitmap"

        fun newInstance(imageUrl: String? = null, bitmap: Bitmap): BottomSheetDownloadFragment {
            val fragment = BottomSheetDownloadFragment()
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
        binding = DialogDownloadBottomSheetBinding.inflate(inflater)

        setVisible()
        setClick()

        return binding.root
    }

    private fun setVisible() {
        //다운로드할 URL 정보가 없다면 download 버튼을 지운다
        if (imageUrl == null) binding.btnDownload.visibility = View.GONE
    }

    private fun setClick() {
        binding.apply {
            btnDownload.setOnClickListener {
                downloadImageFromUrl(imageUrl!!)
            }

            btnSetBackGround.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    setBackGround(WallpaperManager.FLAG_SYSTEM)
                }
            }

            btnSetLockScreen.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    setBackGround(WallpaperManager.FLAG_LOCK)
                }
            }
        }
    }

    private fun downloadImageFromUrl(url: String) {
        ImageDownloadManager.downloadImageFromUrl(
            url,
            requireContext()
        )
    }

    private suspend fun setBackGround(flag: Int) {
        try {
            // 백그라운드에서 실행
            withContext(Dispatchers.IO) {
                val wallPaperManager = WallpaperManager.getInstance(requireContext())
                wallPaperManager.setBitmap(bitmap, null, true, flag)
            }

            withContext(Dispatchers.Main) {
                Toast.makeText(
                    requireContext(),
                    "이미지가 적용되었습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            //종료
            dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    requireContext(),
                    "적용 실패 - ${e.message.toString()}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}