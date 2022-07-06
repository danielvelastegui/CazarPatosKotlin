package com.velasteguidaniel.cazarpatos.storage_manager

import android.app.Activity
import android.content.Context
import androidx.security.crypto.MasterKeys
import com.velasteguidaniel.cazarpatos.Interfaces.FileHandler
import com.velasteguidaniel.cazarpatos.LOGIN_KEY
import com.velasteguidaniel.cazarpatos.PASSWORD_KEY

class SharedPreferencesManager(val activity: Activity): FileHandler {
    override fun SaveInformation(datosAGrabar: Pair<String, String>) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        sharedPref.edit()
            .putString(LOGIN_KEY, datosAGrabar.first)
            .putString(PASSWORD_KEY, datosAGrabar.second)
            .apply()
    }

    override fun ReadInformation(): Pair<String, String> {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        val email = sharedPref.getString(LOGIN_KEY, "").toString()
        val password = sharedPref.getString(PASSWORD_KEY, "").toString()
        return (email to password)
    }

}