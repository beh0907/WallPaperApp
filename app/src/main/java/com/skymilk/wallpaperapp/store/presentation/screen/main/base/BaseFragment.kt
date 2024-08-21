package com.skymilk.wallpaperapp.store.presentation.screen.main.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<viewBinding : ViewBinding>(
    private val layoutInflater: (inflater: LayoutInflater) -> viewBinding
) : Fragment() {

    private var _binding: viewBinding? = null
    val binding: viewBinding
        get() = _binding as viewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = layoutInflater.invoke(inflater)

        if (_binding == null)
            throw IllegalStateException("바인딩이 없습니다")

        return binding.root
    }
}