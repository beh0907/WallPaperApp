package com.skymilk.wallpaperapp.screen.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.skymilk.wallpaperapp.R
import com.skymilk.wallpaperapp.databinding.FragmentHomeBinding
import com.skymilk.wallpaperapp.databinding.FragmentRandomBinding

class RandomFragment : Fragment() {

    private lateinit var binding: FragmentRandomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRandomBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }
}