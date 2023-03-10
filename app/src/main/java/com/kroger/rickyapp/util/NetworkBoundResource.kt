package com.kroger.rickyapp.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * fetch - responsible for fetching new data from the api
 * return (RequestType) - Characters
 *
 * [networkBoundResource] takes other functions as inputs
 */
inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    // fetch data from the local cache
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        // Emit a loading state
        emit(Resource.Loading())

        try {
            // make a networking request and save it to the database
            saveFetchResult(fetch())

            // Now fetch from the database again and give it to the UI
            query().map { Resource.Success(it) }
        } catch (throwable: Throwable) {
            // Old cache data
            query().map { Resource.Error(throwable.toString(), it) }
        }
    } else {
        // Query to the the database and give it to the UI
        query().map { Resource.Success(it) }
    }

    emitAll(flow)
}
