package com.skymilk.wallpaperapp.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.skymilk.wallpaperapp.BuildConfig
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object ImageUtil {

    //로딩 shimmer 이펙트
    fun getShimmerDrawable(): ShimmerDrawable {
        val shimmer = Shimmer.ColorHighlightBuilder()
            .setBaseColor(Color.parseColor("#F3F3F3"))
            .setBaseAlpha(1f)
            .setHighlightColor(Color.parseColor("#9E9E9E"))
            .setHighlightAlpha(1f)
            .setDropoff(50f)
            .build()

        return ShimmerDrawable().apply {
            setShimmer(shimmer)
        }
    }

    //저장소 폴더에서 이미지 목록 가져오기
    fun getSavedImages(): List<File> {
        val picturesDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val wallpapersDirectory = File(picturesDirectory, "wallpapers")

        //최신 이미지부터 가져오기 위해 reversed 설정
        return if (wallpapersDirectory.exists() && wallpapersDirectory.isDirectory) {
            wallpapersDirectory.listFiles { file -> file.isFile && file.extension == "jpg" }
                ?.toList()?.reversed() ?: emptyList()
        } else {
            emptyList()
        }
    }

    //이미지 경로로부터 URI 정보가져오기
    fun getImageUri(context: Context, imagePath: String): Uri? {
        val file = File(imagePath)
        return if (file.exists()) {
            FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID + ".provider",
                file
            )
        } else {
            null
        }
    }

    //이미지 공유하기
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

    //에셋 폴더로부터 특정 이미지 가져오기
    fun getBitmapFromAsset(
        context: Context,
        filePath: String
    ): Bitmap? {
        val assetManager = context.assets
        var inputStream:InputStream? = null

        try {
            inputStream = assetManager.open(filePath)
            return BitmapFactory.decodeStream(inputStream)
        }catch (e:Exception){
            e.printStackTrace()
            return null
        }
    }
}