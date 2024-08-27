package com.skymilk.wallpaperapp.store.presentation.common

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.ContextCompat.RECEIVER_NOT_EXPORTED
import java.io.File

object ImageDownloadManager {
    fun downloadImageFromUrl(url: String, context: Context, downloadReceiver: BroadcastReceiver) {
        try {
            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

            val imageUrl = Uri.parse(url)
            val request = DownloadManager.Request(imageUrl).apply {
                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                    .setMimeType("image/*")
                    .setAllowedOverRoaming(false)
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setTitle("이미지 다운로드")
                    .setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_PICTURES,
                        File.separator + "wallpapers" + File.separator + "image" + ".jpg"
                    )
            }

            downloadManager.enqueue(request)

            val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            context.registerReceiver(downloadReceiver, filter, Context.RECEIVER_NOT_EXPORTED)

        } catch (e: Exception) {
            e.printStackTrace()

            Toast.makeText(
                context,
                "이미지 다운로드 실패 - ${e.message.toString()}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}