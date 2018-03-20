package com.tokopedia.flight.dashboard.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.flight.R;

/**
 * Created by nathan on 10/20/17.
 */

public class TextInputView extends BaseCustomView {

    private static final String PNG_EXTENSION = "png";
    private static final String SVG_EXTENSION = "xml";

    private TkpdHintTextInputLayout textInputLayout;

    private Drawable iconDrawable;
    private String titleText;
    private String hintText;
    private boolean allowInputManually;
    private boolean removeBackground;

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
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.TextInputView);

        // get resource full name, to use later for checking is it png or vector
        TypedValue value = new TypedValue();
        getContext().getResources().getValue(styledAttributes.getResourceId(R.styleable.TextInputView_tiv_icon, R.drawable.ic_smiley_good), value, true);

        try {
            if (value.string.toString().contains(SVG_EXTENSION)) {
                iconDrawable = VectorDrawableCompat.create(getContext().getResources(), styledAttributes.getResourceId(R.styleable.TextInputView_tiv_icon, R.drawable.ic_smiley_good), getContext().getTheme());
            } else if (value.string.toString().contains(PNG_EXTENSION)) {
                iconDrawable = styledAttributes.getDrawable(R.styleable.TextInputView_tiv_icon);
            }
            hintText = styledAttributes.getString(R.styleable.TextInputView_tiv_hint_text);
            titleText = styledAttributes.getString(R.styleable.TextInputView_tiv_title_text);
            allowInputManually = styledAttributes.getBoolean(R.styleable.TextInputView_allow_input_manually, false);
            removeBackground = styledAttributes.getBoolean(R.styleable.TextInputView_tiv_remove_background, false);
        } finally {
            styledAttributes.recycle();
        }
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_text_input_view, this);
        AppCompatImageView imageView = (AppCompatImageView) view.findViewById(R.id.image_view);
        textInputLayout = (TkpdHintTextInputLayout) view.findViewById(R.id.text_input_layout);
        EditText etText = textInputLayout.getEditText();

        if (removeBackground) {
            etText.setBackground(null);
        }

        if (iconDrawable == null) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setImageDrawable(iconDrawable);
            imageView.setVisibility(View.VISIBLE);
        }
        textInputLayout.setLabel(titleText);
        if (TextUtils.isEmpty(hintText)) {
            if (!TextUtils.isEmpty(titleText)) {
                textInputLayout.setHint(titleText);
            } else {
                textInputLayout.setHint(null);
            }
        } else {
            textInputLayout.setHint(hintText);
        }
        if (allowInputManually) {

        }
        textInputLayout.setClickable(allowInputManually);
        etText.setClickable(allowInputManually);
        textInputLayout.setEnabled(allowInputManually);
        etText.setEnabled(allowInputManually);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (allowInputManually) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return true;
        }
    }

    public CharSequence getText() {
        return textInputLayout.getEditText().getText();
    }

    public void setText(CharSequence text) {
        textInputLayout.getEditText().setText(text);
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
    }
}