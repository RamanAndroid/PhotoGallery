package com.example.photogallery.pattern.observable

interface IObserver {
    interface Publisher {
        fun register(subscriber: InternetStateSubscriber)
        fun remove(subscriber: InternetStateSubscriber)
    }

    interface InternetStateSubscriber {
        fun update(internetState: Boolean)
    }
}