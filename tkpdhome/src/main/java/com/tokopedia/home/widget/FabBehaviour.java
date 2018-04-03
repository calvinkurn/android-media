package com.tokopedia.home.widget;

import android.animation.Animator;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by errysuprayogi on 4/3/18.
 */

public class FabBehaviour extends CoordinatorLayout.Behavior<FloatingTextButton> {
    private static final String TAG = "ScrollingFABBehavior";

    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    private static final Long DURATION = 250L;
    private ViewPropertyAnimatorCompat animation = null;

    Handler mHandler;
    private boolean animationStart;
    public FabBehaviour(Context context, AttributeSet attrs) {
        super();
    }

    public FabBehaviour() {
        super();
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                   @NonNull final FloatingTextButton child, @NonNull View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type);
        if (mHandler == null)
            mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showView(child);
                Log.d("FabAnim", "startHandler()");
            }
        }, 3000);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                               @NonNull FloatingTextButton child, @NonNull View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        if (dyConsumed > 0 && !animationStart) {
            hideView(child);
        } else if (dyConsumed < 0 && !animationStart) {
            showView(child);
        }
    }

    private void showView(@NonNull FloatingTextButton child) {
        child.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2))
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        animationStart = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        animationStart = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                        animationStart = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .setDuration(DURATION)
                .start();
    }

    private void hideView(@NonNull FloatingTextButton child) {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        int fab_bottomMargin = layoutParams.bottomMargin;
        child.animate().translationY(child.getHeight() + fab_bottomMargin).setInterpolator(new AccelerateInterpolator(2))
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        animationStart = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        animationStart = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                        animationStart = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .setDuration(DURATION)
                .start();
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull FloatingTextButton child, @NonNull View directTargetChild,
                                       @NonNull View target, int axes, int type) {
        if (mHandler != null) {
            mHandler.removeMessages(0);
            Log.d("Scrolling", "stopHandler()");
        }
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public boolean layoutDependsOn(
            CoordinatorLayout parent,
            FloatingTextButton child,
            View dependency
    ) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(
            CoordinatorLayout parent,
            FloatingTextButton child,
            View dependency
    ) {
        if (child.getTranslationY() > 0) {
            return true;
        }
        if (animation != null) {
            animation.cancel();
            animation = null;
        }

        child.setTranslationY(
                Math.min(0f, dependency.getTranslationY() - dependency.getHeight())
        );
        return true;
    }

    @Override
    public void onDependentViewRemoved(
            CoordinatorLayout parent,
            FloatingTextButton child,
            View dependency
    ) {
        if (dependency instanceof Snackbar.SnackbarLayout) {

            animation = ViewCompat.animate(child)
                    .translationY(0f)
                    .setInterpolator(INTERPOLATOR)
                    .setDuration(DURATION);

            animation.start();
        }
        super.onDependentViewRemoved(parent, child, dependency);
    }

}