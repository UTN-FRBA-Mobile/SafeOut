package com.utn_frba_mobile_2020_c2.safeout.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.utn_frba_mobile_2020_c2.safeout.R

class NfcFragment :Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.nfc_fragment, container, false)
    }
}
/*  //    Lo dejo comentado, pero no pide permisos NFC. Siempre está seteado con permisos validos para usarlo.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dispatchStoragePermissionRequest(this, android.Manifest.permission.NFC, externalNFCRequestCode)

    }
*/
