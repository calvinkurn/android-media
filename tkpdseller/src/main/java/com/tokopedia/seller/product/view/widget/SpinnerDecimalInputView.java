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

public class SpinnerDecimalInputView extends FrameLayout {

    private DecimalInputView decimalInputView;
    private SpinnerTextView spinnerTextView;

    private String hintText;
    private CharSequence[] entries;
    private CharSequence[] values;
    private int selection;

    public SpinnerDecimalInputView(Context context) {
        super(context);
        init();
    }

    public SpinnerDecimalInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SpinnerDecimalInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.SpinnerDecimalInputView);
        try {
            hintText = styledAttributes.getString(R.styleable.SpinnerDecimalInputView_spinner_count_hint);
            selection = styledAttributes.getInt(R.styleable.SpinnerDecimalInputView_spinner_count_selection, 0);
            entries = styledAttributes.getTextArray(R.styleable.SpinnerDecimalInputView_spinner_count_entries);
            values = styledAttributes.getTextArray(R.styleable.SpinnerDecimalInputView_spinner_count_values);
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
        if (entries != null) {
            spinnerTextView.setEntries(ConverterUtils.convertCharSequenceToString(entries));
        }
        invalidate();
        requestLayout();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_spinner_count_input_view, this);
        spinnerTextView = (SpinnerTextView) view.findViewById(R.id.spinner_text_view);
        decimalInputView = (DecimalInputView) view.findViewById(R.id.decimal_input_view);
    }

    public void setHint(String hintText) {
        decimalInputView.setHint(hintText);
        invalidate();
        requestLayout();
    }
}