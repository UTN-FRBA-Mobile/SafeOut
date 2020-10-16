package com.utn_frba_mobile_2020_c2.safeout.activities

import android.nfc.NfcAdapter
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

        btnCompatibilidad.setOnClickListener {

            if (nfcAdapter != null){
                res = "El dispositivo es compatible con NFC"
            }else{
                res = "El dispositivo NO es compatible con NFC"
            }

            AlertDialog.Builder(this).setMessage(res).show()
        }

        btnActivo.setOnClickListener {
            if (nfcAdapter.isEnabled){
                res = "NFC activado"
            }else{
                res = "NFC desactivado"
            }

            AlertDialog.Builder(this).setMessage(res).show()
        }
    }
}