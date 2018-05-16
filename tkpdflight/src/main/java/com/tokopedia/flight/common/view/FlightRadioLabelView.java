package com.tokopedia.flight.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.flight.R;

public class FlightRadioLabelView extends BaseCustomView {
    private TextView titleTextView;
    private SwitchCompat toggleSwitchCompat;
    private OnCheckChangeListener listener;
    private String title;

    public interface OnCheckChangeListener {
        void onCheckedChanged(Boolean checked);
    }


    public FlightRadioLabelView(@NonNull Context context) {
        super(context);
        init();
    }

    public FlightRadioLabelView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FlightRadioLabelView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.FlightRadioLabelView);
        try {
            title = styledAttributes.getString(R.styleable.FlightRadioLabelView_frlv_title);
        } finally {
            styledAttributes.recycle();
        }
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_flight_radio_label_view, this);
        titleTextView = (TextView) view.findViewById(R.id.title);
        toggleSwitchCompat = (SwitchCompat) view.findViewById(R.id.toggle);
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener
                = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (listener != null) {
                    listener.onCheckedChanged(isChecked);
                }
            }
        };
        toggleSwitchCompat.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleTextView.setText(title);
    }

    public void setListener(OnCheckChangeListener listener) {
        this.listener = listener;
    }

    public void setChecked(boolean checked) {
        toggleSwitchCompat.setChecked(checked);
    }
}
