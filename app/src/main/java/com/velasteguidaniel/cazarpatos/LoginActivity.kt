package com.velasteguidaniel.cazarpatos

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import com.velasteguidaniel.cazarpatos.Interfaces.FileHandler
import com.velasteguidaniel.cazarpatos.storage_manager.EncryptedSharedPreferencesManager
import com.velasteguidaniel.cazarpatos.storage_manager.FileExternalManager
import com.velasteguidaniel.cazarpatos.storage_manager.SharedPreferencesManager

class LoginActivity : AppCompatActivity() {
    lateinit var manejadorArchivo: FileHandler
    lateinit var editTextEmail: EditText
    lateinit var editTextPassword:EditText
    lateinit var buttonLogin: Button
    lateinit var buttonNewUser:Button
    lateinit var mediaPlayer:MediaPlayer
    lateinit var chechBoxRecordarme: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Inicialización de variables
//        manejadorArchivo = SharedPreferencesManager(this) // Datos sin encriptar
//        manejadorArchivo = EncryptedSharedPreferencesManager(this) // Datos encriptados
        manejadorArchivo = FileExternalManager(this) // Archivo externo
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonNewUser = findViewById(R.id.buttonNewUser)
        chechBoxRecordarme = findViewById(R.id.checkBoxRecordarme)
        // Leer datos
        leerDatosDePreferencias()
        //Eventos clic
        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val clave = editTextPassword.text.toString()
            //Validaciones de datos requeridos y formatos
            if(!ValidarDatosRequeridos())
                return@setOnClickListener
            if(chechBoxRecordarme.isChecked){
                guardarDatosEnPreferencias()
            }
            //Si pasa validación de datos requeridos, ir a pantalla principal
            val intencion = Intent(this, MainActivity::class.java)
            intencion.putExtra(EXTRA_LOGIN, email)
            startActivity(intencion)
        }
        buttonNewUser.setOnClickListener{

        }
        mediaPlayer= MediaPlayer.create(this, R.raw.title_screen)
        mediaPlayer.start()
        mediaPlayer.isLooping = true
    }

    private fun ValidarDatosRequeridos():Boolean{
        val email = editTextEmail.text.toString()
        val clave = editTextPassword.text.toString()
        if (email.isEmpty()) {
            editTextEmail.setError("El email es obligatorio")
            editTextEmail.requestFocus()
            return false
        }
        if (clave.isEmpty()) {
            editTextPassword.setError("La clave es obligatoria")
            editTextPassword.requestFocus()
            return false
        }
        if (clave.length < 3) {
            editTextPassword.setError("La clave debe tener al menos 3 caracteres")
            editTextPassword.requestFocus()
            return false
        }
        return true
    }

    private fun leerDatosDePreferencias(){
        val listadoLeido = manejadorArchivo.ReadInformation()
        if (listadoLeido.first != ""){
            chechBoxRecordarme.isChecked = true
        }
        editTextEmail.setText(listadoLeido.first)
        editTextPassword.setText(listadoLeido.second)
    }

    private fun guardarDatosEnPreferencias() {
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()
        val listadoAGrabar:Pair<String, String>
        listadoAGrabar = email to password
        manejadorArchivo.SaveInformation(listadoAGrabar)
    }

    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()
    }
}

