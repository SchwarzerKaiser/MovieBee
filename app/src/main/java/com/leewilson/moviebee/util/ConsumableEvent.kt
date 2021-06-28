package com.leewilson.moviebee.util

class ConsumableEvent<T>(private val content: T) {

    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content

    override fun toString(): String {
        return "Event(content=$content,hasBeenHandled=$hasBeenHandled)"
    }

    companion object {

        fun <T> dataEvent(data: T?): ConsumableEvent<T>?{
            data?.let {
                return ConsumableEvent(it)
            }
            return null
        }

        fun messageEvent(message: String?): ConsumableEvent<String>? {
            message?.let{
                return ConsumableEvent(message)
            }
            return null
        }
    }
}