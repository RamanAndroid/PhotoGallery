package com.example.photogallery.screens.favorite

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
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
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteImagesViewModel @Inject constructor(
    pagingSource: ImagePagingSource, @ApplicationContext context: Context
) : ViewModel() {

    val images: Flow<PagingData<ImageResponse>> =
        Pager(config = PagingConfig(pageSize = 20, prefetchDistance = 2),
            pagingSourceFactory = { pagingSource }).flow.cachedIn(viewModelScope)

    private val _internetState: MutableLiveData<Boolean> = MutableLiveData(false)
    val internetState: LiveData<Boolean> = _internetState

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val networkCallback: NetworkCallback = object : NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)

            viewModelScope.launch {
                _internetState.value = true
            }
        }

        override fun onLost(network: Network) {
            super.onLost(network)

            viewModelScope.launch {
                _internetState.value = false
            }
        }
    }

    fun networkStateReceiver() {
        val builder = NetworkRequest.Builder().apply {
            addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        }
        val networkRequest = builder.build()

        connectivityManager.registerNetworkCallback(
            networkRequest, networkCallback
        )
    }

    override fun onCleared() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}