package com.skymilk.wallpaperapp.store.presentation.util

import android.content.Context
import com.skymilk.wallpaperapp.R

object EmojiUtil {

    //리소스 이모지 정보를 반영해 리턴
    fun getEmojis(context: Context): List<String> =
        context.resources.getStringArray(R.array.photo_editor_emoji)
            .map { convertEmoji(it) }

    //이모지 변환 처리
    private fun convertEmoji(emoji: String): String {
        return try {
            val convertEmojiToInt = emoji.substring(2).toInt(16)
            String(Character.toChars(convertEmojiToInt))
        } catch (e: NumberFormatException) {
            ""
        }
    }
}