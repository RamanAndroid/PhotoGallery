package com.example.photogallery.screens

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
import androidx.compose.runtime.remember
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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberAsyncImagePainter
import com.example.photogallery.screens.favorite.FavoriteImagesViewModel
import com.example.photogallery.screens.favorite.IntentService
import com.example.photogallery.screens.favorite.Lifecycle

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FavoriteImagesScreen(
    viewModel: FavoriteImagesViewModel = hiltViewModel()
) {
    val images = viewModel.images.collectAsLazyPagingItems()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val lifecycleScope = lifecycleOwner.lifecycleScope
    val color: MutableLiveData<Color> = remember { MutableLiveData(Color.Yellow) }

    lifecycleScope.launchWhenCreated { viewModel.setLifecycle(Lifecycle.CREATE) }
    lifecycleScope.launchWhenStarted { viewModel.setLifecycle(Lifecycle.START) }
    lifecycleScope.launchWhenResumed { viewModel.setLifecycle(Lifecycle.RESUME) }

    val observer = LifecycleEventObserver { source, event ->
        if (event == Event.ON_PAUSE) {
            viewModel.setLifecycle(Lifecycle.PAUSE)

            val intent = Intent(context,IntentService::class.java).apply {
                putExtra(IntentService.DATA_NAME,"Essential information")
            }
            context.startService(intent)
        } else if (event == Event.ON_STOP) {
            viewModel.setLifecycle(Lifecycle.STOP)
        }
    }

    lifecycleOwner.lifecycle.addObserver(observer)

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        stickyHeader {
            when (images.loadState.append) {
                is LoadState.Loading -> color.value = Color.Green
                is LoadState.Error -> color.value = Color.Red
                else -> color.value = Color.Yellow
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(color.value!!)
            )
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