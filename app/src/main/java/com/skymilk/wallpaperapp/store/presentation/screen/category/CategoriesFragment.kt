package com.skymilk.wallpaperapp.store.presentation.screen.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.skymilk.wallpaperapp.databinding.FragmentCategoriesBinding
import com.skymilk.wallpaperapp.store.presentation.screen.main.MainFragmentDirections
import com.skymilk.wallpaperapp.util.ApiCategoryList

class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)

        initRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initRecyclerView() {
        categoryAdapter = CategoryAdapter(ApiCategoryList.list)

        //선택한 카테고리명을 넘겨 화면을 이동한다
        categoryAdapter.onItemClick = { category ->
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToSpecificCategoryFragment(category)
            )
        }

        binding.recyclerWallPaper.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = categoryAdapter
        }
    }
}