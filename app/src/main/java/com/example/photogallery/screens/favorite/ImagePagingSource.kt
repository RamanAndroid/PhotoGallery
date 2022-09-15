package com.example.photogallery.screens.favorite

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.photogallery.network.ImagesApi
import com.example.photogallery.network.model.ImageResponse
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@ViewModelScoped
class ImagePagingSource @Inject constructor(
    private val imagesApi: ImagesApi
) : PagingSource<Int, ImageResponse>() {

    companion object {
        private const val DEFAULT_PAGE_NUMBER = 1
        private const val COUNTER_NUMBER = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ImageResponse>): Int {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(COUNTER_NUMBER) ?: anchorPage?.nextKey?.minus(COUNTER_NUMBER)
        } ?: DEFAULT_PAGE_NUMBER
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageResponse> =
        try {
            val position = params.key ?: DEFAULT_PAGE_NUMBER
            val responses = imagesApi.getImages(position)

            LoadResult.Page(
                data = responses,
                prevKey = if (position == COUNTER_NUMBER) null else position - COUNTER_NUMBER,
                nextKey = if (responses.isEmpty()) null else position + COUNTER_NUMBER
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
}