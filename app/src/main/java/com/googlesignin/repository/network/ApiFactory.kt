/*
 * *
 *  * Created by Nikhil on 21/07/22, 11:08 AM
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 21/07/22, 11:08 AM
 *
 */

package com.googlesignin.repository.network

import com.googlesignin.repository.Pref


/**
 * @Author: Nikhil
 * @Date: 21,July,2022
 */
object ApiFactory {


    val auth: GoogleAccountApi = RetrofitFactory.retrofit(Pref.auth)
        .create(GoogleAccountApi::class.java)
}