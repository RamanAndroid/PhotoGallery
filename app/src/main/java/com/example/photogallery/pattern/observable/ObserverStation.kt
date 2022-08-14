package com.example.photogallery.pattern.observable

data class ObserverStation(
    private val subscribers: ArrayList<IObserver.InternetStateSubscriber> = arrayListOf()
) : IObserver.Publisher {

    private var internetState: Boolean = false
        set(value) {
            field = value
            this.notifySubscribers()
        }

    override fun register(subscriber: IObserver.InternetStateSubscriber) {
        subscribers.add(subscriber)
        subscriber.update(internetState)
    }

    override fun remove(subscriber: IObserver.InternetStateSubscriber) {
        subscribers.remove(subscriber)
    }

    private fun notifySubscribers() {
        subscribers.forEach {
            it.update(internetState)
        }
    }

    fun updateData(internetState: Boolean) {
        this.internetState = internetState
    }
}
