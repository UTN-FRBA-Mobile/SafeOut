package com.utn_frba_mobile_2020_c2.safeout.listeners

import com.utn_frba_mobile_2020_c2.safeout.models.SectionInfo

interface RecyclerSectionListener {
    fun onClick(sections: SectionInfo, position: Int)
}