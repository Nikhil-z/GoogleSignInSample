package com.googlesignin.repository.network.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey


/**
 * @Author: Nikhil
 * @Date: 25,July,2022
 */
internal object SecuredStorage {


    fun build(context: Context): SharedPreferences {

        return context.getSharedPreferences("MySharedPref", MODE_PRIVATE)
    }

    fun buildSecure(context: Context): SharedPreferences {

        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            "secret_shared_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    }


}