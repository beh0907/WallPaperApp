package com.skymilk.wallpaperapp.util

import android.Manifest.permission.POST_NOTIFICATIONS
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.os.Build
import com.gun0912.tedpermission.coroutine.TedPermission


object PermissionUtil {

    //특정 권한
    suspend fun requestPermissions(permissions: Array<String>): Boolean {
        try {
            //권한 요청 및 결과 정보 리턴
            return TedPermission.create()
                .setPermissions(*permissions)
                .checkGranted()
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    //특정 권한
    suspend fun requestAllPermissions(): Boolean {
        // SDK 버전에 따라 요청할 권한 결정
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(
                READ_MEDIA_IMAGES,
                POST_NOTIFICATIONS
            )
        } else {
            listOf(
                READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE
            )
        }

        try {
            //권한 요청 및 결과 정보 리턴
            return TedPermission.create()
                .setPermissions(*permissions.toTypedArray())
                .checkGranted()
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    //저장장치 코루틴 권한 요청 처리
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

        try {
            //권한 요청 및 결과 정보 리턴
            return TedPermission.create()
                .setPermissions(*permissions.toTypedArray())
                .checkGranted()
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    //알림 코루틴 권한 요청 처리
    suspend fun requestNotificationPermissions(): Boolean {
        //API 33 미만은 권한이 필요 없기 때문에 허용결과를 리턴한다
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true

        try {
            //권한 요청 및 결과 정보 리턴
            return TedPermission.create()
                .setPermissions(POST_NOTIFICATIONS)
                .checkGranted()
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}