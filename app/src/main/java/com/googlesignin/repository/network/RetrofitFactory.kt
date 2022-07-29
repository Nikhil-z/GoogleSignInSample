/*
 * *
 *  * Created by Nikhil on 21/07/22, 10:49 AM
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 21/07/22, 10:49 AM
 *
 */

package com.googlesignin.repository.network

import com.googlesignin.BuildConfig
import com.googlesignin.repository.Pref
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * @Author: Nikhil
 * @Date: 21,July,2022
 */
object RetrofitFactory {

    private val authInterceptor = Interceptor { chain ->

        val accessToken = Pref.token

        val newUrl = chain.request().url
            .newBuilder()
            //.addQueryParameter("api_key", AppConstants.tmdbApiKey)
            .build()

        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl)
            //  .addHeader("Authorization", "Bearer $accessToken")
            .build()

        chain.proceed(newRequest)
    }


    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val headersInterceptor = Interceptor { chain ->
        chain.proceed(
            chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build()
        )
    }

    //Not logging the authkey if not debug
    private val client =
        if (BuildConfig.DEBUG) {
            OkHttpClient().newBuilder()
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(headersInterceptor)
                .build()
        } else {
            OkHttpClient().newBuilder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .addInterceptor(headersInterceptor)
                .build()
        }


    fun retrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        //.addConverterFactory(MoshiConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
}