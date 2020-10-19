package com.utn_frba_mobile_2020_c2.safeout.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.*
import com.utn_frba_mobile_2020_c2.safeout.R
import kotlinx.android.synthetic.main.qr_scanner_fragment.*

class QrScannerFragment : Fragment() {
        private lateinit var codeScanner: CodeScanner
        val MY_CAMERA_PERMISSION_REQUEST = 1111
        private var mPermissionGranted = false

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.qr_scanner_fragment, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
            val activity = requireActivity()
            codeScanner = CodeScanner(activity, scannerView)
            codeScanner.decodeCallback = DecodeCallback {
                activity.runOnUiThread {
                    Toast.makeText(activity, "Result: ${it.text}", Toast.LENGTH_LONG).show()
                }
            }
            codeScanner.errorCallback = ErrorCallback {
                activity.runOnUiThread {
                    Toast.makeText(activity, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                }
            }
            checkPermission();

            scannerView.setOnClickListener {
                codeScanner.startPreview()
            }
        }

    fun checkPermission(){
        val activity = requireActivity()
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CAMERA),
                MY_CAMERA_PERMISSION_REQUEST
            )
        } else {
            mPermissionGranted = true
            codeScanner.startPreview()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==MY_CAMERA_PERMISSION_REQUEST&&grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            mPermissionGranted = true
            codeScanner.startPreview()
        }else{
            val activity = requireActivity()
            Toast.makeText(activity, "Permission denied", Toast.LENGTH_LONG).show()
        }

    }

        override fun onResume() {
            super.onResume()
            if (mPermissionGranted) {
                codeScanner.startPreview()
            }
        }

        override fun onPause() {
            codeScanner.releaseResources()
            super.onPause()
        }
    }
