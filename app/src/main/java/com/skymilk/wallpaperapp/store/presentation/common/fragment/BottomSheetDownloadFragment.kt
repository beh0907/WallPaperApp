package com.skymilk.wallpaperapp.store.presentation.common.fragment

import android.app.DownloadManager
import android.app.WallpaperManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skymilk.wallpaperapp.databinding.DialogDownloadBottomSheetBinding
import com.skymilk.wallpaperapp.store.presentation.util.ImageUtil
import com.skymilk.wallpaperapp.store.presentation.util.MessageUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class BottomSheetDownloadFragment : BottomSheetDialogFragment() {

    private lateinit var binding: DialogDownloadBottomSheetBinding

    private var downloadImageUrl: String? = null

    //공유, 배경화면 설정을 위한 이미지 정보
    private lateinit var currentImageFile: File
    private var bitmap: Bitmap? = null

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
                downloadImageFromUrl(downloadImageUrl!!)
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

    private fun downloadImageFromUrl(url: String) {
        ImageUtil.downloadImageFromUrl(
            url,
            requireContext(),
            object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    Log.d("receive context", context.toString())
                    Log.d("receive intent", intent.toString())
                    val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

                    //메시지 출력
                    MessageUtil.showToast(requireActivity(), "이미지 다운로드 완료")

                    // 여기에 다운로드 완료 후 필요한 작업을 수행
                    requireContext().unregisterReceiver(this) // 리시버 해제
                    dismiss() // BottomSheetDialogFragment 닫기
                }

            }
        )
    }

    private fun shareImage(file: File) {
        //이미지 공유
        ImageUtil.shareImage(
            requireContext(),
            file
        )

        //공유 후 종료
        dismiss()
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
            dismiss()
        } catch (e: Exception) {
            e.printStackTrace()

            viewLifecycleOwner.lifecycleScope.launch {
                MessageUtil.showToast(requireContext(), "적용 실패 - ${e.message.toString()}")
            }
        }
    }
}