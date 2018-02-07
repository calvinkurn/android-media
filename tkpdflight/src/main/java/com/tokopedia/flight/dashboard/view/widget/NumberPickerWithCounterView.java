package com.tokopedia.flight.dashboard.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.flight.R;

/**
 * @author by alvarisi on 10/25/17.
 */

public class NumberPickerWithCounterView extends BaseCustomView {
    private static final int DEFAULT_VALUE = 0;
    private static final int DEFAULT_MIN_VALUE = 0;
    private static final int DEFAULT_MAX_VALUE = 100;
    private static final boolean DEFAULT_ENABLED_VALUE = true;

    private AppCompatTextView numberInputView;
    private AppCompatImageButton minusImageButton;
    private AppCompatImageButton plusImageButton;

    private boolean enabled;
    private int number;
    private int minValue;
    private int maxValue;
    private OnPickerActionListener onPickerActionListener;

    public interface OnPickerActionListener {
        void onNumberChange(int num);
    }

    public NumberPickerWithCounterView(@NonNull Context context) {
        super(context);
        init();
    }

    public NumberPickerWithCounterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public NumberPickerWithCounterView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.NumberPickerWithCounterView);
        try {
            maxValue = styledAttributes.getInteger(R.styleable.NumberPickerWithCounterView_npc_max_value, DEFAULT_MAX_VALUE);
            minValue = styledAttributes.getInteger(R.styleable.NumberPickerWithCounterView_npc_min_value, DEFAULT_MIN_VALUE);
            number = styledAttributes.getInteger(R.styleable.NumberPickerWithCounterView_npc_value, DEFAULT_VALUE);
            enabled = styledAttributes.getBoolean(R.styleable.NumberPickerWithCounterView_npc_enabled, DEFAULT_ENABLED_VALUE);
        } finally {
            styledAttributes.recycle();
        }
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_number_picker_with_counter_view, this);

        numberInputView = (AppCompatTextView) view.findViewById(R.id.decimal_input_view);
        plusImageButton = (AppCompatImageButton) view.findViewById(R.id.image_button_plus);
        minusImageButton = (AppCompatImageButton) view.findViewById(R.id.image_button_minus);
        numberInputView.setEnabled(false);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        numberInputView.setText(String.valueOf(number));
        plusImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                validateMaxValue(Integer.parseInt(numberInputView.getText().toString().trim()) + 1);
                updateButtonState();
            }
        });

        minusImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                validateMinValue(Integer.parseInt(numberInputView.getText().toString().trim()) - 1);
                updateButtonState();
            }
        });
        numberInputView.setEnabled(enabled);
        updateButtonState();
    }

    private void validateMaxValue(int newNumber) {
        if (Integer.parseInt(numberInputView.getText().toString().trim()) < maxValue) {
            numberInputView.setText(String.valueOf(newNumber));
        }
    }

    private void validateMinValue(int newNumber) {
        if (Integer.parseInt(numberInputView.getText().toString().trim()) > minValue) {
            numberInputView.setText(String.valueOf(newNumber));
        }
    }

    private void updateButtonState() {
        minusImageButton.setEnabled(enabled && Integer.parseInt(numberInputView.getText().toString().trim()) > minValue);
        plusImageButton.setEnabled(enabled && Integer.parseInt(numberInputView.getText().toString().trim()) < maxValue);

        if (onPickerActionListener != null) {
            onPickerActionListener.onNumberChange(Integer.parseInt(numberInputView.getText().toString().trim()));
        }
    }

    public void setNumber(int number) {
        numberInputView.setText(String.valueOf(number));
        minusImageButton.setEnabled(enabled && Integer.parseInt(numberInputView.getText().toString().trim()) > minValue);
        plusImageButton.setEnabled(enabled && Integer.parseInt(numberInputView.getText().toString().trim()) < maxValue);
    }

    public void setOnPickerActionListener(OnPickerActionListener onPickerActionListener) {
        this.onPickerActionListener = onPickerActionListener;
    }

    public void setMinValue(int number) {
        this.minValue = number;
        invalidate();
        requestLayout();
    }

    public void setMaxValue(int number) {
        this.maxValue = number;
        invalidate();
        requestLayout();
    }

    public int getValue() {
        return Integer.parseInt(numberInputView.getText().toString().trim());
    }
}
