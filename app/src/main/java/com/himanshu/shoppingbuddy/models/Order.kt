package com.himanshu.shoppingbuddy.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    val user_id: String = "",
    val items: ArrayList<CartItems> = ArrayList(),
    val address: Address = Address(),
    val title: String = "",
    val image: String = "",
    val sub_total_amount: String = "",
    val shipping_charge: String = "",
    val total_amount: String = "",
    // TODO Step 4: Add the param for order date.
    // START
    val order_datetime: Long = 0L,
    // END
    var id: String = ""
) : Parcelable