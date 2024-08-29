package com.skymilk.wallpaperapp

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.skymilk.wallpaperapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_WallPaperApp)
        setContentView(binding.root)

        requestPermission()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun requestPermission() {
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {

            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Log.d("권한 요청", "권한 비허용\n$deniedPermissions")
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedMessage("권한을 거부하면 이 서비스를 사용할 수 없습니다.\n\n[설정] > [권한]에서 권한을 켜주세요")
                .setPermissions(READ_MEDIA_IMAGES)
                .check()
        } else {
            TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedMessage("권한을 거부하면 이 서비스를 사용할 수 없습니다.\n\n[설정] > [권한]에서 권한을 켜주세요")
                .setPermissions(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)
                .check()
        }
    }
}