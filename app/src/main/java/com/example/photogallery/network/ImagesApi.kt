package com.example.photogallery.network

import com.example.photogallery.network.model.ImageResponse
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ImagesApi {

    @GET("v2/list")
    fun getImagesObservable(
        @Query("page")
        page: Int
    ): Observable<List<ImageResponse>>

    @GET("v2/list")
    fun getImagesFlowable(
        @Query("page")
        page: Int
    ): Flowable<List<ImageResponse>>

    @GET("600/1000?random=1")
    fun getImagesSingle(): Single<ImageResponse>

    @GET("v2/list")
    suspend fun getImages(
        @Query("page")
        page: Int
    ): List<ImageResponse>
}