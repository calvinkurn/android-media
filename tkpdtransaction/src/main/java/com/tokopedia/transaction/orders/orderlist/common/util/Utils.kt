package com.tokopedia.transaction.orders.orderlist.common.util

import android.content.Context
import com.tokopedia.transaction.R
import java.text.ParseException
import java.text.SimpleDateFormat

object Utils {

    fun convertMonth(num: Int, context: Context): String {
        val entries = context.resources.getStringArray(R.array.month_title)
        var monthString = ""
        when (num) {
            1 -> monthString = entries[0]
            2 -> monthString = entries[1]
            3 -> monthString = entries[2]
            4 -> monthString = entries[3]
            5 -> monthString = entries[4]
            6 -> monthString = entries[5]
            7 -> monthString = entries[6]
            8 -> monthString = entries[7]
            9 -> monthString = entries[8]
            10 -> monthString = entries[9]
            11 -> monthString = entries[10]
            12 -> monthString = entries[11]
        }
        return monthString
    }


    fun setFormat(target: SimpleDateFormat, current: SimpleDateFormat, value: String): String? {
        var result: String? = null
        try {
            result = target.format(current.parse(value))
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return result

    }
}
