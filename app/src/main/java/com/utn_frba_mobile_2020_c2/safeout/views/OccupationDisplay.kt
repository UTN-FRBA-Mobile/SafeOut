package com.utn_frba_mobile_2020_c2.safeout.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.utn_frba_mobile_2020_c2.safeout.R
import kotlinx.android.synthetic.main.occupation_display.view.*
import kotlin.math.roundToInt

class OccupationDisplay : RelativeLayout {
    private var mInflater: LayoutInflater
    private var initialized = false
    var level = 0
        set(value) {
            field = value
            updateView()
        }

    constructor(context: Context?) : super(context) {
        mInflater = LayoutInflater.from(context)
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        mInflater = LayoutInflater.from(context)
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        mInflater = LayoutInflater.from(context)
        init(attrs)
    }

    private fun init(attrs: AttributeSet? = null) {
        if (attrs != null) {
            mInflater.inflate(R.layout.occupation_display, this, true)
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.OccupationDisplay, 0, 0)
            try {
                this.level = typedArray.getInt(R.styleable.OccupationDisplay_level, 0)
            } finally {
                typedArray.recycle()
            }
            initialized = true
            updateView()
        }
    }

    private fun getResourceForLevel(type: String): Int {
        val string = type == "string"
        return when (level) {
            in 0..14 -> if (string) R.string.occupation_level_1 else R.color.occupation_level_1
            in 15..44 -> if (string) R.string.occupation_level_2 else R.color.occupation_level_2
            in 45..64 -> if (string) R.string.occupation_level_3 else R.color.occupation_level_3
            in 65..74 -> if (string) R.string.occupation_level_4 else R.color.occupation_level_4
            in 75..99 -> if (string) R.string.occupation_level_5 else R.color.occupation_level_5
            100 -> if (string) R.string.occupation_level_6 else R.color.occupation_level_6
            else -> 0
        }
    }

    private fun updateView() {
        if (!initialized || level < 0 || level > 100) {
            return
        }
        textViewLevel.text = context!!.getString(getResourceForLevel("string"))
        val color = getResourceForLevel("color")
        textViewLevel.setTextColor(context.resources.getColor(color, context.theme))
        viewLevelIndicator.backgroundTintList = context.resources.getColorStateList(color, context.theme)
    }

    companion object {
        fun calculateLevel(occupation: Int, capacity: Int): Int {
            return (occupation * 1.0 / capacity * 100).roundToInt()
        }
    }
}
