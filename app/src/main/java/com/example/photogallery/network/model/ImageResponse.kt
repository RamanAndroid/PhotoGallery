package com.example.photogallery.network.model

import com.google.gson.annotations.SerializedName

data class ImageResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("download_url")
    val downloadUrl: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("height")
    val height: Int,
    @SerializedName("width")
    val width: Int
)