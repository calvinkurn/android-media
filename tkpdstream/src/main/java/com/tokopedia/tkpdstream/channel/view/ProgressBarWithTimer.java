package com.tokopedia.tkpdstream.channel.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.tkpdstream.R;

/**
 * @author by StevenFredian on 14/02/18.
 */

public class ProgressBarWithTimer extends FrameLayout {
    private TextView text;
    private ProgressBar progressBar;
    private CountDownTimer countDownTimer;
    Listener listener;

    private long startTime, endTime;

    public static interface Listener {
        void onStartTick();

        void onFinishTick();
    }

    public ProgressBarWithTimer(@NonNull Context context) {
        super(context);
        init();
    }

    public ProgressBarWithTimer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ProgressBarWithTimer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressBarWithTimer);
        styledAttributes.recycle();
    }

    private String formatMilliSecondsToTime(long milliseconds) {

        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)));
        return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : "
                + twoDigitString(seconds);
    }

    private String twoDigitString(long number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private void init() {
        View view = inflate(getContext(), R.layout.progress_bar_with_timer, this);
        text = view.findViewById(R.id.text_view);
        progressBar = view.findViewById(R.id.progress_bar);
    }

    public void setTimer(final long startTime, final long endTime) {
        Log.d("NISNIS", "setTimer " + startTime + " - " + endTime);
        this.startTime = startTime;
        this.endTime = endTime;
        long now = System.currentTimeMillis() / 1000L;
        getRootView().setVisibility(VISIBLE);
        listener.onStartTick();
        countDownTimer = new CountDownTimer(1000 * (endTime - now), 100) {
            @Override
            public void onTick(long l) {
                long now = System.currentTimeMillis() / 1000L;
                text.setText(formatMilliSecondsToTime(l));
                int percent = (int) ((now - startTime) * 100 / (endTime - startTime));
                progressBar.setProgress(Math.abs(percent));
            }

            @Override
            public void onFinish() {
                if (listener != null)
                    listener.onFinishTick();
            }
        };
        countDownTimer.start();
        invalidate();
        requestLayout();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        invalidate();
        requestLayout();
    }

    public void start() {
        if (countDownTimer != null) {
            countDownTimer.start();
        }
    }

    public void cancel() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public void restart() {
        setTimer(startTime, endTime);
    }
}
