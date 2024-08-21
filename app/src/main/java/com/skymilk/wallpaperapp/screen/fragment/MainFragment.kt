package com.skymilk.wallpaperapp.screen.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.skymilk.wallpaperapp.databinding.FragmentMainBinding
import com.skymilk.wallpaperapp.screen.fragment.adapter.ViewPagerAdapter

class MainFragment : Fragment() {

    private val tabTitles = arrayOf("Home", "Popular", "Random", "Categories")

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(
            inflater,  // `layoutInflater` 대신 `inflater`를 사용합니다.
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewPager()
        initTabLayout()
        initToolBar()
    }

    private fun initViewPager() {
        val fragmentActivity = activity as? FragmentActivity
            ?: throw IllegalStateException("Activity must be a FragmentActivity")

        val fragments = listOf(HomeFragment(), PopularFragment(), RandomFragment(), CategoriesFragment())
        val pagerAdapter = ViewPagerAdapter(fragmentActivity, fragments)
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
}