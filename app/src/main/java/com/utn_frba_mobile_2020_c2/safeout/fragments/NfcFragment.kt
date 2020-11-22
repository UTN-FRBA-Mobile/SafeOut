package com.utn_frba_mobile_2020_c2.safeout.fragments

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.utils.GlobalUtils
import com.utn_frba_mobile_2020_c2.safeout.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.fragment_my_reservations.view.*

class NfcFragment :Fragment() {
    lateinit var nfcAdapter: NfcAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        ViewUtils.setAppBarTitle(context!!.getString(R.string.check_in))
        return inflater.inflate(R.layout.nfc_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this.context)

        if (nfcAdapter.isEnabled){
            println("estaba activado")
        }else{
            val builder = AlertDialog.Builder(this.context!!)
            builder.setTitle("NFC desactivado")
            builder.setCancelable(true)
            builder.setMessage("Es necesario la activación del NFC para realizar el checkin")
            builder.setPositiveButton("Activar") { _, _ ->
                startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
            }
            builder.setNegativeButton("No Activar"){ _, _ ->
                val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
                fragmentTransaction?.replace(R.id.frameLayout, HomeFragment())
                fragmentTransaction?.commit()
            }
            builder.show()
        }
    }
}


/*  //    Lo dejo comentado, pero no pide permisos NFC. Siempre está seteado con permisos validos para usarlo.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dispatchStoragePermissionRequest(this, android.Manifest.permission.NFC, externalNFCRequestCode)
    }
*/
