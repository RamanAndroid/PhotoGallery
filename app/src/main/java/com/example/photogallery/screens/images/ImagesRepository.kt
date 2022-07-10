package com.example.photogallery.screens.images

import android.util.Log
import com.example.photogallery.network.ImagesApi
import com.example.photogallery.network.model.ImageResponse
import com.example.photogallery.screens.images.model.ImageSize
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureOverflowStrategy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ViewModelScoped
class ImagesRepository
@Inject constructor(private val imagesApi: ImagesApi) {

    private val compositeDisposable = CompositeDisposable()


    /*
     * Стандартная работа с Observable
     *  Подписываемся и отправляем запрос на сервер на потоке в Schedulers.io() чтобы не забивать основной поток
     * А считываем уже полученные данные в AndroidSchedulers.mainThread()
     */
    fun getImagesObservable() {
        compositeDisposable.add(
            imagesApi
                .getImagesObservable(1)
                .doOnSubscribe {
                    Log.d("doImagesOnSubscribe", "Request to the server")
                }
                .doFinally {
                    Log.d("doImagesFinally", "Response received")
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { listResponse ->
                        listResponse.forEach {
                            Log.d("SubscribeImagesResponse", it.toString())
                        }
                    }, {
                        Log.d("SubscribeImagesThrowable", it.localizedMessage ?: "Not error")
                    }, {
                        Log.d("SubscribeImagesComplete", "FunctionComplete")
                    }
                )
        )
    }

    /*
    * Стандартная работа с Flowable
    * Подписываемся и отправляем запрос на сервер на потоке в Schedulers.io() чтобы не забивать основной поток
    * А считываем уже полученные данные в AndroidSchedulers.mainThread()
    * Также к этому добавляется работа с onBackpressure чтобы при большом потоке данных приложение не вылетело
    * */

    fun getImagesFlowable() {
        compositeDisposable.add(
            imagesApi
                .getImagesFlowable(1)
                .onBackpressureBuffer(
                    1024,
                    { Log.d("onBackpressureError", "onBackpressureError") },
                    BackpressureOverflowStrategy.DROP_LATEST
                )
                .doOnSubscribe {
                    Log.d("doImagesOnSubscribe", "Request to the server")
                }
                .doFinally {
                    Log.d("doImagesFinally", "Response received")
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { listResponse ->
                        listResponse.forEach {
                            Log.d("SubscribeImagesResponse", it.toString())
                        }
                    }, {
                        Log.d("SubscribeImagesThrowable", it.localizedMessage ?: "Not error")
                    }, {
                        Log.d("SubscribeImagesComplete", "FunctionComplete")
                    }
                )
        )
    }

    /*
   * Стандартная работа с Single
   * Подписываемся и отправляем запрос на сервер на потоке в Schedulers.single() чтобы не забивать основной поток
   * А считываем уже полученные данные в AndroidSchedulers.mainThread()
   * */
    fun getImageSingle() {
        compositeDisposable.add(
            imagesApi
                .getImagesSingle()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    Log.d("doImageOnSubscribe", "Request to the server")
                }
                .doFinally {
                    Log.d("doImageFinally", "Response received")
                }
                .subscribe({ image ->
                    Log.d("SubscribeImageResponse", image.toString())
                }, {
                    Log.d("SubscribeImageThrowable", it.localizedMessage ?: "Not error")
                })
        )
    }

    /*
    Преображение ImageResponse в ImageSize с помощью метода flatMap
    * */
    fun flatMapImageResponseToImageSize(): Observable<ImageResponse> {
        return getFakeObservableImages()
            .subscribeOn(Schedulers.newThread())
            .flatMap {
                val delay = (0..10).random().toLong()

                Observable.just(it).delay(delay, TimeUnit.SECONDS)
            }
    }

    /*
    Преображение ImageResponse в ImageSize с помощью метода switchMap
    * */
    fun switchMapImageResponseToImageSize(): Observable<ImageSize> {
        return getFakeObservableImages()
            .subscribeOn(Schedulers.io())
            .switchMap {
                Observable.just(
                    ImageSize(
                        id = it.id,
                        width = it.width,
                        height = it.height
                    )
                ).delay((0..10).random().toLong(), TimeUnit.SECONDS)
            }
    }

    /*
    Преображение ImageResponse в ImageSize с помощью метода concatMap
    * */
    fun concatMapImageResponseToImageSize(): Observable<ImageSize> {
        return getFakeObservableImages()
            .subscribeOn(Schedulers.io())
            .switchMap {
                Observable.just(
                    ImageSize(
                        id = it.id,
                        width = it.width,
                        height = it.height
                    )
                ).delay((0..10).random().toLong(), TimeUnit.SECONDS)
            }
    }

    /*
    * Соеденение двух Observable с помощью метода merge
    * */
    fun mergeObservable() {
        compositeDisposable.add(
            Observable
                .merge(
                    listOf(
                        switchMapImageResponseToImageSize(),
                        concatMapImageResponseToImageSize()
                    )
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("SubscribeImagesResponse", it.toString())
                }, {
                    Log.d("SubscribeImagesThrowable", it.localizedMessage ?: "Not error")
                }, {
                    Log.d("SubscribeImagesComplete", "FunctionComplete")
                })
        )
    }

    /*
    Умножение цифр с помощью метода map
    * */
    fun getMultiplyNumbers() {
        compositeDisposable.add(
            Observable.just(1, 2, 3, 4, 5)
                .delay(10, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .map {
                    it * (0..100).random()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("SubscribeNumbersResponse", it.toString())
                }, {
                    Log.d("SubscribeNumbersThrowable", it.localizedMessage ?: "Not error")
                }, {
                    Log.d("SubscribeNumbersComplete", "FunctionComplete")
                })
        )
    }

    /*
    *Сложение чисел с помощью scan
    *  */
    fun getConcatenationNumbers() {
        compositeDisposable.add(
            Observable.just(1, 2, 3, 4, 5)
                .delay(10, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .scan { t1, t2 ->
                    t1 + t2
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("SubscribeNumbersResponse", it.toString())
                }, {
                    Log.d("SubscribeNumbersThrowable", it.localizedMessage ?: "Not error")
                }, {
                    Log.d("SubscribeNumbersComplete", "FunctionComplete")
                })
        )
    }

    fun getNumbersDebounce() {
        compositeDisposable.add(
            Observable.just(1, 2, 3, 4, 5)
                .delay(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .scan { t1, t2 ->
                    t1 + t2
                }
                .debounce(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("SubscribeNumbersResponse", it.toString())
                }, {
                    Log.d("SubscribeNumbersThrowable", it.localizedMessage ?: "Not error")
                }, {
                    Log.d("SubscribeNumbersComplete", "FunctionComplete")
                })
        )
    }

    fun filterImageResponse() {
        compositeDisposable.add(getFakeObservableImages()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .filter { it.height > 1000 }
            .doOnSubscribe {
                Log.d("doImagesOnSubscribe", "Request to the server")
            }
            .doFinally {
                Log.d("doImagesFinally", "Response received")
            }
            .subscribe({
                Log.d("SubscribeNumbersResponse", it.toString())
            }, {
                Log.d("SubscribeNumbersThrowable", it.localizedMessage ?: "Not error")
            }, {
                Log.d("SubscribeNumbersComplete", "FunctionComplete")
            })
        )
    }

    fun distinctImageResponse() {
        compositeDisposable.add(
            getFakeObservableImages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .distinct()
                .doOnSubscribe {
                    Log.d("doImagesOnSubscribe", "Request to the server")
                }
                .doFinally {
                    Log.d("doImagesFinally", "Response received")
                }
                .subscribe({
                    Log.d("SubscribeNumbersResponse", it.toString())
                }, {
                    Log.d("SubscribeNumbersThrowable", it.localizedMessage ?: "Not error")
                }, {
                    Log.d("SubscribeNumbersComplete", "FunctionComplete")
                })
        )
    }

    fun takeImageResponse() {
        compositeDisposable.add(
            getFakeObservableImages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .take(3)
                .doOnSubscribe {
                    Log.d("doImagesOnSubscribe", "Request to the server")
                }
                .doFinally {
                    Log.d("doImagesFinally", "Response received")
                }
                .subscribe({
                    Log.d("SubscribeNumbersResponse", it.toString())
                }, {
                    Log.d("SubscribeNumbersThrowable", it.localizedMessage ?: "Not error")
                }, {
                    Log.d("SubscribeNumbersComplete", "FunctionComplete")
                })
        )
    }

    /*
   * Соеденение двух Observable с помощью метода zip
   * */
    fun zipWithObservable() {
        concatMapImageResponseToImageSize()
            .zipWith(switchMapImageResponseToImageSize())
            { t1, t2 ->
                t1.width + t2.width
            }
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("SubscribeNumbersResponse", it.toString())
            }, {
                Log.d("SubscribeNumbersThrowable", it.localizedMessage ?: "Not error")
            }, {
                Log.d("SubscribeNumbersComplete", "FunctionComplete")
            })
    }

    fun clearedCompositeDisposable() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
            compositeDisposable.clear()
        }
    }

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
}