package com.tokopedia.seller.product.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.tokopedia.seller.R;

/**
 * Created by nathan on 04/05/17.
 */

public class CountInputView extends FrameLayout {

    private TextInputLayout textInputLayout;
    private EditText countEditText;
    private ImageButton minusImageButton;
    private ImageButton plusImageButton;

    private String hintText;
    private String valueText;

    public CountInputView(Context context) {
        super(context);
        init();
    }

    public CountInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CountInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.CountInputView);
        try {
            hintText = styledAttributes.getString(R.styleable.CountInputView_count_hint);
            valueText = styledAttributes.getString(R.styleable.CountInputView_count_value);
        } finally {
            styledAttributes.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        textInputLayout.setHint(hintText);
        if (!TextUtils.isEmpty(valueText)) {
            countEditText.setText(valueText);
        }
        invalidate();
        requestLayout();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_count_input_view, this);
        textInputLayout = (TextInputLayout) view.findViewById(R.id.text_input_layout);
        countEditText = (EditText) view.findViewById(R.id.edit_text_count);
        minusImageButton = (ImageButton) view.findViewById(R.id.image_button_minus);
        plusImageButton = (ImageButton) view.findViewById(R.id.image_button_plus);
        minusImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int result = getIntValue() - 1;
                if (result >= 0) {
                    countEditText.setText(String.valueOf(result));
                }
            }
        });
        plusImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                countEditText.setText(String.valueOf(getIntValue() + 1));
            }
        });
    }

    public void setHint(String hintText) {
        countEditText.setHint(hintText);
        invalidate();
        requestLayout();
    }

    public void setValue(String textValue) {
        countEditText.setText(textValue);
        invalidate();
        requestLayout();
    }

    public int getIntValue() {
        return Integer.parseInt(getStringValue());
    }

    public String getStringValue() {
        return countEditText.getText().toString();
    }
}