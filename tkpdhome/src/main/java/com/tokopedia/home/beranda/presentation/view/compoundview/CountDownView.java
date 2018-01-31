package com.tokopedia.home.beranda.presentation.view.compoundview;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.home.R;

import java.util.Locale;

/**
 * Created by henrypriyono on 31/01/18.
 */

public class CountDownView extends FrameLayout {
    private TextView hourView;
    private TextView minuteView;
    private TextView secondView;
    private View rootView;

    private int hour;
    private int minute;
    private int second;

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

    public void setTime(int hour, int minute, int second) {
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
}
