package com.utn_frba_mobile_2020_c2.safeout.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.services.AuthService
import com.utn_frba_mobile_2020_c2.safeout.utils.RequestUtils
import kotlinx.android.synthetic.main.activity_auth.*


class AuthActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        RequestUtils.init(this)
    }

    fun onButtonClicked(view: View) {
        val action: String? = when (view.id) {
            R.id.textViewSignup -> {
                "signup"
            }
            R.id.cardViewLogin -> {
                "login"
            }
            else -> {
                null
            }
        }

        val username = editTextUser.text.toString()
        val password = editTextPassword.text.toString()
        if (username.trim() == "" || password.trim() == "") {
            Toast.makeText(
                applicationContext,
                getString(R.string.auth_error_empty_fields),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        viewContainer.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        AuthService.doAuth(action!!, username, password, { response ->
            println("response: $response")
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }, { _, message ->
            progressBar.visibility = View.GONE
            viewContainer.visibility = View.VISIBLE
            Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        })
    }
}