package com.utn_frba_mobile_2020_c2.safeout.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.controllers.AuthController
import com.utn_frba_mobile_2020_c2.safeout.services.CheckinService
import com.utn_frba_mobile_2020_c2.safeout.services.ReservationService
import com.utn_frba_mobile_2020_c2.safeout.utils.DateUtils
import kotlinx.android.synthetic.main.activity_auth.*
import com.utn_frba_mobile_2020_c2.safeout.utils.RequestUtils2
import com.utn_frba_mobile_2020_c2.safeout.utils.StorageUtils


class AuthActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        RequestUtils2.init(this)
        AuthController.init(this)
        StorageUtils.init(this)
        if (AuthController.loggedIn) {
            doAfterLoginSuccess()
        }
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
        setLoading(true)
        AuthController.doAuth(action!!, username, password, {
            doAfterLoginSuccess()
        }, { _, message ->
            setLoading(false)
            Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        })
    }

    private fun displayDrawerActivity() {
        val intent = Intent(this, DrawerActivity::class.java)
        startActivity(intent)
    }

    private fun doAfterLoginSuccess() {
        setLoading(true)
        CheckinService.getCheckedInSection { checkin, _ ->
            StorageUtils.set(StorageUtils.CHECKIN, checkin)
            displayDrawerActivity()
        }
    }

    private fun setLoading(loading: Boolean) {
        if (loading) {
            viewContainer.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
            viewContainer.visibility = View.VISIBLE
        }
    }
}
