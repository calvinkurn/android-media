package com.tokopedia.seller.product.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.tokopedia.seller.R;

/**
 * Created by nathan on 04/05/17.
 */

public class CounterInputView extends FrameLayout {

    private static final int DEFAULT_MIN_VALUE = 0;

    private DecimalInputView decimalInputView;
    private ImageButton minusImageButton;
    private ImageButton plusImageButton;

    private String hintText;
    private String valueText;
    private int minValue;
    private boolean showCounterButton;

    public CounterInputView(Context context) {
        super(context);
        init();
    }

    public CounterInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CounterInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.CounterInputView);
        try {
            hintText = styledAttributes.getString(R.styleable.CounterInputView_counter_hint);
            valueText = styledAttributes.getString(R.styleable.CounterInputView_counter_text);
            minValue = styledAttributes.getInt(R.styleable.CounterInputView_counter_min, DEFAULT_MIN_VALUE);
            showCounterButton = styledAttributes.getBoolean(R.styleable.CounterInputView_counter_show_counter_button, true);
        } finally {
            styledAttributes.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!TextUtils.isEmpty(hintText)) {
            decimalInputView.setHint(hintText);
        }
        if (!TextUtils.isEmpty(valueText)) {
            decimalInputView.setText(valueText);
        }
        updateCounterButtonView(showCounterButton);
        updateButtonState();
        invalidate();
        requestLayout();
    }

    public void updateCounterButtonView(boolean show) {
        plusImageButton.setVisibility(show ? View.VISIBLE : View.GONE);
        minusImageButton.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_count_input_view, this);
        decimalInputView = (DecimalInputView) view.findViewById(R.id.decimal_input_view);
        minusImageButton = (ImageButton) view.findViewById(R.id.image_button_minus);
        plusImageButton = (ImageButton) view.findViewById(R.id.image_button_plus);
        decimalInputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateButtonState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        minusImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int result = (int) decimalInputView.getFloatValue() - 1;
                    if (result >= 0) {
                        decimalInputView.setText(String.valueOf(result));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        plusImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                decimalInputView.setText(String.valueOf(decimalInputView.getFloatValue() + 1));
            }
        });
    }

    private void updateButtonState() {
        minusImageButton.setEnabled(decimalInputView.getFloatValue() > minValue);
    }

    public void setHint(String hintText) {
        decimalInputView.setHint(hintText);
        invalidate();
        requestLayout();
    }

    public void setText(String textValue) {
        decimalInputView.setText(textValue);
        invalidate();
        requestLayout();
    }

    public void showCounterButton(boolean show) {
        updateCounterButtonView(show);
        invalidate();
        requestLayout();
    }

    public void addTextChangedListener(TextWatcher watcher) {
        decimalInputView.addTextChangedListener(watcher);
    }

    public EditText getEditText() {
        return decimalInputView.getEditText();
    }
}