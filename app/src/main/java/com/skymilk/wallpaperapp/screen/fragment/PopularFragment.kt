package com.skymilk.wallpaperapp.screen.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.skymilk.wallpaperapp.R
import com.skymilk.wallpaperapp.databinding.FragmentHomeBinding
import com.skymilk.wallpaperapp.databinding.FragmentPopularBinding

class PopularFragment : Fragment() {

    private lateinit var binding:FragmentPopularBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPopularBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }
}