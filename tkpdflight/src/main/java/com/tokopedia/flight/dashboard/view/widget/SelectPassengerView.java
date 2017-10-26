package com.tokopedia.flight.dashboard.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.flight.R;

/**
 * Created by alvarisi on 10/25/17.
 */

public class SelectPassengerView extends BaseCustomView {
    private static final int DEFAULT_VALUE = 0;
    private static final int DEFAULT_MIN_VALUE = 0;
    private static final int DEFAULT_MAX_VALUE = 100;

    private AppCompatImageView passengerImageView;
    private AppCompatTextView titleTextView;
    private AppCompatTextView subtitleTextView;
    private NumberPickerWithCounterView numberPickerWithCounterView;

    private Drawable icon;
    private int selectedNumber;
    private int maxValue;
    private int minValue;
    private String title;
    private String subtitle;

    public SelectPassengerView(@NonNull Context context) {
        super(context);
        init();
    }

    public SelectPassengerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SelectPassengerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.SelectPassengerView);
        try {
            maxValue = styledAttributes.getInteger(R.styleable.SelectPassengerView_spv_max_value, DEFAULT_MAX_VALUE);
            minValue = styledAttributes.getInteger(R.styleable.SelectPassengerView_spv_min_value, DEFAULT_MIN_VALUE);
            selectedNumber = styledAttributes.getInteger(R.styleable.SelectPassengerView_spv_value, DEFAULT_VALUE);
            icon = styledAttributes.getDrawable(R.styleable.SelectPassengerView_spv_icon);
            title = styledAttributes.getString(R.styleable.SelectPassengerView_spv_title);
            subtitle = styledAttributes.getString(R.styleable.SelectPassengerView_spv_subtitle);
        } finally {
            styledAttributes.recycle();
        }
    }


    private void init() {
        View view = inflate(getContext(), R.layout.widget_select_passenger_view, this);
        numberPickerWithCounterView = (NumberPickerWithCounterView) view.findViewById(R.id.number_picker_passenger);
        passengerImageView = (AppCompatImageView) view.findViewById(R.id.image_passenger_icon);
        titleTextView = (AppCompatTextView) view.findViewById(R.id.textview_title);
        subtitleTextView = (AppCompatTextView) view.findViewById(R.id.textview_subtitle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        numberPickerWithCounterView.setMaxValue(maxValue);
        numberPickerWithCounterView.setMinValue(minValue);
        numberPickerWithCounterView.setNumber(selectedNumber);

        if (icon != null) {
            passengerImageView.setImageDrawable(icon);
        }

        if (!TextUtils.isEmpty(title)) {
            titleTextView.setText(title);
        }

        if (!TextUtils.isEmpty(subtitle)) {
            subtitleTextView.setText(subtitle);
        }
    }

    public int getValue() {
        return numberPickerWithCounterView.getValue();
    }
}
