package com.tokopedia.seller.lib.widget;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;


public class QuickReturnHeaderBehavior extends QuickReturnFooterBehavior {

    public QuickReturnHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected ObjectAnimator getObjectAnimatorHideView(View child) {
        return ObjectAnimator.ofFloat(child, "translationY", 0, -childHeight);
    }

    protected ObjectAnimator getObjectAnimatorShowView(View child) {
        return ObjectAnimator.ofFloat(child, "translationY", -childHeight, 0);
    }
}