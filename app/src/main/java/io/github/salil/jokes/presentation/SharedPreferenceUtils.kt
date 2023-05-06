package com.example.myweatherapp.helper

import android.content.Context

class SharedPreferenceUtils(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )


    companion object {
        private const val PREFS_NAME = "Jokes"

        @Volatile
        private var instance: SharedPreferenceUtils? = null

        fun getInstance(context: Context): SharedPreferenceUtils? {
            if (instance == null) {
                instance = SharedPreferenceUtils(context)
            }
            return instance
        }
    }

    fun containsKey(key: String) = sharedPreferences.contains(key)


    fun putSharedPref(key:String?,value: List<String>?) {
        val set: MutableSet<String> = LinkedHashSet()
        value?.let { set.addAll(it) }
        sharedPreferences.edit().putStringSet(key, set).commit()
    }

    fun getSharedPref(key: String): MutableList<String> {
        return sharedPreferences.getStringSet(key,null)!!.toMutableList()
    }


}