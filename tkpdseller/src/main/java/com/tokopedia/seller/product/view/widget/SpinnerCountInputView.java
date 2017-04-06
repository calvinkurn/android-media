package com.tokopedia.seller.product.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.utils.ConverterUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 04/05/17.
 */

public class SpinnerCountInputView extends FrameLayout {

    private TextInputLayout textInputLayout;
    private EditText countEditText;
    private SpinnerTextView spinnerTextView;

    private String hintText;
    private CharSequence[] entries;
    private CharSequence[] values;
    private int selection;

    public SpinnerCountInputView(Context context) {
        super(context);
        init();
    }

    public SpinnerCountInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SpinnerCountInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.SpinnerCountInputView);
        try {
            hintText = styledAttributes.getString(R.styleable.SpinnerCountInputView_spinner_count_hint);
            selection = styledAttributes.getInt(R.styleable.SpinnerCountInputView_spinner_count_selection, 0);
            entries = styledAttributes.getTextArray(R.styleable.SpinnerCountInputView_spinner_count_entries);
            values = styledAttributes.getTextArray(R.styleable.SpinnerCountInputView_spinner_count_values);
        } finally {
            styledAttributes.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        textInputLayout.setHint(hintText);
        if (entries != null) {
            spinnerTextView.setEntries(ConverterUtils.convertCharSequenceToString(entries));
        }
        invalidate();
        requestLayout();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_spinner_count_input_view, this);
        spinnerTextView = (SpinnerTextView) view.findViewById(R.id.spinner_text_view_count);
        textInputLayout = (TextInputLayout) view.findViewById(R.id.text_input_layout_count);
        countEditText = (EditText) view.findViewById(R.id.edit_text_count);
    }

    public void setHint(String hintText) {
        textInputLayout.setHint(hintText);
        invalidate();
        requestLayout();
    }
}