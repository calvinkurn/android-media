package com.tokopedia.tkpdpdp.listener

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.widget.ViewGroupUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.tokopedia.tkpdpdp.R

class CodBehavior : CoordinatorLayout.Behavior<View>{
    private var mTempRect: Rect? = null
    private var toolbarHeight = 0

    constructor(): super()

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    override fun layoutDependsOn(parent: CoordinatorLayout?, child: View?, dependency: View?): Boolean {
        return dependency is AppBarLayout
    }

    @SuppressLint("RestrictedApi")
    override fun onDependentViewChanged(parent: CoordinatorLayout?, child: View?, dependency: View?): Boolean {
        if (dependency == null)
            return false

        if (child?.visibility == View.GONE)
            return false


        if (mTempRect == null)
            mTempRect = Rect()

        if (toolbarHeight == 0) {
            val styledAttributes = dependency.context.theme.obtainStyledAttributes(
                    intArrayOf( android.R.attr.actionBarSize ))
            toolbarHeight = styledAttributes.getDimensionPixelSize(0, 0)
        }

        val rect = mTempRect!!
        ViewGroupUtils.getDescendantRect(parent, dependency, rect)

        if (rect.bottom <= toolbarHeight + (child?.height ?: 0)){
            child?.visibility = View.INVISIBLE
        } else {
            child?.visibility = View.VISIBLE
        }

        return true
    }
}