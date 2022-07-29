/*
 * *
 *  * Created by Nikhil on 21/07/22, 12:14 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 20/07/22, 4:12 PM
 *
 */

package com.googlesignin.repository.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.googlesignin.repository.network.Coroutines
import com.googlesignin.repository.network.Resource
import com.googlesignin.repository.network.model.AuthResponse
import com.googlesignin.repository.network.repo.GoogleAccountRepository


/**
 * @Author: Nikhil
 * @Date: 15,July,2022
 */
class MainViewModel : ViewModel() {


    private val googleAccountRepository = GoogleAccountRepository()


    private val _authentication = MutableLiveData<Resource<AuthResponse>>()
    val authentication: LiveData<Resource<AuthResponse>> get() = _authentication




    fun getAuthCode(serverAuthCode: String?) {
        Coroutines.ioThenMain({
            googleAccountRepository.getAuthCode(serverAuthCode)
        }) {
            it.let { auth ->
                if (auth != null) {
                    _authentication.value =Resource.success(auth)

                }

            }
        }
    }


}