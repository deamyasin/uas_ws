package com.foodorder.models

import com.foodorder.R
import java.io.Serializable

/**
 * Created on 04-04-2020.
 */
class SideMenuModel : Serializable {

    var icon: Int = R.drawable.ic_user
    var title: String = ""
    var extra: String = ""
    var isEnable: Boolean = false
    var isSelected: Boolean = false
    var isChecked: Boolean = false

    constructor()

    constructor(icon: Int, title: String, extra: String, isEnable: Boolean, isSelected: Boolean) {
        this.icon = icon
        this.title = title
        this.extra = extra
        this.isEnable = isEnable
        this.isSelected = isSelected
    }

    constructor(
        icon: Int,
        title: String,
        extra: String,
        isEnable: Boolean,
        isSelected: Boolean,
        isChecked: Boolean
    ) {
        this.icon = icon
        this.title = title
        this.extra = extra
        this.isEnable = isEnable
        this.isSelected = isSelected
        this.isChecked = isChecked
    }

}