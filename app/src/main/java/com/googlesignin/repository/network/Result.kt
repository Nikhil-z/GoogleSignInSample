/*
 * *
 *  * Created by Nikhil on 21/07/22, 11:11 AM
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 21/07/22, 11:11 AM
 *
 */

package com.googlesignin.repository.network


/**
 * @Author: Nikhil
 * @Date: 21,July,2022
 */
sealed class Result <out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}