package com.kroger.rickyapp.util

/**
 * Wraps around the network Response
 *
 * Handle the loading state
 *
 * Only the defined classes are allowed to inherit form [Resource]
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}
