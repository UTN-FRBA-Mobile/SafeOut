package com.utn_frba_mobile_2020_c2.safeout.activities

import androidx.appcompat.app.AppCompatActivity
import com.utn_frba_mobile_2020_c2.safeout.R
import android.os.Bundle
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.widget.Toast
import com.utn_frba_mobile_2020_c2.safeout.fragments.HomeFragment

class CheckinActivity : AppCompatActivity() {

    private var nfcPendingIntent: PendingIntent? = null
    private var nfcAdapter : NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkin)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcPendingIntent = PendingIntent.getActivity(this, 0,
        Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)

    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, nfcPendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (intent != null) processIntent(intent)

    }

    private fun processIntent(checkIntent: Intent) {
        if (NfcAdapter.ACTION_TAG_DISCOVERED == checkIntent.action){
            // aca va  a logica para registrar la entrada

            Toast.makeText(this, "Check in exitoso, Bienvenido!", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this, "Error, vuelva a intentar", Toast.LENGTH_LONG).show()
        }
        }
}