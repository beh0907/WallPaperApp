package com.skymilk.wallpaperapp.store.presentation.screen.category

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.skymilk.wallpaperapp.databinding.FragmentSpecificCategoryBinding
import com.skymilk.wallpaperapp.store.presentation.common.adapter.LoaderStateAdapter
import com.skymilk.wallpaperapp.store.presentation.common.adapter.WallPaperAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SpecificCategoryFragment : Fragment() {

    @Inject
    lateinit var mainViewModelFactory: CategoryViewModel.CategoryViewModelFactory

    private val categoryViewModel by viewModels<CategoryViewModel> {
        CategoryViewModel.provideFactory(mainViewModelFactory, args.category.toString())
    }

    private lateinit var binding: FragmentSpecificCategoryBinding

    private val args: SpecificCategoryFragmentArgs by navArgs()

    private val wallPaperAdapter: WallPaperAdapter = WallPaperAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSpecificCategoryBinding.inflate(layoutInflater)

        initRecyclerView()

        setObserve()
        setCategoryName()
        setClick()

        return binding.root
    }

    private fun setObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            categoryViewModel.categoryWallPapers.collectLatest {
                wallPaperAdapter.submitData(it)
            }
        }
    }

    private fun initRecyclerView() {
        //이미지 아이템 클릭 이벤트
        wallPaperAdapter.onItemClick = { data ->
            val imageData = arrayOf(data.fullImageUrl.toString(), data.blurHash.toString())

            //이미지 URL 정보와 함꼐 다운로드 화면으로 이동
            findNavController()
                .navigate(
                    SpecificCategoryFragmentDirections.actionSpecificCategoryFragmentToDownloadFragment(
                        imageData
                    )
                )
        }

        //이미지 로드 리스너
        wallPaperAdapter.addLoadStateListener { loadState ->
            binding.apply {
                recyclerWallPaper.isVisible = loadState.source.refresh is LoadState.NotLoading
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                handleError(loadState)
            }
        }

        binding.recyclerWallPaper.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = wallPaperAdapter.withLoadStateHeaderAndFooter(
                header = LoaderStateAdapter { wallPaperAdapter.retry() },
                footer = LoaderStateAdapter { wallPaperAdapter.retry() }
            )
        }
    }

    private fun setCategoryName() {
        binding.txtCategory.text = args.category.toString()
    }

    private fun setClick() {
        binding.apply {
            btnRetry.setOnClickListener {
                wallPaperAdapter.retry()
            }

            txtCategory.onRightDrawableClickListener {
                findNavController().popBackStack()
            }
        }
    }


    //카테고리 텍스트 뷰 end단에 위치한 이미지 터치 영역 지정
    @SuppressLint("ClickableViewAccessibility")
    private fun TextView.onRightDrawableClickListener(onClicked: (view: TextView) -> Unit) {
        this.setOnTouchListener { view, event ->
            var hasConsumed = false

            if (view is TextView) {
                if (event.x >= view.width - view.totalPaddingRight)
                    if (event.action == MotionEvent.ACTION_UP) {
                        onClicked(this)
                    }
                hasConsumed = true
            }

            hasConsumed
        }
    }

    private fun handleError(loadState: CombinedLoadStates) {
        val errorState = loadState.source.append as? LoadState.Error
            ?: loadState.source.prepend as? LoadState.Error

        errorState?.let {
            Toast.makeText(requireContext(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
        }
    }
}
