package com.tokopedia.seller.product.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TextInputLayout;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.util.NumberTextWatcher;

import java.text.DecimalFormat;

/**
 * Created by nathan on 04/05/17.
 */

public class DecimalInputView extends FrameLayout {

    private static final String DECIMAL_FORMAT = "#.##";
    private static final String DEFAULT_VALUE = "0";
    private static final int DEFAULT_INPUT_VALUE_LENGTH = -1;

    private TextInputLayout textInputLayout;
    private EditText editText;

    private String hintText;
    private String valueText;
    private boolean enabled;
    private int maxLength;

    private TextWatcher currentTextWatcher;

    public DecimalInputView(Context context) {
        super(context);
        init();
    }

    public DecimalInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DecimalInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.DecimalInputView);
        try {
            maxLength = styledAttributes.getInt(R.styleable.CounterInputView_counter_max_length, DEFAULT_INPUT_VALUE_LENGTH);
            hintText = styledAttributes.getString(R.styleable.DecimalInputView_decimal_input_hint);
            valueText = styledAttributes.getString(R.styleable.DecimalInputView_decimal_input_text);
            enabled = styledAttributes.getBoolean(R.styleable.DecimalInputView_decimal_input_enabled, true);
        } finally {
            styledAttributes.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!TextUtils.isEmpty(hintText)) {
            textInputLayout.setHint(hintText);
        }
        if (!TextUtils.isEmpty(valueText)) {
            editText.setText(valueText);
        }
        setEnabled(enabled);
        setMaxLength(maxLength);
        invalidate();
        requestLayout();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_decimal_input_view, this);
        textInputLayout = (TextInputLayout) view.findViewById(R.id.text_input_layout);
        editText = (EditText) view.findViewById(R.id.edit_text);
        currentTextWatcher = new NumberTextWatcher(editText, DEFAULT_VALUE);
        addTextChangedListener(currentTextWatcher);
    }

    @Override
    public void setEnabled(boolean enable) {
        textInputLayout.setEnabled(enable);
        editText.setEnabled(enable);
    }

    public void setHint(String hintText) {
        textInputLayout.setHint(hintText);
        invalidate();
        requestLayout();
    }

    public void setText(String textValue) {
        editText.setText(textValue);
        invalidate();
        requestLayout();
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        if (textWatcher == null) {
            return;
        }
        if (textWatcher instanceof NumberTextWatcher) {
            if (currentTextWatcher != null) {
                editText.removeTextChangedListener(currentTextWatcher);
            }
            currentTextWatcher = textWatcher;
        }
        editText.addTextChangedListener(textWatcher);
    }

    public void removeTextChangedListener(TextWatcher watcher) {
        if (watcher == currentTextWatcher) {
            currentTextWatcher = null;
        }
        editText.removeTextChangedListener(watcher);
    }

    public void setError(String error) {
        textInputLayout.setErrorEnabled(!TextUtils.isEmpty(error));
        textInputLayout.setError(error);
    }

    public String getText() {
        return editText.getText().toString();
    }

    public float getFloatValue() {
        String valueString = CurrencyFormatHelper.removeCurrencyPrefix(getText());
        valueString = CurrencyFormatHelper.RemoveNonNumeric(valueString);
        if (TextUtils.isEmpty(valueString)) {
            return 0;
        }
        return Float.parseFloat(valueString);
    }

    public void setValue(float value) {
        DecimalFormat df = new DecimalFormat(DECIMAL_FORMAT);
        editText.setText(df.format(value));
    }

    public EditText getEditText() {
        return editText;
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

    public void setMaxLength(int maxLengthInput) {
        if(maxLengthInput > DEFAULT_INPUT_VALUE_LENGTH) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthInput)});
        }
    }
}