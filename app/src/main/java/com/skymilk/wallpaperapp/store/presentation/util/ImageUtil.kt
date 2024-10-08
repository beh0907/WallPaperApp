package com.skymilk.wallpaperapp.store.presentation.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.skymilk.wallpaperapp.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.Locale

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

        // 이미지 파일 확장자 리스트
        val imageExtensions = listOf("jpg", "jpeg", "png", "gif", "bmp", "webp")

        //최신 이미지부터 가져오기 위해 reversed 설정
        return if (wallpapersDirectory.exists() && wallpapersDirectory.isDirectory) {
            wallpapersDirectory.listFiles { file ->
                file.isFile && imageExtensions.contains(
                    file.extension.lowercase(
                        Locale.getDefault()
                    )
                )
            }
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

    //에셋 폴더로부터 특정 이미지 가져오기
    fun getBitmapFromAsset(
        context: Context,
        filePath: String
    ): Bitmap? {
        val assetManager = context.assets
        var inputStream: InputStream? = null

        try {
            inputStream = assetManager.open(filePath)
            return BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    //파일 객체에서 이미지를 가져온다
    fun getBitmapFromCache(file: File): Bitmap? {
        return if (file.exists()) {
            BitmapFactory.decodeFile(file.absolutePath)
        } else {
            null
        }
    }

    //공유 or 배경화면 저장용 임시 이미지 저장
    //프래그먼트 간 BITMAP 공유가 어려워 우회
    suspend fun saveCacheImage(context: Context, bitmap: Bitmap): File {
        // 비트맵을 캐시 디렉토리에 파일로 저장
        val file = withContext(Dispatchers.IO) {
            val file = File(context.externalCacheDir, File.separator + "image.jpg")
            FileOutputStream(file).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
            }
            file
        }

        return file
    }


    //이미지 공유 처리
    fun shareImage(context: Context, file: File) {
        try {
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
            MessageUtil.showToast(context, "공유 실패 - ${e.message.toString()}")
        }
    }

    //특정 경로의 이미지 파일 제거
    suspend fun deleteImageFile(filePath: String): Boolean {
        return withContext(Dispatchers.IO) {
            // 경로와 파일명을 결합하여 전체 파일 경로를 생성
            val file = File(filePath)

            // 파일이 존재하는지 확인하고 삭제
            if (file.exists()) {
                file.delete()
            } else {
                false
            }
        }
    }
}