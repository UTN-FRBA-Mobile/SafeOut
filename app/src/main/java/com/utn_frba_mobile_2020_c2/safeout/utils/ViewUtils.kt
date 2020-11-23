package com.utn_frba_mobile_2020_c2.safeout.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.extensions.extend

object ViewUtils {
    fun showSnackbar(view: View, message: String) {
        val snackbar = Snackbar.make(
            view, message,
            Snackbar.LENGTH_LONG
        ).setAction("Action", null)
        snackbar.setActionTextColor(Color.BLUE)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(Color.LTGRAY)
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.DKGRAY)
        textView.textSize = 16f
        snackbar.show()
    }
    fun showDialog(
        context: Context,
        message: String,
        positiveText: String? = null,
        negativeText: String? = null,
        positiveAction: (() -> Unit)? = null,
        negativeAction: (() -> Unit)? = null,
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.app_name))
        builder.setMessage(message)
        builder.setPositiveButton(positiveText ?: context.getString(R.string.dialog_yes)) { dialog, _ ->
            if (positiveAction != null) {
                positiveAction()
            }
            dialog.dismiss()
        }
        builder.setNegativeButton(negativeText ?: context.getString(R.string.dialog_no)) { dialog, _ ->
            if (negativeAction != null) {
                negativeAction()
            }
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }
    fun pushFragment(current: Fragment, next: Fragment, arguments: JsonObject? = null) {
        val fragmentTransaction = current.fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, next)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        GlobalUtils.drawerActivity?.setBackButtonVisible(true)
        GlobalUtils.arguments.add(arguments)
        GlobalUtils.backStackSize += 1
    }
    fun setAppBarTitle(title: String? = null) {
        GlobalUtils.drawerActivity?.setTitle(title)
    }
    fun createBundle(map: Map<String, String>): Bundle {
        val bundle = Bundle()
        for ((key, value) in map) {
            bundle.putString(key, value)
        }
        return bundle
    }
    fun getArguments(): JsonObject? {
        val index =  GlobalUtils.backStackSize - 1
        return GlobalUtils.arguments[index]
    }
    fun goBack(current: Fragment, arguments: JsonObject? = null) {
        GlobalUtils.arguments.removeAt(GlobalUtils.backStackSize - 1)
        GlobalUtils.backStackSize -= 1
        if (arguments != null) {
            println("arguments from goBack")
            println(arguments)
            val currentArgs = GlobalUtils.arguments[GlobalUtils.backStackSize - 1] ?: JsonObject()
            currentArgs.extend(arguments)
            println(currentArgs)
        }
        current.fragmentManager!!.popBackStackImmediate()
    }
}
