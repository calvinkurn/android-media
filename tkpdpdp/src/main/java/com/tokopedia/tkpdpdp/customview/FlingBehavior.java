package com.tokopedia.tkpdpdp.customview;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.ScrollerCompat;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by alifa on 11/6/17.
 */

public class FlingBehavior extends AppBarLayout.Behavior {
    private static final String TAG = "SmoothScrollBehavior";
    //The higher this value is, the faster the user must scroll for the AppBarLayout to collapse by itself
    private static final int SCROLL_SENSIBILITY = 7;
    //The real fling velocity calculation seems complex, in this case it is simplified with a multiplier
    private static final int FLING_VELOCITY_MULTIPLIER = 40;

    private boolean alreadyFlung = false;
    private boolean request = false;
    private boolean expand = false;
    private int velocity = 0;
    private int nestedScrollViewId;

    public FlingBehavior(int nestedScrollViewId) {
        this.nestedScrollViewId = nestedScrollViewId;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child,
                                  View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        if(Math.abs(dy) >= SCROLL_SENSIBILITY) {
            request = true;
            expand = dy < 0;
            velocity = dy * FLING_VELOCITY_MULTIPLIER;
        } else {
            request = false;
        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child,
                                       View directTargetChild, View target, int nestedScrollAxes) {
        request = false;
        return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View target) {
        super.onStopNestedScroll(coordinatorLayout, appBarLayout, target);
        if(request) {
            NestedScrollView nestedScrollView = (NestedScrollView) coordinatorLayout.findViewById(nestedScrollViewId);
            int finalY = getPredictedScrollY(nestedScrollView);
            if (expand) {
                if (nestedScrollView.getScrollY() > 0) {
                    if (finalY <= 3) {
                        nestedScrollView.smoothScrollTo(0,0);
                        expandAppBarLayoutWithVelocity(coordinatorLayout, appBarLayout, velocity);
                    }
                }
            } else {
                onNestedFling(coordinatorLayout, appBarLayout, target, 0, velocity, true);
                if(!alreadyFlung) {
                    nestedScrollView.fling(velocity);
                }
            }
        }
        alreadyFlung = false;
    }

    private int getPredictedScrollY(NestedScrollView nestedScrollView) {
        int finalY = 0;
        try {
            Field scrollerField = nestedScrollView.getClass().getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            Object object = scrollerField.get(nestedScrollView);
            ScrollerCompat scrollerCompat = (ScrollerCompat) object;
            finalY = scrollerCompat.getFinalY();
        } catch (Exception e ) {
            e.printStackTrace();
        }
        return finalY;
    }

    private void expandAppBarLayoutWithVelocity(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, float velocity) {
        try {
            Method animateOffsetTo = getClass().getSuperclass().getDeclaredMethod("animateOffsetTo", CoordinatorLayout.class, AppBarLayout.class, int.class, float.class);
            animateOffsetTo.setAccessible(true);
            animateOffsetTo.invoke(this, coordinatorLayout, appBarLayout, 0, velocity);
        } catch (Exception e) {
            e.printStackTrace();
            appBarLayout.setExpanded(true, true);
        }
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY) {
        alreadyFlung = true;
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }
}