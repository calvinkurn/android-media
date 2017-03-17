package com.tokopedia.seller.lib.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.seller.R;

/**
 * Created by Hendry on 3/16/2017.
 * Custom widget extending from support.widget.TextInputLayout
 * Add function to setSuccess("Your success Text") to show different color with error text
 *
 * attrb successTextAppearance will refer to TextAppearance class
 * attrb successFocusColor - color when success is shown and focus
 * attrb successLeaveColor - color when success is shown but not in focus
 *
 * use hideErrorSuccess to set visibility to INVISIBLE
 * use setErrorSuccessEnabled to set visibility to VISIBLE/GONE
 */

public class TkpdTextInputLayout extends TextInputLayout {
    int mSuccessFocusColor;
    int mSuccessLeaveColor;

    ViewGroup mIndicatorView;

    CharSequence mSuccessString;
    private boolean mSuccessEnabled;
    private boolean mSuccessShown;
    private TextView mSuccessView;

    private int mSuccessTextAppearance;

    private int COLOR_GREEN = Color.parseColor("#42b549");

    public TkpdTextInputLayout(Context context) {
        super(context);
        init(null, 0);
    }

    public TkpdTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TkpdTextInputLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TkpdTextInputLayout, defStyle, 0);

        mSuccessFocusColor = a.getColor(
                R.styleable.TkpdTextInputLayout_successFocusColor,
                COLOR_GREEN);
        mSuccessLeaveColor = a.getColor(
                R.styleable.TkpdTextInputLayout_successLeaveColor,
                mSuccessFocusColor);
        mSuccessTextAppearance = a.getResourceId(
                R.styleable.TkpdTextInputLayout_successTextAppearance,
                android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Caption);
        a.recycle();


        // this is to initialize the indicatorView
        super.setErrorEnabled(true);

        mIndicatorView = (ViewGroup) this.getChildAt(this.getChildCount()-1);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        EditText editText = getEditText();
        if (editText!= null) {
            editText.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (mSuccessView!= null) {
                        mSuccessView.setActivated(hasFocus);
                    }
                }
            });
        }
    }

    public void setSuccessEnabled(boolean enabled) {
        if (mSuccessEnabled != enabled) {
            if (enabled) {
                mSuccessView = new AppCompatTextView(getContext());
                mSuccessView.setTypeface(getEditText()!= null? getEditText().getTypeface() : null);
                boolean useDefaultColor = false;
                try {
                    TextViewCompat.setTextAppearance(mSuccessView, mSuccessTextAppearance);

                    if (Build.VERSION.SDK_INT >= 23
                            && mSuccessView.getTextColors().getDefaultColor() == Color.MAGENTA) {
                        // Caused by our theme not extending from Theme.Design*. On API 23 and
                        // above, unresolved theme attrs result in MAGENTA rather than an exception.
                        // Flag so that we use a decent default
                        useDefaultColor = true;
                    }
                } catch (Exception e) {
                    // Caused by our theme not extending from Theme.Design*. Flag so that we use
                    // a decent default
                    useDefaultColor = true;
                }
                if (useDefaultColor) {
                    // Probably caused by our theme not extending from Theme.Design*. Instead
                    // we manually set something appropriate
                    TextViewCompat.setTextAppearance(mSuccessView,
                            android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Caption);
                }

                int[][] states = new int[][] {
                        new int[] { android.R.attr.state_activated}, // focused
                        new int[] {-android.R.attr.state_activated}, // not focused
                };

                int[] colors = new int[] {
                        mSuccessFocusColor,
                        mSuccessLeaveColor,
                };

                ColorStateList myList = new ColorStateList(states, colors);

                // override the text color with our own
                mSuccessView.setTextColor(myList);

                mSuccessView.setVisibility(INVISIBLE);

                // ensure error view is added next to success view & indicator view is visible
                super.setErrorEnabled(true);
                mIndicatorView.addView(mSuccessView, 0);
            } else {
                mSuccessShown = false;
                mIndicatorView.removeView(mSuccessView);
                setErrorEnabled(false); // to update state
                mSuccessView = null;
            }
            mSuccessEnabled = enabled;
        }
    }

    public void setSuccess(@Nullable final CharSequence successString) {
        boolean isEmptySuccessString = TextUtils.isEmpty(successString);

        mSuccessString = successString;

        if (!mSuccessEnabled) { // success not enabled, enabled true and create it
            if (isEmptySuccessString) {
                // If success isn't enabled, and the success is empty, just return
                return;
            }
            // Else, we'll assume that they want to enable the success functionality
            setSuccessEnabled(true);
        }

        mSuccessShown = !isEmptySuccessString;

        if (mSuccessShown) {
            super.setErrorEnabled(false); // to update state, make it visible gone
            mSuccessView.setText(successString);
            mSuccessView.setActivated(true);
            mSuccessView.setVisibility(VISIBLE);
        } else {
            if (mSuccessView.getVisibility() == VISIBLE) {
                mSuccessView.setText(successString);
                mSuccessView.setVisibility(GONE);
            }
        }
        refreshDrawableState();
    }

    @Override
    public void setError(@Nullable CharSequence error) {
        // if success is shown, hide success to show error
        if (! TextUtils.isEmpty(error) && mSuccessShown) {
            setSuccess(null);
        }
        super.setError(error);
    }

    public void hideErrorSuccess(){
        setError(null);
        setSuccess(null);
    }

    public void setErrorSuccessEnabled(boolean errorSuccessEnabled){
        super.setErrorEnabled(errorSuccessEnabled);
        setSuccessEnabled(errorSuccessEnabled);
    }
}