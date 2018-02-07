package com.tokopedia.home.beranda.presentation.view.compoundview;

import android.content.Context;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.home.R;

import java.util.Date;
import java.util.Locale;

/**
 * Created by henrypriyono on 31/01/18.
 */

public class CountDownView extends FrameLayout {
    public static final int REFRESH_DELAY_MS = 1000;

    private TextView hourView;
    private TextView minuteView;
    private TextView secondView;
    private View rootView;

    private int hour;
    private int minute;
    private int second;

    private Handler refreshCounterHandler;
    private Runnable runnableRefreshCounter;

    public CountDownView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public CountDownView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CountDownView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        rootView = inflate(context, R.layout.count_down_view, this);
        hourView = (TextView) rootView.findViewById(R.id.hourView);
        minuteView = (TextView) rootView.findViewById(R.id.minuteView);
        secondView = (TextView) rootView.findViewById(R.id.secondView);
        displayTime();
    }

    public void setup(final Date expiredTime) {
        if (refreshCounterHandler == null && runnableRefreshCounter == null) {
            refreshCounterHandler = new Handler();
            runnableRefreshCounter = new Runnable() {
                @Override
                public void run() {
                    Date now = new Date();
                    TimeDiffModel timeDiff = getTimeDiff(now, expiredTime);
                    setTime(timeDiff.getHour(), timeDiff.getMinute(), timeDiff.getSecond());
                    refreshCounterHandler.postDelayed(this, REFRESH_DELAY_MS);
                }
            };
            startAutoRefreshCounter();
        }
    }

    private TimeDiffModel getTimeDiff(Date startTime, Date endTime) {
        long diff = endTime.getTime() - startTime.getTime();
        TimeDiffModel model = new TimeDiffModel();
        model.setSecond((int) (diff / 1000 % 60));
        model.setMinute((int) (diff / (60 * 1000) % 60));
        model.setHour((int) (diff / (60 * 60 * 1000) % 24));
        return model;
    }

    public void stopAutoRefreshCounter() {
        if (refreshCounterHandler != null && runnableRefreshCounter != null) {
            refreshCounterHandler.removeCallbacks(runnableRefreshCounter);
        }
    }

    private void startAutoRefreshCounter() {
        if (refreshCounterHandler != null && runnableRefreshCounter != null) {
            refreshCounterHandler.postDelayed(runnableRefreshCounter, REFRESH_DELAY_MS);
        }
    }

    private void setTime(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        displayTime();
    }

    private void displayTime() {
        hourView.setText(String.format(Locale.US, "%02d", hour));
        minuteView.setText(String.format(Locale.US, "%02d", minute));
        secondView.setText(String.format(Locale.US, "%02d", second));
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        stopAutoRefreshCounter();
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        startAutoRefreshCounter();
    }

    public String getCurrentCountDown() {
        return String.format("%s:%s:%s", hourView.getText(), minuteView.getText(), secondView.getText());
    }

    private static class TimeDiffModel {
        private int second;
        private int minute;
        private int hour;

        public int getSecond() {
            return second;
        }

        public void setSecond(int second) {
            this.second = second;
        }

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }
    }
}
