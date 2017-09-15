package com.tokopedia.inbox.rescenter.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.inbox.R;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * @author by nisie on 9/15/17.
 */

public class TimeTickerUtil {

    public interface TimeTickerListener{
        void onStart();
        void onFinish();
    }

    private static final long COUNTDOWN_INTERVAL = 1000;
    private static final String FORMAT_DAY = "";
    private TextView day;
    private TextView hour;
    private TextView minute;
    private TextView second;
    private CountDownTimer countDownTimer;
    private boolean isRunningTimer;
    private TimeTickerListener listener;

    public TimeTickerUtil(View timeTickerView, TimeTickerListener listener) {
        day = (TextView) timeTickerView.findViewById(R.id.day);
        hour = (TextView) timeTickerView.findViewById(R.id.hour);
        minute = (TextView) timeTickerView.findViewById(R.id.minute);
        second = (TextView) timeTickerView.findViewById(R.id.second);
        day.setText("0");
        hour.setText("0");
        minute.setText("0");
        second.setText("0");
        this.listener = listener;
    }

    public static TimeTickerUtil createInstance(View timeTickerView,
                                                TimeTickerListener listener) {
        return new TimeTickerUtil(timeTickerView, listener);
    }

    public void startTimer(long duration) {
        listener.onStart();
        if (!isRunningTimer) {
            countDownTimer = new CountDownTimer(duration, COUNTDOWN_INTERVAL) {
                public void onTick(long millisUntilFinished) {
                    try {
                        isRunningTimer = true;

                        day.setText(String.format(
                                Locale.getDefault(),
                                "%d",
                                getDay(millisUntilFinished)));

                        hour.setText(String.format(
                                Locale.getDefault(),
                                "%d",
                                getHour(millisUntilFinished)));

                        minute.setText(String.format(
                                Locale.getDefault(),
                                "%d",
                                getMinute(millisUntilFinished)));

                        second.setText(String.format(
                                Locale.getDefault(),
                                "%d",
                                getSecond(millisUntilFinished)));

                    } catch (Exception e) {
                        cancel();
                    }
                }

                public void onFinish() {
                    try {
                        isRunningTimer = false;
                        listener.onFinish();
                    } catch (Exception e) {

                    }
                }

            }.start();
        }
    }

    private long getSecond(long millisUntilFinished) {
        return TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
    }

    private long getMinute(long millisUntilFinished) {
        return (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 3600) / 60;
    }

    private long getHour(long millisUntilFinished) {
        return (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 86400) / 3600;
    }

    private long getDay(long millisUntilFinished) {
        return TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) / 86400;
    }

    public void destroy() {
        isRunningTimer = false;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

}
