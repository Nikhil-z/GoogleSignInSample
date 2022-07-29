package com.googlesignin.repository.network.model

import com.googlesignin.AppConstants.CLIENT_SECRET
import com.googlesignin.AppConstants.REQUEST_ID_TOKEN


/**
 * @Author: Nikhil
 * @Date: 21,July,2022
 */

data class AuthRequest(
    val grant_type: String = "authorization_code",
    val client_id: String = REQUEST_ID_TOKEN,
    val client_secret: String = CLIENT_SECRET,
    val redirect_uri: String = "",
    var code: String = ""
)
