package com.utn_frba_mobile_2020_c2.safeout.listeners

import com.utn_frba_mobile_2020_c2.safeout.models.Section

interface RecyclerSectionListener {
    fun onClick(section: Section, position: Int)
}