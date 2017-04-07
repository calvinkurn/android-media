package com.tokopedia.seller.product.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.tokopedia.seller.R;

/**
 * Created by nathan on 04/05/17.
 */

public class CounterInputView extends FrameLayout {

    private DecimalInputView decimalInputView;
    private ImageButton minusImageButton;
    private ImageButton plusImageButton;

    private String hintText;
    private String valueText;

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
        invalidate();
        requestLayout();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_count_input_view, this);
        decimalInputView = (DecimalInputView) view.findViewById(R.id.decimal_input_view);
        minusImageButton = (ImageButton) view.findViewById(R.id.image_button_minus);
        plusImageButton = (ImageButton) view.findViewById(R.id.image_button_plus);
        minusImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int result = decimalInputView.getIntValue() - 1;
                if (result >= 0) {
                    decimalInputView.setText(String.valueOf(result));
                }
            }
        });
        plusImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                decimalInputView.setText(String.valueOf(decimalInputView.getIntValue() + 1));
            }
        });
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

    public void addTextChangedListener(TextWatcher watcher) {
        decimalInputView.addTextChangedListener(watcher);
    }
}