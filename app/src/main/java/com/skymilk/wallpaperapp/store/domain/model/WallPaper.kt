package com.skymilk.wallpaperapp.store.domain.model

data class WallPaper(
    val count: Int,
    val `data`: List<Data?>?,
    val paggination: Paggination,
    val success: Boolean?
)
