package com.utn_frba_mobile_2020_c2.safeout.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.utn_frba_mobile_2020_c2.safeout.R
import kotlinx.android.synthetic.main.fragment_home.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }


       override fun onCreateView(
           inflater: LayoutInflater, container: ViewGroup?,
           savedInstanceState: Bundle?
       ): View? {
           // Inflate the layout for this fragment

           val view = inflater.inflate(R.layout.fragment_home, container, false)
           val placeListFragment = (PlaceListFragment())
           //val qrScannerFragment = (QrScannerFragment())

           view.buttonListar.setOnClickListener(){

               val fragmentTransaction = this.fragmentManager?.beginTransaction()
               if (fragmentTransaction != null) {
                   fragmentTransaction.replace(R.id.frameLayout, placeListFragment)
                   //fragmentTransaction.replace(R.id.frameLayout, qrScannerFragment)
               }
               if (fragmentTransaction != null) {
                   fragmentTransaction.commit()
               }
               //this.fragmentManager?.beginTransaction()?.replace(R.id.container, fragment)?.commit()
           }
           view.buttonCheckin.setOnClickListener(){
               val fragmentTransaction = this.fragmentManager?.beginTransaction()
               if (fragmentTransaction != null) {
                   fragmentTransaction.replace(R.id.frameLayout, QrScannerFragment(), "QrScannerFragment")
                   fragmentTransaction.addToBackStack("QrScannerFragment")
               }
               if (fragmentTransaction != null) {
                   fragmentTransaction.commit()
               }
           }
           return view
       }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}