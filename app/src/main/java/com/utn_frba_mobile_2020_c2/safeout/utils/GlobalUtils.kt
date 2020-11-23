package com.utn_frba_mobile_2020_c2.safeout.utils

import com.google.gson.JsonObject
import com.utn_frba_mobile_2020_c2.safeout.activities.DrawerActivity

object GlobalUtils {
    var drawerActivity: DrawerActivity? = null
    var checkedInSection: String? = null
    var backStackSize = 0
    var arguments: ArrayList<JsonObject?> = ArrayList()
}
