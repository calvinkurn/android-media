package com.tokopedia.profilecompletion.view.util

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.ProgressBar
import androidx.annotation.RequiresApi

/**
 * Created by stevenfredian on 6/22/17.
 */
class StepProgressBar : ProgressBar {
    var animation: ProgressBarAnimation? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        max = MAX_BAR
        animation = ProgressBarAnimation(this)
    }

    @Synchronized
    override fun setProgress(progress: Int) {
        val newValue = progress * 100
        if (animation == null) {
            super.setProgress(progress)
        } else {
            animation!!.setValue(getProgress(), newValue)
            animation!!.duration = 1000
            startAnimation(animation)
            super.setProgress(newValue)
        }
    }

    @get:Synchronized
    val percentProgress: Int
        get() = super.getProgress() / MAX_BAR

    companion object {
        private const val MAX_BAR = 10000
    }
}