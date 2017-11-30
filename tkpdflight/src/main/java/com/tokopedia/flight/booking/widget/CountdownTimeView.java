package com.tokopedia.flight.booking.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.flight.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * @author by alvarisi on 11/23/17.
 */

public class CountdownTimeView extends BaseCustomView {
    private static final String DEFAULT_HOURS = "h";
    private static final String DEFAULT_MINUTE = "m";
    private static final String DEFAULT_SECOND = "s";

    private Date expiredDate;
    private AppCompatTextView timeTextView;
    private LinearLayout containerLayout;
    private OnActionListener listener;
    private String hoursLabel;
    private String minutesLabel;
    private String secondsLabel;
    private CountDownTimer countDownTimer;

    public interface OnActionListener {
        void onFinished();
    }

    public CountdownTimeView(@NonNull Context context) {
        super(context);
        init();
    }

    public CountdownTimeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CountdownTimeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_countdown_time, this);
        timeTextView = (AppCompatTextView) view.findViewById(R.id.tv_time);
        containerLayout = (LinearLayout) view.findViewById(R.id.container);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.CountdownTimeView);
        try {
            hoursLabel = styledAttributes.getString(R.styleable.CountdownTimeView_ctv_hour_title);
            minutesLabel = styledAttributes.getString(R.styleable.CountdownTimeView_ctv_minute_title);
            secondsLabel = styledAttributes.getString(R.styleable.CountdownTimeView_ctv_second_title);

        } finally {
            styledAttributes.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (TextUtils.isEmpty(hoursLabel)) {
            hoursLabel = DEFAULT_HOURS;
        }
        if (TextUtils.isEmpty(minutesLabel)) {
            minutesLabel = DEFAULT_MINUTE;
        }
        if (TextUtils.isEmpty(secondsLabel)) {
            secondsLabel = DEFAULT_SECOND;
        }
    }

    public void setListener(OnActionListener listener) {
        this.listener = listener;
    }

    public void setExpiredDate(Date date) {
        this.expiredDate = date;
    }

    public void start() {
        if (this.expiredDate != null) {
            Calendar now = new GregorianCalendar(TimeZone.getTimeZone("Asia/Jakarta"));
            long delta = this.expiredDate.getTime() - now.getTimeInMillis();

            if (TimeUnit.MILLISECONDS.toDays(delta) < 1) {
                timeTextView.setText(getCountdownText(delta));
                containerLayout.setVisibility(VISIBLE);

                countDownTimer = new CountDownTimer(delta, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        timeTextView.setText(getCountdownText(millisUntilFinished));
                    }

                    @Override
                    public void onFinish() {
                        if (listener != null) {
                            listener.onFinished();
                        }
                        containerLayout.setVisibility(GONE);
                    }
                };
                countDownTimer.start();
            }
        }
    }

    public void cancel() {
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
    }

    private String getCountdownText(long millisUntilFinished) {
        String countdown = "";
        if ((millisUntilFinished / (1000 * 60 * 60)) % 24 > 0) {
            countdown = (millisUntilFinished / (1000 * 60 * 60)) % 24 + " " + hoursLabel + (millisUntilFinished / (1000 * 60)) % 60 + " " + minutesLabel + " " + (millisUntilFinished / (1000)) % 60 + " " + secondsLabel;
        } else {
            countdown = (millisUntilFinished / (1000 * 60)) % 60 + " " + minutesLabel + " " + (millisUntilFinished / (1000)) % 60 + " " + secondsLabel;
        }
        return countdown;
    }
}
