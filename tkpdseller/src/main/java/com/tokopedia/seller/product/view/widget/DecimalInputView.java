package com.tokopedia.seller.product.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.view.widget.TopAdsCurrencyTextWatcher;

/**
 * Created by nathan on 04/05/17.
 */

public class DecimalInputView extends FrameLayout {

    private TextInputLayout textInputLayout;
    private EditText editText;

    private String hintText;
    private String valueText;

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
            hintText = styledAttributes.getString(R.styleable.DecimalInputView_hint);
            valueText = styledAttributes.getString(R.styleable.DecimalInputView_value);
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
        invalidate();
        requestLayout();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_decimal_input_view, this);
        textInputLayout = (TextInputLayout) view.findViewById(R.id.text_input_layout);
        editText = (EditText) view.findViewById(R.id.edit_text);
        addTextChangedListener(new TopAdsCurrencyTextWatcher(editText, "0"));
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

    public void addTextChangedListener(TextWatcher watcher) {
        editText.addTextChangedListener(watcher);
    }

    public String getText() {
        return editText.getText().toString();
    }

    public int getIntValue() {
        String valueString = CurrencyFormatHelper.removeCurrencyPrefix(getText());
        valueString = CurrencyFormatHelper.RemoveNonNumeric(valueString);
        return Integer.parseInt(valueString);
    }
}