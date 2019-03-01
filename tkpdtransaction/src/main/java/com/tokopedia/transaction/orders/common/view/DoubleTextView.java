package com.tokopedia.transaction.orders.common.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.transaction.R;

public class DoubleTextView extends LinearLayout {
    private LinearLayout layout = null;
    private TextView topTextView = null;
    private TextView bottomTextView = null;
    private LinearLayout llBottomTextView = null;
    private Context mContext = null;

    public DoubleTextView(Context context, int orientation) {
        super(context);
        int layoutId;
        mContext = context;
        String service = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(service);
        if (orientation == VERTICAL) {
            layoutId = R.layout.custom_vertical_text_view_layout;
        } else {
            layoutId = R.layout.custom_horizontal_text_view_layout;
        }
        layout = (LinearLayout) li.inflate(layoutId, this, true);
        topTextView = (TextView) layout.findViewById(R.id.top_text);
        bottomTextView = (TextView) layout.findViewById(R.id.bottom_text);
        llBottomTextView = layout.findViewById(R.id.ll_bottom_text);
    }

    public void setBottonTextViewGravity(int gravity) {
        bottomTextView.setGravity(gravity);
    }

    public DoubleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
    }

    public DoubleTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    @SuppressWarnings("unused")
    public void setOrientation(int orientation) {
        layout.setOrientation(orientation);
    }

    @SuppressWarnings("unused")
    public void setTopText(String text) {
        topTextView.setText(text);
    }

    @SuppressWarnings("unused")
    public void setTopTextSize(float topTextSize) {
        this.topTextView.setTextSize(topTextSize);
    }

    @SuppressWarnings("unused")
    public void setTopTextStyle(String topTextStyle) {
        if (topTextStyle.equalsIgnoreCase("bold"))
            topTextView.setTypeface(Typeface.DEFAULT_BOLD);
        else
            topTextView.setTypeface(Typeface.DEFAULT);
    }

    @SuppressWarnings("unused")
    public void setTopTextColor(int topTextColor) {
        topTextView.setTextColor(topTextColor);
    }


    @SuppressWarnings("unused")
    public void setBottomText(String text) {
        bottomTextView.setText(text);
    }

    public void setBottomText(CharSequence spannableString) {
        bottomTextView.setHighlightColor(Color.TRANSPARENT);
        bottomTextView.setMovementMethod(LinkMovementMethod.getInstance());
        bottomTextView.setText(spannableString, TextView.BufferType.SPANNABLE);
    }

    @SuppressWarnings("unused")
    public void setBottomTextSize(float bottomTextSize) {
        this.bottomTextView.setTextSize(bottomTextSize);
    }

    @SuppressWarnings("unused")
    public void setBottomTextStyle(String bottomTextStyle) {
        if (bottomTextStyle.equalsIgnoreCase("bold"))
            bottomTextView.setTypeface(Typeface.DEFAULT_BOLD);
        else
            bottomTextView.setTypeface(Typeface.DEFAULT);
    }

    public void setBottomTextRightPadding(int left,int top,int right, int bottom) {
        bottomTextView.setPadding(left,top,right,bottom);
    }

    @SuppressWarnings("unused")
    public void setBottomTextColor(int bottomTextColor) {
        this.bottomTextView.setTextColor(bottomTextColor);
    }

    public void setBottomTextBackground(Drawable drawable) {
        this.bottomTextView.setBackground(drawable);
    }

    public void setBottomGravity(int gravity) {
        if(this.llBottomTextView != null) {
            this.llBottomTextView.setGravity(gravity);
        }
    }

    public void setBottomTextBackgroundColor(int s) {
        this.bottomTextView.getBackground().mutate().setTint(s);
    }
}