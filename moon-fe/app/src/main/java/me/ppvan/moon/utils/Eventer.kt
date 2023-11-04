package me.ppvan.moon.utils

import android.util.Log

typealias EventSubscriber<T> = (T) -> Unit
typealias EventUnsubscribeFn = () -> Unit

class Eventer<T> {
    private val subscribers = mutableListOf<EventSubscriber<T>>()

    fun subscribe(subscriber: EventSubscriber<T>): EventUnsubscribeFn {
        Log.i("INFO", "SUb")
        subscribers.add(subscriber)
        return { unsubscribe(subscriber) }
    }

    fun unsubscribe(subscriber: EventSubscriber<T>) {
        subscribers.remove(subscriber)
    }

    fun dispatch(event: T) {
        subscribers.forEach { it(event); Log.i("INFO", "DP") }
    }

    companion object {
        fun nothing() = Eventer<Nothing?>()
    }
}