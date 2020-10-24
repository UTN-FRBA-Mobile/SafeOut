package com.utn_frba_mobile_2020_c2.safeout.activities

import android.nfc.NfcAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.utn_frba_mobile_2020_c2.safeout.R
import kotlinx.android.synthetic.main.activity_nfc.*

class NFCActivity : AppCompatActivity() {
    lateinit var nfcAdapter: NfcAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        var res = ""

        if (nfcAdapter != null){
            res = "Tu dispositivo es compatible con NFC"

        }else{
            res = "Tu dispositivo no es compatible con NFC"
        }

        tv_CompatibleNFC.text = res

        if (nfcAdapter.isEnabled){
            res = "NFC activado"
        }else{
            res = "NFC desactivado"
        }

        tv_ActivacionNFC.text = res

        btnCheckin.setOnClickListener {
            val intent = Intent(this, CheckinActivity::class.java)
            startActivity(intent)
        }
    }
}