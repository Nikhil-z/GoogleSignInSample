/*
 * *
 *  * Created by Nikhil on 21/07/22, 12:29 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 21/07/22, 12:29 PM
 *
 */

package com.googlesignin.repository.network

import kotlinx.coroutines.*

object Coroutines {

    fun <T : Any> io(work: suspend (() -> T?)): Job =
        CoroutineScope(Dispatchers.IO).launch {
            work()
        }

    fun <T : Any> ioThenMain(work: suspend (() -> T?), callback: ((T?) -> Unit)? = null): Job =
        CoroutineScope(Dispatchers.Main).launch {
            val data = CoroutineScope(Dispatchers.IO).async {
                return@async work()
            }.await()
            callback?.let {
                it(data)
            }
        }

}