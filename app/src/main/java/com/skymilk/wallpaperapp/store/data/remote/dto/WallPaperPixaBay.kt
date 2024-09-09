package com.skymilk.wallpaperapp.store.data.remote.dto


import com.skymilk.wallpaperapp.store.domain.model.Hit
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WallPaperPixaBay(
    @SerialName("hits")
    val hits: List<Hit>,
    @SerialName("total")
    val total: Int,
    @SerialName("totalHits")
    val totalHits: Int
)