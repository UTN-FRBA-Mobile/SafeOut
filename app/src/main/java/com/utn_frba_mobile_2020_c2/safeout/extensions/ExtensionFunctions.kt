package com.utn_frba_mobile_2020_c2.safeout.extensions

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast

fun Int.isNatural() = this >= 0

fun Activity.toast(mensaje: CharSequence, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, mensaje, duration).show()

fun ViewGroup.inflate(layoutId: Int) = LayoutInflater.from(context).inflate(layoutId, this, false)!!