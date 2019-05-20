package com.kisanhub.test

import android.app.Application
import android.content.Context
import com.google.gson.GsonBuilder
import com.kisanhub.test.models.Rainfall

/**
 * Singleton class for application level resources
 */

class App: Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: App? = null

        fun applicationContext() : App {
            return instance as App
        }
    }

    override fun onCreate() {
        super.onCreate()
    }

    //Save data in preference for use in offline mode
    fun saveWeatherData(key: String, data: List<Rainfall>) {
        val sharedPref = applicationContext ?.getSharedPreferences(
                getString(R.string.pref_file), Context.MODE_PRIVATE)
                ?: return
        with (sharedPref.edit()) {
            val builder = GsonBuilder()
            val sExposeGson = builder.create()
            val jsonFavorites = sExposeGson.toJson(data)
            putString(key, jsonFavorites)
            commit()
        }
    }

    //Data for offline mode
    fun getWeatherData(key: String) : List<Rainfall>? {
        val sharedPref = applicationContext ?.getSharedPreferences(
                getString(R.string.pref_file), Context.MODE_PRIVATE)
        val data =  sharedPref?.getString(key, null)

        val builder = GsonBuilder()
        val sExposeGson = builder.create()

        var items = sExposeGson.fromJson(data, Array<Rainfall>::class.java)

        if(items != null)
            return items.toList()

        return null;
    }
}