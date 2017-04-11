package com.tokopedia.seller.product.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.utils.ConverterUtils;

/**
 * Created by nathan on 04/05/17.
 */

public class SpinnerCounterInputView extends FrameLayout {

    private CounterInputView counterInputView;
    private SpinnerTextView spinnerTextView;

    private String hintText;
    private CharSequence[] entries;
    private CharSequence[] values;
    private int selection;
    private boolean showCounterButton;

    public SpinnerCounterInputView(Context context) {
        super(context);
        init();
    }

    public SpinnerCounterInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SpinnerCounterInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.SpinnerCounterInputView);
        try {
            hintText = styledAttributes.getString(R.styleable.SpinnerCounterInputView_spinner_decimal_hint);
            selection = styledAttributes.getInt(R.styleable.SpinnerCounterInputView_spinner_decimal_selection, 0);
            entries = styledAttributes.getTextArray(R.styleable.SpinnerCounterInputView_spinner_decimal_entries);
            values = styledAttributes.getTextArray(R.styleable.SpinnerCounterInputView_spinner_decimal_values);
            showCounterButton = styledAttributes.getBoolean(R.styleable.SpinnerCounterInputView_spinner_decimal_show_counter_button, true);
        } finally {
            styledAttributes.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!TextUtils.isEmpty(hintText)) {
            counterInputView.setHint(hintText);
        }
        if (entries != null) {
            spinnerTextView.setEntries(ConverterUtils.convertCharSequenceToString(entries));
        }
        counterInputView.showCounterButton(showCounterButton);
        invalidate();
        requestLayout();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_spinner_counter_input_view, this);
        spinnerTextView = (SpinnerTextView) view.findViewById(R.id.spinner_text_view);
        counterInputView = (CounterInputView) view.findViewById(R.id.counter_input_view);
    }

    public void setHint(String hintText) {
        counterInputView.setHint(hintText);
        invalidate();
        requestLayout();
    }
}