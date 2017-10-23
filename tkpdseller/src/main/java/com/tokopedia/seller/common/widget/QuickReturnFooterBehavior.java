package com.tokopedia.seller.common.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;


public class QuickReturnFooterBehavior extends CoordinatorLayout.Behavior<View> {
    protected int mTotalDyDistance;
    protected boolean hide = false;
    protected int childHeight;

    public QuickReturnFooterBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        childHeight = child.getHeight();
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        if (dy > 0 && mTotalDyDistance < 0 || dy < 0 && mTotalDyDistance > 0) {
            mTotalDyDistance = 0;
        }
        mTotalDyDistance += dy;
        if (!hide && mTotalDyDistance > child.getHeight()) {
            hideView(child);
        } else if (hide && mTotalDyDistance < -child.getHeight()) {
            showView(child);
        }
    }

    public void hideView(final View child) {
        ObjectAnimator animator = getObjectAnimatorHideView(child);
        animator.setDuration(300);
        animator.start();
        hide = true;
    }

    public void showView(final View child) {
        ObjectAnimator animator = getObjectAnimatorShowView(child);
        animator.setDuration(300);
        animator.start();
        hide = false;
    }

    protected ObjectAnimator getObjectAnimatorHideView(View child) {
        return ObjectAnimator.ofFloat(child, "translationY", 0, childHeight);
    }

    protected ObjectAnimator getObjectAnimatorShowView(View child) {
        return ObjectAnimator.ofFloat(child, "translationY", childHeight, 0);
    }
}