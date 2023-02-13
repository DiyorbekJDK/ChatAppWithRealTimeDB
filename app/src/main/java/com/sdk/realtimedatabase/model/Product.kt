package com.sdk.realtimedatabase.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val name: String = "",
    val price: Int = 0,
    val oldName: String = ""
): Parcelable
