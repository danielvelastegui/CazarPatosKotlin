package com.velasteguidaniel.cazarpatos

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.velasteguidaniel.cazarpatos.Interfaces.FileHandler
import com.velasteguidaniel.cazarpatos.storage_manager.FileExternalManager

class LoginActivity : AppCompatActivity() {
    lateinit var manejadorArchivo: FileHandler
    lateinit var editTextEmail: EditText
    lateinit var editTextPassword:EditText
    lateinit var buttonLogin: Button
    lateinit var buttonNewUser:Button
    lateinit var mediaPlayer:MediaPlayer
    lateinit var chechBoxRecordarme: CheckBox
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Inicialización de variables
//        manejadorArchivo = SharedPreferencesManager(this) // Datos sin encriptar
//        manejadorArchivo = EncryptedSharedPreferencesManager(this) // Datos encriptados
        manejadorArchivo = FileExternalManager(this) // Archivo externo
        editTextEmail = findViewById(R.id.editTextEmailRegister)
        editTextPassword = findViewById(R.id.editTextPasswordRegister)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonNewUser = findViewById(R.id.buttonNewUser)
        chechBoxRecordarme = findViewById(R.id.checkBoxRecordarme)
        storage = Firebase.storage
        // Initialize Firebase Auth
        auth = Firebase.auth

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
            AutenticarUsuario(email, clave)
        }
        buttonNewUser.setOnClickListener{
            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registerIntent)
        }
        mediaPlayer= MediaPlayer.create(this, R.raw.title_screen)
        mediaPlayer.start()
        mediaPlayer.isLooping = true
    }

    fun AutenticarUsuario(email:String, password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(EXTRA_LOGIN, "signInWithEmail:success")
                    //Si pasa validación de datos requeridos, ir a pantalla principal
                    val intencion = Intent(this, MainActivity::class.java)
                    intencion.putExtra(EXTRA_LOGIN, auth.currentUser!!.email)
                    startActivity(intencion)
                    //finish()
                } else {
                    Log.w(EXTRA_LOGIN, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, task.exception!!.message,
                        Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun ValidarEmail(email:String):Boolean{
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
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
        if(!ValidarEmail(email)){
            editTextEmail.setError("El email ingresado no es válido")
            editTextEmail.requestFocus()
            return false
        }
        if (clave.length < 8) {
            editTextPassword.setError("La clave debe tener al menos 8 caracteres")
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

