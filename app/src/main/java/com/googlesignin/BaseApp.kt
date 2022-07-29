package com.googlesignin

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import timber.log.Timber


/**
 * @Author: Nikhil
 * @Date: 22,July,2022
 */
class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        if (Timber.treeCount == 0) {
            if (BuildConfig.DEBUG) {
                Timber.plant(Timber.DebugTree())
            } else {
                //Timber.plant(CrashReportingTree(context.packageName))
            }
        }

    }
}