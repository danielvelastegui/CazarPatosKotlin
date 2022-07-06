package com.velasteguidaniel.cazarpatos.storage_manager

import android.app.Activity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.velasteguidaniel.cazarpatos.Interfaces.FileHandler
import com.velasteguidaniel.cazarpatos.LOGIN_KEY
import com.velasteguidaniel.cazarpatos.PASSWORD_KEY

class EncryptedSharedPreferencesManager(val activity: Activity):FileHandler {
    private val masterKyeAlias: String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val sharedPref = EncryptedSharedPreferences.create(
        "encrypted+shared+preferences",
        masterKyeAlias,
        activity,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    override fun SaveInformation(datosAGrabar: Pair<String, String>) {
        sharedPref.edit()
            .putString(LOGIN_KEY, datosAGrabar.first)
            .putString(PASSWORD_KEY, datosAGrabar.second)
            .apply()
    }

    override fun ReadInformation(): Pair<String, String> {
        val email = sharedPref.getString(LOGIN_KEY, "").toString()
        val password = sharedPref.getString(PASSWORD_KEY, "").toString()
        return email to password
    }
}