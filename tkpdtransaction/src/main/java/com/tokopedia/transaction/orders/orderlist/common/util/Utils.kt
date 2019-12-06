package com.tokopedia.transaction.orders.orderlist.common.util

import android.content.Context
import com.tokopedia.transaction.R
import java.text.ParseException
import java.text.SimpleDateFormat

object Utils {

    fun convertMonth(num: Int, context: Context): String {
        val entries = context.resources.getStringArray(R.array.month_title)
        return entries[num]
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
