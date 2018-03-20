package com.tokopedia.shop.page.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tokopedia.abstraction.base.view.widget.TouchViewPager;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by nathan on 3/10/18.
 */

public class ShopPageViewPager extends TouchViewPager {

    private static final long DELAY_STATE_CHANGED = TimeUnit.SECONDS.toMillis(2);

    private boolean pagingEnabled = true;
    private Timer timer = new Timer();

    public ShopPageViewPager(Context context) {
        super(context);
    }

    public ShopPageViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.pagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        try {
            return this.pagingEnabled && super.onInterceptTouchEvent(event);
        } catch (IllegalArgumentException exception) {
            exception.printStackTrace();
        }
        return false;

    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        return this.pagingEnabled && super.canScroll(v, checkV, dx, x, y);
    }

    public void setPagingEnabled(boolean pagingEnabled) {
        if (this.pagingEnabled == pagingEnabled) {
            return;
        }
        if (this.pagingEnabled) {
            timer.cancel();
            this.pagingEnabled = false;
            return;
        }
        // Update with timer
        updatePagingStateWithTimer(true);
    }

    private void updatePagingStateWithTimer(final boolean pagingEnabled) {
        timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ShopPageViewPager.this.pagingEnabled = pagingEnabled;
            }
        }, DELAY_STATE_CHANGED);
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
    }

    public Boolean getPagingStatus() {
        return pagingEnabled;
    }
}