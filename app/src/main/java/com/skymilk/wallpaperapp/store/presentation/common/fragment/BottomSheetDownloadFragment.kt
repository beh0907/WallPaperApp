package com.skymilk.wallpaperapp.store.presentation.common.fragment

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skymilk.wallpaperapp.databinding.DialogDownloadBottomSheetBinding
import com.skymilk.wallpaperapp.store.presentation.util.ImageUtil
import com.skymilk.wallpaperapp.store.presentation.util.MessageUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BottomSheetDownloadFragment : BottomSheetDialogFragment() {

    private lateinit var binding: DialogDownloadBottomSheetBinding
    private var imageUrl: String? = null
    private var bitmap: Bitmap? = null

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
        requireArguments().let {
            imageUrl = it.getString(ARG_IMAGE_URL)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bitmap = it.getParcelable(ARG_BITMAP, Bitmap::class.java)
            } else {
                bitmap = it.getParcelable(ARG_BITMAP)
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
        //다운로드 URL 정보가 있다면 download 버튼을 표시한다
        binding.btnDownload.visibility = if (imageUrl == null) View.GONE else View.VISIBLE

        //공유할 이미지 bitmap이 있다면 공유 버튼을 표시한다
        binding.btnShare.visibility = if (bitmap == null) View.GONE else View.VISIBLE
    }

    private fun setClick() {
        binding.apply {

            btnDownload.setOnClickListener {
                downloadImageFromUrl(imageUrl!!)
            }

            btnShare.setOnClickListener {
                shareImage(bitmap!!)
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
        ImageUtil.downloadImageFromUrl(
            url,
            requireContext()
        )
    }

    private fun shareImage(bitmap: Bitmap) {
        ImageUtil.shareImage(
            requireContext(),
            bitmap
        )
    }

    private suspend fun setBackGround(flag: Int) {
        try {
            // 백그라운드에서 실행
            withContext(Dispatchers.IO) {
                val wallPaperManager = WallpaperManager.getInstance(requireContext())
                wallPaperManager.setBitmap(bitmap, null, true, flag)
            }


            viewLifecycleOwner.lifecycleScope.launch {
                MessageUtil.showToast(requireContext(), "이미지가 적용되었습니다.")
            }

            //종료
            dismiss()
        } catch (e: Exception) {
            e.printStackTrace()

            viewLifecycleOwner.lifecycleScope.launch {
                MessageUtil.showToast(requireContext(), "적용 실패 - ${e.message.toString()}")
            }
        }
    }
}