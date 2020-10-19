package com.utn_frba_mobile_2020_c2.safeout.activities


import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.widget.EditText
import com.utn_frba_mobile_2020_c2.safeout.extensions.hideKeyboard


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