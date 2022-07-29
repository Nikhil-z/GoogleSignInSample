/*
 * *
 *  * Created by Nikhil on 21/07/22, 11:13 AM
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 21/07/22, 11:13 AM
 *
 */

package com.googlesignin.repository.network

import okio.IOException
import retrofit2.Response
import timber.log.Timber

open class BaseRepository {

    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>, errorMessage: String): T? {

        val result: Result<T> = safeApiResult(call, errorMessage)
        var data: T? = null

        when (result) {
            is Result.Success ->
                data = result.data
            is Result.Error -> {
                Timber.d("DataRepository", "$errorMessage & Exception - ${result.exception}")
            }
        }


        return data

    }

    private suspend fun <T : Any> safeApiResult(
        call: suspend () -> Response<T>,
        errorMessage: String
    ): Result<T> {
        val response = call.invoke()
        if (response.isSuccessful) return Result.Success(response.body()!!)

        return Result.Error(IOException("Error Occurred during getting safe Api result, Custom ERROR - $errorMessage"))
    }
}