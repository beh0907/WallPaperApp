package com.skymilk.wallpaperapp.store.presentation.screen.home

import com.skymilk.wallpaperapp.databinding.FragmentHomeBinding
import com.skymilk.wallpaperapp.store.presentation.screen.main.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {
}