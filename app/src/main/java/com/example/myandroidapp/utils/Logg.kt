package com.example.myandroidapp.utils

import android.util.Log
import com.example.myandroidapp.BuildConfig

/**
 * Created by Dipen Jansari
 */
object Logg {
    private val showLogs = BuildConfig.DEBUG

    fun e(logTAG: String, msg: String?) {
        if (showLogs) {
            Log.e(logTAG, msg ?: "string is null or empty")
        }
    }

    fun d(logTAG: String, msg: String?) {
        if (showLogs) {
            Log.d(logTAG, msg ?: "string is null or empty")
        }
    }

    fun i(logTAG: String, msg: String?) {
        if (showLogs) {
            Log.i(logTAG, msg ?: "string is null or empty")
        }
    }

    fun v(logTAG: String, msg: String?) {
        if (showLogs) {
            Log.v(logTAG, msg ?: "string is null or empty")
        }
    }

    fun w(logTAG: String, msg: String?) {
        if (showLogs) {
            Log.w(logTAG, msg ?: "string is null or empty")
        }
    }

    fun wtf(logTAG: String, msg: String?) {
        if (showLogs) {
            Log.wtf(logTAG, msg)
        }
    }
}
