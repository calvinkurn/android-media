package com.tokopedia.transaction.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

import android.content.Context.CLIPBOARD_SERVICE
import com.tokopedia.transaction.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit


object Utils {
    val VIBRATE_DURATION = 150

    @JvmStatic
    fun copyTextToClipBoard(label: String, textVoucherCode: String, context: Context) {
        val clipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, textVoucherCode)
        clipboard.primaryClip = clip
    }
    @JvmStatic
    fun vibrate(context: Context) {
        val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (v != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(VIBRATE_DURATION.toLong(), VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                v.vibrate(VIBRATE_DURATION.toLong())
            }
        }
    }

    @JvmStatic
    fun convertMonth(num: Int, context: Context): String {
        val entries = context.resources.getStringArray(R.array.month_title)
        return entries[num]
    }

    @JvmStatic
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
