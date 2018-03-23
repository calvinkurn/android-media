package com.tokopedia.home.beranda.presentation.view.compoundview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.home.R;

import org.w3c.dom.Text;

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
    private TextView col1, col2;
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
        col1 = rootView.findViewById(R.id.col1);
        col2 = rootView.findViewById(R.id.col2);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CountDownView, 0, 0);
        try {
            hourView.setTextColor(a.getColor(R.styleable.CountDownView_countDownTxtColor, Color.WHITE));
            minuteView.setTextColor(a.getColor(R.styleable.CountDownView_countDownTxtColor, Color.WHITE));
            secondView.setTextColor(a.getColor(R.styleable.CountDownView_countDownTxtColor, Color.WHITE));
            col1.setTextColor(a.getColor(R.styleable.CountDownView_countDownSparatorColor, ContextCompat.getColor(context, R.color.tkpd_main_orange)));
            col2.setTextColor(a.getColor(R.styleable.CountDownView_countDownSparatorColor, ContextCompat.getColor(context, R.color.tkpd_main_orange)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                hourView.setBackground(a.getDrawable(R.styleable.CountDownView_coundDownBackgroud));
                minuteView.setBackground(a.getDrawable(R.styleable.CountDownView_coundDownBackgroud));
                secondView.setBackground(a.getDrawable(R.styleable.CountDownView_coundDownBackgroud));
            } else {
                final int drawableBackgroud = a.getResourceId(R.styleable.CountDownView_coundDownBackgroud, -1);
                hourView.setBackgroundResource(drawableBackgroud);
                minuteView.setBackgroundResource(drawableBackgroud);
                secondView.setBackgroundResource(drawableBackgroud);
            }
        } finally {
            a.recycle();
        }
        displayTime();
    }

    public void setup(final Date expiredTime, final CountDownListener listener) {
        if (isExpired(expiredTime)) {
            handleExpiredTime(listener);
            return;
        }
        stopAutoRefreshCounter();
        refreshCounterHandler = new Handler();
        runnableRefreshCounter = new Runnable() {
            @Override
            public void run() {
                if (!isExpired(expiredTime)) {
                    Date now = new Date();
                    TimeDiffModel timeDiff = getTimeDiff(now, expiredTime);
                    setTime(timeDiff.getHour(), timeDiff.getMinute(), timeDiff.getSecond());
                    refreshCounterHandler.postDelayed(this, REFRESH_DELAY_MS);
                } else {
                    handleExpiredTime(listener);
                }
            }
        };
        startAutoRefreshCounter();
    }

    private void handleExpiredTime(CountDownListener listener) {
        stopAutoRefreshCounter();
        setTime(0, 0, 0);
        listener.onCountDownFinished();
    }

    private boolean isExpired(Date expiredTime) {
        return new Date().after(expiredTime);
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

    public interface CountDownListener {
        void onCountDownFinished();
    }
}
