package com.velasteguidaniel.cazarpatos.storage_manager

import android.app.Activity
import android.os.Environment
import com.velasteguidaniel.cazarpatos.Interfaces.FileHandler
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream

class FileExternalManager(val activity: Activity):FileHandler {
    private val file_name:String = "login_credentials.dat"
    private fun isExternalStorageWritable(): Boolean{
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    private fun isExternalStorageReadable():Boolean{
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }

    override fun SaveInformation(datosAGrabar: Pair<String, String>) {
        if (isExternalStorageWritable()){
            FileOutputStream(
                File(
                    activity.getExternalFilesDir(null),
                    this.file_name
                )
            ).bufferedWriter().use { outputStream ->
                outputStream.write(datosAGrabar.first)
                outputStream.write(System.lineSeparator())
                outputStream.write(datosAGrabar.second)
            }
        }
    }

    override fun ReadInformation(): Pair<String, String> {
        var email = ""
        var password = ""
        try {
            if (isExternalStorageReadable()){
                FileInputStream(
                    File(
                        activity.getExternalFilesDir(null),
                        this.file_name
                    )
                ).bufferedReader().use {
                    val userCredentials = it.readText()
                    val credentials_array = userCredentials.split(System.lineSeparator())
                    email = credentials_array[0]
                    password = credentials_array[1]
                }
            }
        }catch (e: FileNotFoundException){

        }

        return email to password
    }
}