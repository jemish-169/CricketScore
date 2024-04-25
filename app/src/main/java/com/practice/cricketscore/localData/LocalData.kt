package com.practice.cricketscore.localData

import android.content.Context
import android.content.SharedPreferences
import com.practice.cricketscore.utils.Constants

class LocalData {
    companion object {
        private lateinit var sharedPref: SharedPreferences
        fun getInstance(context: Context): SharedPreferences {
            if (!Companion::sharedPref.isInitialized) sharedPref =
                context.getSharedPreferences(Constants.USER_NAME, Context.MODE_PRIVATE)
            return sharedPref
        }

        fun getUserName(): String {
            return sharedPref.getString(Constants.USER_NAME, "").toString()
        }

        fun setUserName(userName: String) {
            sharedPref.edit().putString(Constants.USER_NAME, userName).apply()
        }
    }
}