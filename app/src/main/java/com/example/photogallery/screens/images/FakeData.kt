package com.example.photogallery.screens.images

import com.example.photogallery.network.model.ImageResponse
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject

fun getFakeObservableImages(): Observable<ImageResponse> {
    return Observable.just(
        ImageResponse(
            id = "0",
            author = "Alejandro Escamilla",
            downloadUrl = "https://picsum.photos/id/0/5616/3744",
            url = "https://unsplash.com/photos/yC-Yzbqy7PY",
            height = 3744,
            width = 616
        ),
        ImageResponse(
            id = "1",
            author = "Alejandro Escamilla",
            downloadUrl = "https://picsum.photos/id/0/5616/3744",
            url = "https://unsplash.com/photos/yC-Yzbqy7PY",
            height = 374,
            width = 5616
        ),
        ImageResponse(
            id = "2",
            author = "Alejandro Escamilla",
            downloadUrl = "https://picsum.photos/id/0/5616/3744",
            url = "https://unsplash.com/photos/yC-Yzbqy7PY",
            height = 3744,
            width = 616
        ),
        ImageResponse(
            id = "3",
            author = "Alejandro Escamilla",
            downloadUrl = "https://picsum.photos/id/0/5616/3744",
            url = "https://unsplash.com/photos/yC-Yzbqy7PY",
            height = 374,
            width = 5616
        ),
        ImageResponse(
            id = "4",
            author = "Alejandro Escamilla",
            downloadUrl = "https://picsum.photos/id/0/5616/3744",
            url = "https://unsplash.com/photos/yC-Yzbqy7PY",
            height = 744,
            width = 566
        ),
        ImageResponse(
            id = "5",
            author = "Alejandro Escamilla",
            downloadUrl = "https://picsum.photos/id/0/5616/3744",
            url = "https://unsplash.com/photos/yC-Yzbqy7PY",
            height = 744,
            width = 561
        ),
    )
}
