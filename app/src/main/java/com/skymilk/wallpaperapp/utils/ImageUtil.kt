package com.skymilk.wallpaperapp.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.skymilk.wallpaperapp.BuildConfig
import java.io.File
import java.io.FileOutputStream

object ImageUtil {
    fun getSavedImages(): List<File> {
        val picturesDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val wallpapersDirectory = File(picturesDirectory, "wallpapers")

        return if (wallpapersDirectory.exists() && wallpapersDirectory.isDirectory) {
            wallpapersDirectory.listFiles { file -> file.isFile && file.extension == "jpg" }
                ?.toList() ?: emptyList()
        } else {
            emptyList()
        }
    }

    fun shareImage(context: Context, bitmap: Bitmap) {
        try {

            // 비트맵을 캐시 디렉토리에 파일로 저장
            val file = File(context.externalCacheDir, File.separator + "image.jpg")
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
            file.setReadable(true, false)

            //저장한 이미지를 공유하기 위해 Intent 구성
            val imageUri = FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID + ".provider",
                file
            )
            val intent = Intent(Intent.ACTION_SEND).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                type = "image/jpg"
                putExtra(Intent.EXTRA_STREAM, imageUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            //이미지 공유 시작
            context.startActivity(Intent.createChooser(intent, "이미지 공유"))


        } catch (e: Exception) {
            e.printStackTrace()

            Toast.makeText(
                context,
                "공유 실패 - ${e.message.toString()}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}