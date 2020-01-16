package com.tokopedia.transaction.orders.orderlist.common.util

import java.text.ParseException
import java.text.SimpleDateFormat

object Utils {

    fun convertMonth(num: Int): String {

        var monthString = ""
        when (num) {
            1 -> monthString = "Jan"
            2 -> monthString = "Feb"
            3 -> monthString = "Mar"
            4 -> monthString = "Apr"
            5 -> monthString = "Mei"
            6 -> monthString = "Jun"
            7 -> monthString = "Jul"
            8 -> monthString = "Ags"
            9 -> monthString = "Sep"
            10 -> monthString = "Okt"
            11 -> monthString = "Nov"
            12 -> monthString = "Des"
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
