package com.utn_frba_mobile_2020_c2.safeout.activities

import android.content.Context
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.extensions.hideKeyboard
import com.utn_frba_mobile_2020_c2.safeout.fragments.HomeFragment
import com.utn_frba_mobile_2020_c2.safeout.fragments.PlaceListFragment
import com.utn_frba_mobile_2020_c2.safeout.others.toast
import kotlinx.android.synthetic.main.activity_drawer.*

abstract class BaseActivity : AppCompatActivity() {
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            if (event.action == MotionEvent.ACTION_UP) {
                val v = currentFocus
                if (v is EditText) {
                    val outRect = Rect()
                    v.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                        v.clearFocus()
                        v.hideKeyboard()
                    }
                }
            }
        }, 100)
        return super.dispatchTouchEvent(event)
    }

}