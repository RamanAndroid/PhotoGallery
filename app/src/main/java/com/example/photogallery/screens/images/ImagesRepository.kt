package com.example.photogallery.screens.images

import android.util.Log
import com.example.photogallery.network.ImagesApi
import com.example.photogallery.network.model.ImageResponse
import com.example.photogallery.screens.images.model.ImageSize
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureOverflowStrategy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.AsyncSubject
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.ReplaySubject
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
        val disposable = imagesApi
            .getImagesObservable(1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { Log.d("doImagesOnSubscribe", "Request to the server") }
            .doFinally { Log.d("doImagesFinally", "Response received") }
            .subscribe({ listResponse ->
                listResponse.forEach {
                    Log.d("SubscribeImagesResponse", it.toString())
                }
            }, { error ->
                Log.d("SubscribeImagesThrowable", error.localizedMessage ?: "Not error")
            }, {
                Log.d("SubscribeImagesComplete", "FunctionComplete")
            })
        compositeDisposable.add(disposable)
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
    Преображение ImageResponse в ImageSize и выдает результат в виде Observable с помощью метода flatMap
    Результат выдаётся с каким-то интервалом в зависимости от рандомного числа
    * */
    fun flatMapImageResponseToImageSize(): Observable<ImageResponse> {
        return getFakeObservableImages()
            .subscribeOn(Schedulers.io())
            .flatMap {

                Observable.just(it).delay((0..10).random().toLong(), TimeUnit.SECONDS)
            }
    }

    /*
    Преображение ImageResponse в ImageSize и выдает результат в виде Observable с помощью метода switchMap
    Результат выдаётся с каким-то интервалом в зависимости от рандомного числа
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
    Преображение ImageResponse в ImageSize и выдает результат в виде Observable с помощью метода concatMap
    Результат выдаётся с каким-то интервалом в зависимости от рандомного числа
    * */
    fun concatMapImageResponseToImageSize(): Observable<ImageSize> {
        return getFakeObservableImages()
            .subscribeOn(Schedulers.io())
            .concatMap {
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
    * Соеденение двух выводов Observable с помощью метода merge
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
    Умножение с рандомными цифрами с помощью метода map
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

    /*
    * вывод элементов, но из-за метода debounce придёт последний пришедший элемент за три секунды
    *
    * */
    fun getNumbersDebounce() {
        compositeDisposable.add(
            Observable.just(1, 2, 3, 4, 5)
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

    /*
    * вывод fake observable которые прошли условие в filter
    * */
    fun filterImageResponse() {
        compositeDisposable.add(
            getFakeObservableImages()
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

    /*
    * выводяться только неповторящиеся элементы  fake observables через проверку hashCode и equals
    * */
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

    /*
    * Из-за метода take берёт только первые три элемента из пришедших fake observables
    * */
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

    fun getPublishSubject(): PublishSubject<ImageResponse> {
        return PublishSubject.create()
    }

    fun getReplaySubject(): ReplaySubject<ImageResponse> {
        return ReplaySubject.create()
    }

    fun getBehaviorSubject(): BehaviorSubject<ImageResponse> {
        return BehaviorSubject.create()
    }
    fun getAsyncSubject(): AsyncSubject<ImageResponse> {
        return AsyncSubject.create()
    }

    fun getFirstObserver(): Observer<ImageResponse> {
        return object : Observer<ImageResponse> {
            override fun onSubscribe(d: Disposable) {
                Log.d("SubjectFirst", "onSubscribe")
            }

            override fun onNext(t: ImageResponse) {
                Log.d("SubjectFirst", "onNext")
                Log.d("SubjectFirst", t.toString())
            }

            override fun onError(e: Throwable) {
                Log.d("SubjectFirst", "onError")
                Log.d("SubjectFirst", e.toString())
            }

            override fun onComplete() {
                Log.d("SubjectFirst", "onComplete")
            }

        }
    }

    fun getSecondObserver(): Observer<ImageResponse> {
        return object : Observer<ImageResponse> {
            override fun onSubscribe(d: Disposable) {
                Log.d("SubjectSecond", "onSubscribe")
            }

            override fun onNext(t: ImageResponse) {
                Log.d("SubjectSecond", "onNext")
                Log.d("SubjectSecond", t.toString())
            }

            override fun onError(e: Throwable) {
                Log.d("SubjectSecond", "onError")
                Log.d("SubjectSecond", e.toString())
            }

            override fun onComplete() {
                Log.d("SubjectSecond", "onComplete")
            }

        }
    }

    fun usingSubject() {
        val subject = getBehaviorSubject()
        val observable = flatMapImageResponseToImageSize()
        val observerFirst = getFirstObserver()
        val observerSecond = getSecondObserver()

        subject.subscribe(observerFirst)

        subject.onNext(
            ImageResponse(
                id = "123123",
                author = "Aloha",
                downloadUrl = "https://picsum.photos/id/0/5616/3744",
                url = "https://unsplash.com/photos/yC-Yzbqy7PY",
                height = 200,
                width = 14000
            )
        )

        subject.onNext(
            ImageResponse(
                id = "99",
                author = "Demian",
                downloadUrl = "https://picsum.photos/id/0/5616/3744",
                url = "https://unsplash.com/photos/yC-Yzbqy7PY",
                height = 200,
                width = 333
            )
        )

        subject.onNext(
            ImageResponse(
                id = "242525",
                author = "Privet",
                downloadUrl = "https://picsum.photos/id/0/5616/3744",
                url = "https://unsplash.com/photos/yC-Yzbqy7PY",
                height = 14000,
                width = 14000
            )
        )

        observable
            .subscribe({
                Log.d("ImageResponse", it.toString())
                subject.onNext(it)
            }, {
                subject.onError(it)
            },{
                subject.onComplete()
            })

        subject.subscribe(observerSecond)

        subject.onNext(
            ImageResponse(
                id = "Hello There",
                author = "Victory Vivaldi",
                downloadUrl = "https://picsum.photos/id/0/5616/3744",
                url = "https://unsplash.com/photos/yC-Yzbqy7PY",
                height = 666,
                width = 616
            )
        )
        subject.onError(Throwable("И тут бац!!!"))
    }

    fun clearedCompositeDisposable() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
            compositeDisposable.clear()
        }
    }
}