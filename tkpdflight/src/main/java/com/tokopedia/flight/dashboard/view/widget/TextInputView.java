package com.tokopedia.flight.dashboard.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.flight.R;

/**
 * Created by nathan on 10/20/17.
 */

public class TextInputView extends BaseCustomView {

    private ImageView imageView;
    private TextInputLayout textInputLayout;
    private EditText editText;

    private Drawable iconDrawable;
    private String titleText;
    private String hintText;

    public TextInputView(Context context) {
        super(context);
        init();
    }

    public TextInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TextInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.TextInputView);
        try {
            iconDrawable = styledAttributes.getDrawable(R.styleable.TextInputView_tiv_icon);
            hintText = styledAttributes.getString(R.styleable.TextInputView_tiv_hint_text);
            titleText = styledAttributes.getString(R.styleable.TextInputView_tiv_title_text);
        } finally {
            styledAttributes.recycle();
        }
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_text_input_view, this);
        imageView = (ImageView) view.findViewById(R.id.image_view);
        textInputLayout = (TextInputLayout) view.findViewById(R.id.text_input_layout);
        editText = (EditText) view.findViewById(R.id.edit_text);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (iconDrawable == null) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setImageDrawable(iconDrawable);
            imageView.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(titleText)) {
            textInputLayout.setHint(titleText);
        }
        invalidate();
        requestLayout();
    }
}