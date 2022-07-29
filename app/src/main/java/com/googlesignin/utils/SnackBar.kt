package com.googlesignin.utils

import android.app.Activity
import com.google.android.material.snackbar.Snackbar


/**
 * @Author: Nikhil
 * @Date: 25,July,2022
 */


fun Activity.showSnack(msg: String?) {
    Snackbar.make(
        findViewById(android.R.id.content),
        msg ?: "",
        Snackbar.LENGTH_SHORT
    ).show()
}

fun Activity.SnackBarLong(msg: String?) {
    Snackbar.make(
        findViewById(android.R.id.content),
        msg ?: "",
        Snackbar.LENGTH_LONG
    ).show()
}