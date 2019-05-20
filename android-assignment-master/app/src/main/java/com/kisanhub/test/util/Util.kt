package com.kisanhub.test.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.kisanhub.test.App

class Util {
    companion object {
        @JvmStatic
        fun getMonthString(month: Int?): CharSequence? {
            return when (month) {
                1 -> "Jan"
                2 -> "Feb"
                3 -> "Mar"
                4 -> "Apr"
                5 -> "May"
                6 -> "Jun"
                7 -> "Jul"
                8 -> "Aug"
                9 -> "Sep"
                10 -> "Oct"
                11 -> "Nov"
                12 -> "Dec"
                else -> null
            }
        }


        fun isNetworkAvailable(): Boolean {
            val connectivityManager = App.applicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)
            return if (connectivityManager is ConnectivityManager) {
                val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
                networkInfo?.isConnected ?: false
            } else false
        }
    }

}