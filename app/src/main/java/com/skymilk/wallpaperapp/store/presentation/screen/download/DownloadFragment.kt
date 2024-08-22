package com.skymilk.wallpaperapp.store.presentation.screen.download

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.skymilk.wallpaperapp.databinding.FragmentDownloadBinding
import com.skymilk.wallpaperapp.store.presentation.common.BottomSheetFragment
import com.skymilk.wallpaperapp.utils.BlurHashDecoder

class DownloadFragment : Fragment() {

    private lateinit var binding: FragmentDownloadBinding
    private val args: DownloadFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDownloadBinding.inflate(inflater, container, false)

        loadImage(args.imageData[0])
        setClick()

        return binding.root
    }

    private fun loadImage(url:String) {
        val blurHash = BlurHashDecoder.decode(args.imageData[1])

        //선택한 이미지 적용
        Glide.with(this)
            .load(url)
            .centerCrop()
            .placeholder(blurHash?.toDrawable(this.resources))
            .error(blurHash)
            .into(binding.imageDownload)

        //백그라운드 블러 이미지 적용
        binding.layoutConstraint.background = BitmapDrawable(this.resources, blurHash)
    }

    private fun setClick() {

        binding.apply {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }

            btnDownload.setOnClickListener {
                showBottomSheet()
            }
        }
    }

    private fun showBottomSheet() {
        val bottomSheet = BottomSheetFragment(args.imageData[0])
        bottomSheet.show(requireActivity().supportFragmentManager, "bottomSheet")
    }
}