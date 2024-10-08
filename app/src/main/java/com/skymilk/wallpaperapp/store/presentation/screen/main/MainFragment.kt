package com.skymilk.wallpaperapp.store.presentation.screen.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.skymilk.wallpaperapp.databinding.FragmentMainBinding
import com.skymilk.wallpaperapp.store.presentation.screen.category.CategoriesFragment
import com.skymilk.wallpaperapp.store.presentation.screen.home.HomeFragment
import com.skymilk.wallpaperapp.store.presentation.screen.popular.PopularFragment
import com.skymilk.wallpaperapp.store.presentation.screen.random.RandomFragment

class MainFragment : Fragment() {

    private val tabTitles = arrayOf("Home", "Popular", "Random", "Categories")
    private val fragments =
        listOf(HomeFragment(), PopularFragment(), RandomFragment(), CategoriesFragment())

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(
            inflater,  // `layoutInflater` 대신 `inflater`를 사용합니다.
            container,
            false
        )


        initViewPager()
        initTabLayout()
        initToolBar()
        setClick()

        return binding.root
    }


    private fun initViewPager() {
        val pagerAdapter = ViewPagerAdapter(requireContext() as FragmentActivity, fragments)
        binding.viewPager.apply {
            adapter = pagerAdapter
            isUserInputEnabled = false
        }
    }

    private fun initTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    private fun initToolBar() {
        binding.toolbar.title = "WallPapers"
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
    }

    private fun setClick() {
        binding.apply {
            btnSearch.setOnClickListener {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToSearchFragment()
                )
            }

            btnMyDownload.setOnClickListener {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToMyDownloadFragment()
                )
            }
        }


    }
}