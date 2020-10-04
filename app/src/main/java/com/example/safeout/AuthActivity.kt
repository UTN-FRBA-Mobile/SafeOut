package com.example.safeout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        Thread.sleep(5000) // Esto es para poder ver la pantalla de carga unos segundos. Despues quitar.
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        setup()
    }
    private fun setup(){

        title = "Autenticación"

        botonRegistro.setOnClickListener {
            if (editTextEmail.text.isNotEmpty() && editTextPassword.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(editTextEmail.text.toString(),
                                                                          editTextPassword.text.toString()).addOnCompleteListener {

                        if (it.isSuccessful) {
                            mostrarHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                        } else {
                            mostrarAlerta()
                        }
                }
            }
        }

        botonIngreso.setOnClickListener {
            if (editTextEmail.text.isNotEmpty() && editTextPassword.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(editTextEmail.text.toString(),
                    editTextPassword.text.toString()).addOnCompleteListener {

                    if (it.isSuccessful) {
                        mostrarHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else {
                        mostrarAlerta()
                    }
                }
            }
        }

    }
    private fun mostrarAlerta() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error al autenticar al Usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun mostrarHome(email: String, provider: ProviderType){

        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }
}