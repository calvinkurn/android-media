package com.tokopedia.seller.product.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.SparseArray;
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
    public static final int DEFAULT_INPUT_VALUE_LENGTH = -1;

    private DecimalInputView decimalInputView;
    private ImageButton minusImageButton;
    private ImageButton plusImageButton;

    private String hintText;
    private String valueText;
    private int minValue;
    private boolean showCounterButton;
    private boolean enabled;
    private int maxLenghtInput;

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
            maxLenghtInput = styledAttributes.getInt(R.styleable.CounterInputView_max_length_input, DEFAULT_INPUT_VALUE_LENGTH);
            hintText = styledAttributes.getString(R.styleable.CounterInputView_counter_hint);
            valueText = styledAttributes.getString(R.styleable.CounterInputView_counter_text);
            minValue = styledAttributes.getInt(R.styleable.CounterInputView_counter_min, DEFAULT_MIN_VALUE);
            showCounterButton = styledAttributes.getBoolean(R.styleable.CounterInputView_counter_show_counter_button, true);
            enabled = styledAttributes.getBoolean(R.styleable.CounterInputView_counter_enabled, true);
        } finally {
            styledAttributes.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
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
                    long result = (long) decimalInputView.getFloatValue() - 1;
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
                decimalInputView.setText(String.valueOf((long) decimalInputView.getFloatValue() + 1));
            }
        });

        if (!TextUtils.isEmpty(hintText)) {
            decimalInputView.setHint(hintText);
        }
        if (!TextUtils.isEmpty(valueText)) {
            decimalInputView.setText(valueText);
        }
        setMaxLengthInput(maxLenghtInput);
        updateCounterButtonView(showCounterButton);
        setEnabled(enabled);
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

    }

    @Override
    public void setEnabled(boolean enable) {
        decimalInputView.setEnabled(enable);
        plusImageButton.setEnabled(enable);
        updateButtonState();
    }

    public void updateMinusButtonState(boolean enable) {
        minusImageButton.setEnabled(enable);
    }

    private void updateButtonState() {
        if (enabled) {
            minusImageButton.setEnabled(decimalInputView.getFloatValue() > minValue);
        }
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

    public void removeTextChangedListener(TextWatcher watcher) {
        decimalInputView.removeTextChangedListener(watcher);
    }

    public void setError(String error) {
        decimalInputView.setError(error);
    }

    public String getValueText() {
        return decimalInputView.getText();
    }

    public float getFloatValue() {
        return decimalInputView.getFloatValue();
    }

    public void setValue(float value) {
        decimalInputView.setValue(value);
    }

    public EditText getEditText() {
        return decimalInputView.getEditText();
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.initChildrenStates();
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).saveHierarchyState(ss.getChildrenStates());
        }
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).restoreHierarchyState(ss.getChildrenStates());
        }
    }

    public void setMaxLengthInput(int maxLengthInput) {
        this.maxLenghtInput = maxLengthInput;
        if(maxLengthInput != DEFAULT_INPUT_VALUE_LENGTH){
            decimalInputView.setMaxLengthInput(maxLengthInput);
        }
    }

    public int getMaxLenghtInput() {
        return maxLenghtInput;
    }
}