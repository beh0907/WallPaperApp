package com.skymilk.wallpaperapp.util

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.os.Build
import com.gun0912.tedpermission.coroutine.TedPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object  PermissionUtil {

    //권한 요청 처리
    suspend fun requestStoragePermissions(): Boolean {
        // SDK 버전에 따라 요청할 권한 결정
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(
                READ_MEDIA_IMAGES
            )
        } else {
            listOf(
                READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE
            )
        }

        return withContext(Dispatchers.IO) {
            try {
                //권한 요청 및 결과 정보 가져오기
                val result = TedPermission.create()
                    .setPermissions(*permissions.toTypedArray())
                    .check()

                //권한 허용 여부 리턴
                result.isGranted
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}