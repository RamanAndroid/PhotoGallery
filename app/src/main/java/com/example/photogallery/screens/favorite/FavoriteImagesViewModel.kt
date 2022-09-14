package com.example.photogallery.screens.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.photogallery.network.model.ImageResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FavoriteImagesViewModel @Inject constructor(
    pagingSource: ImagePagingSource
) : ViewModel() {
    val images: Flow<PagingData<ImageResponse>> = Pager(
        config = PagingConfig(pageSize = 20, prefetchDistance = 2),
        pagingSourceFactory = { pagingSource }
    ).flow.cachedIn(viewModelScope)

    private val _numberLifecycle: MutableLiveData<Lifecycle> = MutableLiveData(Lifecycle.CREATE)
    val numberLifecycle: LiveData<Lifecycle> = _numberLifecycle

    fun setLifecycle(lifecycle: Lifecycle) {
        _numberLifecycle.value = lifecycle
    }

    override fun onCleared() {
        _numberLifecycle.value = Lifecycle.DESTROY
    }
}

enum class Lifecycle {
    CREATE(),
    START(),
    RESUME(),
    PAUSE(),
    STOP(),
    DESTROY(),
}