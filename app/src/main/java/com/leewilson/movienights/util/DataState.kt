package com.leewilson.movienights.util

data class DataState<T>(
    var message: ConsumableEvent<String>? = null,
    var loading: Boolean = false,
    var data: ConsumableEvent<T>? = null
) {

    companion object {

        fun <T> error(message: String): DataState<T> {
            return DataState(
                message = ConsumableEvent(message),
                loading = false,
                data = null
            )
        }

        fun <T> loading(isLoading: Boolean): DataState<T> {
            return DataState(
                message = null,
                loading = isLoading,
                data = null
            )
        }

        fun <T> data(message: String? = null, data: T? = null): DataState<T> {
            return DataState(
                message = ConsumableEvent.messageEvent(message),
                loading = false,
                data = ConsumableEvent.dataEvent(data)
            )
        }
    }
}