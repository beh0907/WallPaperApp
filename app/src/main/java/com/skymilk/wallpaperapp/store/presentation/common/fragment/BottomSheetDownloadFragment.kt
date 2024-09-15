package com.skymilk.wallpaperapp.store.presentation.common.fragment

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ketch.Ketch
import com.ketch.Status
import com.skymilk.wallpaperapp.databinding.DialogDownloadBottomSheetBinding
import com.skymilk.wallpaperapp.store.presentation.util.ImageUtil
import com.skymilk.wallpaperapp.store.presentation.util.MessageUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class BottomSheetDownloadFragment : BottomSheetDialogFragment() {

    private lateinit var binding: DialogDownloadBottomSheetBinding

    private var downloadImageUrl: String? = null

    //공유, 배경화면 설정을 위한 이미지 정보
    private lateinit var currentImageFile: File
    private var bitmap: Bitmap? = null

    //다운로드 매니저
    @Inject
    lateinit var ketch: Ketch

    companion object {
        private const val ARG_DOWNLOAD_IMAGE_URL = "download_image_url"
        private const val ARG_CURRENT_IMAGE_PATH = "current_image_path"

        fun newInstance(
            downloadImageUrl: String? = null,
            currentImageUrl: String
        ): BottomSheetDownloadFragment {
            val fragment = BottomSheetDownloadFragment()
            val args = Bundle().apply {
                putString(ARG_DOWNLOAD_IMAGE_URL, downloadImageUrl)
                putString(ARG_CURRENT_IMAGE_PATH, currentImageUrl)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().let {
            downloadImageUrl = it.getString(ARG_DOWNLOAD_IMAGE_URL)

            it.getString(ARG_CURRENT_IMAGE_PATH)?.let { imageUrl ->
                currentImageFile = File(imageUrl)
                bitmap = ImageUtil.getBitmapFromCache(currentImageFile)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogDownloadBottomSheetBinding.inflate(inflater)

        setVisible()
        setClick()

        return binding.root
    }

    private fun setVisible() {
        //다운로드 URL 정보가 있다면 download 버튼을 표시한다
        binding.btnDownload.isVisible = downloadImageUrl != null

        //공유할 이미지 bitmap이 있다면 공유 버튼을 표시한다
        binding.btnShare.isVisible = bitmap != null
    }

    private fun setClick() {
        binding.apply {

            btnDownload.setOnClickListener {
                downloadImageWithKetch(downloadImageUrl!!)
            }

            btnShare.setOnClickListener {
                shareImage(currentImageFile)
            }

            btnSetBackGround.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    setBackGround(WallpaperManager.FLAG_SYSTEM, bitmap!!)
                }
            }

            btnSetLockScreen.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    setBackGround(WallpaperManager.FLAG_LOCK, bitmap!!)
                }
            }
        }
    }

    private fun downloadImageWithKetch(url: String) {
        val fileName = "image_${System.currentTimeMillis()}.jpg"
        val path =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath +
                    File.separator + "wallpapers" + File.separator
        val id = ketch.download(url = url, path = path, fileName = fileName)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                ketch.observeDownloadById(id).collect { downloadModel ->

                    when (downloadModel.status) {
                        //다운로드 성공
                        Status.SUCCESS -> {
                            Log.d("downloadModel SUCCESS", downloadModel.toString())
                            MessageUtil.showToast(requireContext(), "이미지 다운로드 완료")
                            dismissAllowingStateLoss()
                        }

                        //다운로드 실패
                        Status.FAILED -> {
                            MessageUtil.showToast(requireContext(), "이미지 다운로드에 실패하였습니다")
                        }

                        Status.QUEUED -> {}
                        Status.STARTED -> {}
                        Status.PROGRESS -> {}
                        Status.CANCELLED -> {}
                        Status.PAUSED -> {}
                        Status.DEFAULT -> {}
                    }
                }
            }
        }
    }

    private fun shareImage(file: File) {
        //이미지 공유
        ImageUtil.shareImage(
            requireContext(),
            file
        )

        //공유 후 종료
        dismissAllowingStateLoss()
    }

    private suspend fun setBackGround(flag: Int, bitmap: Bitmap) {
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
            dismissAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()

            viewLifecycleOwner.lifecycleScope.launch {
                MessageUtil.showToast(requireContext(), "적용 실패 - ${e.message.toString()}")
            }
        }
    }
}