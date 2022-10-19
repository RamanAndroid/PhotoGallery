package com.example.photogallery.screens.favorite

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.LifecycleEventObserver
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberAsyncImagePainter
import com.example.photogallery.MainActivity
import com.example.photogallery.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FavoriteImagesScreen(
    viewModel: FavoriteImagesViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val activity = LocalContext.current as MainActivity
    val text = remember { mutableStateOf(activity.getString(R.string.no_internet_connection)) }
    val images = viewModel.images.collectAsLazyPagingItems()
    val color = remember { mutableStateOf(Color.Yellow) }

    viewModel.internetState.observe(lifecycleOwner) {
        if (it) {
            text.value = activity.getString(R.string.yes_internet_connection)
        } else {
            text.value = activity.getString(R.string.no_internet_connection)
            images.retry()
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            when (event) {
                Event.ON_CREATE -> {
                    viewModel.networkStateReceiver()
                }
                Event.ON_RESUME -> {
                    activity.showMusicTime()
                }
                Event.ON_PAUSE -> {
                    val intent = Intent(activity, IntentService::class.java).apply {
                        putExtra(IntentService.DATA_NAME, "Essential information")
                    }

                    activity.startService(intent)
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        stickyHeader {
            when (images.loadState.append) {
                is LoadState.Loading -> color.value = Color.Yellow
                is LoadState.Error -> color.value = Color.Red
                else -> color.value = Color.Green
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(color.value)
            ) {
                Text(
                    text = text.value, color = Color.Black, modifier = Modifier.align(Center)
                )
            }
        }
        items(images) { image ->
            image?.let {
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        painter = rememberAsyncImagePainter(it.downloadUrl),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                    )
                    Text(
                        text = image.author,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .align(CenterHorizontally)
                    )
                }
            }
        }
    }
}