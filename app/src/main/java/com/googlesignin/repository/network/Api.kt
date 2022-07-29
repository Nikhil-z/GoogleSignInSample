/*
 * *
 *  * Created by Nikhil on 21/07/22, 10:45 AM
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 21/07/22, 10:45 AM
 *
 */

package com.googlesignin.repository.network

import com.googlesignin.repository.network.model.AuthRequest
import com.googlesignin.repository.network.model.AuthResponse
import com.googlesignin.repository.network.model.accountlist.PubAccounts
import com.googlesignin.repository.network.model.adunits.AdUnitsResponse
import com.googlesignin.repository.network.model.apps.AppListResponse
import com.googlesignin.repository.network.model.reports.MediationReportResponse
import com.googlesignin.repository.network.model.reports.NetworkReportResponse
import com.google.api.services.admob.v1.model.GenerateMediationReportRequest
import com.google.api.services.admob.v1.model.GenerateNetworkReportRequest
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*


/**
 * @Author: Nikhil
 * @Date: 21,July,2022
 */




interface GoogleAccountApi {
    @POST("v4/token")
    fun authAccess(@Body authRequest: AuthRequest): Deferred<Response<AuthResponse>>
}

