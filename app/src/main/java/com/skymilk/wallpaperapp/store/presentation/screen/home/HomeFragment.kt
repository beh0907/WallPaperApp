package com.skymilk.wallpaperapp.store.presentation.screen.home

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.skymilk.wallpaperapp.store.presentation.common.ImageDownloadManager
import com.skymilk.wallpaperapp.store.presentation.common.adapter.WallPaperAdapter
import com.skymilk.wallpaperapp.store.presentation.common.fragment.BaseFragment
import com.skymilk.wallpaperapp.store.presentation.screen.main.MainFragmentDirections
import com.skymilk.wallpaperapp.utils.ApiListCategory
import com.skymilk.wallpaperapp.utils.ImageUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private val homeViewModel: HomeViewModel by viewModels()

    override var wallPaperAdapter: WallPaperAdapter = WallPaperAdapter()


    //임시 테스트
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Log.d("이미지 목록", ImageUtil.getSavedImages().toString())

//        val imageData = arrayOf(ApiListCategory.list[13].imageUrl, ApiListCategory.list[13].imageUrl)
//
//        findNavController()
//            .navigate(
//                MainFragmentDirections.actionMainFragmentToDownloadFragment(
//                    imageData
//                )
//            )


//        ApiListCategory.list.forEach { category ->
//            ImageDownloadManager.downloadImageFromUrl(
//                category.imageUrl,
//                requireContext(),
//                object : BroadcastReceiver() {
//                    override fun onReceive(p0: Context?, p1: Intent?) {
//                        //다운로드 완료 체크
//                        if (p1?.action != DownloadManager.ACTION_DOWNLOAD_COMPLETE) return
//                    }
//
//                })
//        }

    }

    override fun setObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.wallPapers.collectLatest {
                wallPaperAdapter.submitData(it)
            }
        }
    }
}