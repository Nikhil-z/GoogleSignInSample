/*
 * *
 *  * Created by Nikhil on 21/07/22, 11:14 AM
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 21/07/22, 11:14 AM
 *
 */

package com.googlesignin.repository.network

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

data class Resource<out T>(val status: Status, val data: T?, var message: String) {

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, "REST API communication was successful")
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, "REST API request in progress")
        }
    }
}