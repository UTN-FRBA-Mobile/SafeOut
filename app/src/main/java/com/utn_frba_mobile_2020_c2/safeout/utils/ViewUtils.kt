package com.utn_frba_mobile_2020_c2.safeout.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.extensions.extend
import com.utn_frba_mobile_2020_c2.safeout.fragments.HomeFragment

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
    fun showAlertDialog(
        context: Context,
        message: String,
        buttonText: String? = null,
        buttonAction: (() -> Unit)? = null,
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.app_name))
        builder.setMessage(message)
        builder.setPositiveButton(buttonText ?: context.getString(R.string.dialog_accept)) { dialog, _ ->
            if (buttonAction != null) {
                buttonAction()
            }
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }
    fun showConfirmationDialog(
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
    private fun pushFragment(
        manager: FragmentManager,
        next: Fragment,
        arguments: JsonObject? = null
    ) {
        val fragmentTransaction = manager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, next)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        GlobalUtils.drawerActivity?.setBackButtonVisible(true)
        GlobalUtils.arguments.add(arguments)
        GlobalUtils.backStackSize += 1
    }
    fun pushFragment(current: Fragment, next: Fragment, arguments: JsonObject? = null) {
        pushFragment(current.fragmentManager!!, next, arguments)
    }
    fun pushFragment(current: AppCompatActivity, next: Fragment, arguments: JsonObject? = null) {
        pushFragment(current.supportFragmentManager!!, next, arguments)
    }
    fun setAppBarTitle(title: String? = null) {
        GlobalUtils.drawerActivity?.setTitle(title)
    }
    fun getArguments(): JsonObject? {
        val index =  GlobalUtils.backStackSize - 1
        return GlobalUtils.arguments[index]
    }
    fun goBack(current: Fragment? = null, arguments: JsonObject? = null) {
        val index = GlobalUtils.backStackSize - 1
        if (index >= 0) {
            GlobalUtils.arguments.removeAt(index)
        }
        GlobalUtils.backStackSize -= 1
        if (GlobalUtils.backStackSize == 0) {
            GlobalUtils.drawerActivity!!.setBackButtonVisible(false)
            setAppBarTitle(null)
        }
        if (arguments != null) {
            val currentArgs = GlobalUtils.arguments[GlobalUtils.backStackSize - 1] ?: JsonObject()
            currentArgs.extend(arguments)
        }
        if (current != null) {
            current.fragmentManager!!.popBackStackImmediate()
        }
    }
    fun resetToHome(current: Fragment? = null,) {
        if (current != null) {
            val fragmentManager = current.fragmentManager!!
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            setAppBarTitle(null)
            GlobalUtils.drawerActivity!!.setBackButtonVisible(false)
            GlobalUtils.drawerActivity!!.setVisibleFragment(HomeFragment())
        }
    }
    fun setCheckedNavItem(id: Int) {
        GlobalUtils.drawerActivity!!.setCheckedItem(id)
    }
}
