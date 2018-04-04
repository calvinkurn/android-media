package com.tokopedia.seller.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.seller.R;

/**
 * Created by zulfikarrahman on 12/29/16.
 */
@Deprecated
public class LabelView extends BaseCustomView {

    private TextView titleTextView;
    private TextView contentTextView;

    private String titleText;
    @ColorInt
    private int titleColorValue;
    private int contentTextStyleValue;

    private String contentText;
    @ColorInt
    private int contentColorValue;
    private int titleTextStyleValue;

    private int maxLines;
    private float textSize;
    private int minTitleWidth;

    private boolean enabled;

    public LabelView(Context context) {
        super(context);
        init();
    }

    public LabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.LabelView);
        try {
            titleText = styledAttributes.getString(R.styleable.LabelView_title);
            titleColorValue = styledAttributes.getColor(R.styleable.LabelView_lv_title_color, ContextCompat.getColor(getContext(), R.color.font_black_primary_70));
            contentText = styledAttributes.getString(R.styleable.LabelView_content);
            contentColorValue = styledAttributes.getColor(R.styleable.LabelView_content_color, ContextCompat.getColor(getContext(), R.color.font_black_secondary_54));
            contentTextStyleValue = styledAttributes.getInt(R.styleable.LabelView_content_textStyle, Typeface.NORMAL);
            titleTextStyleValue = styledAttributes.getInt(R.styleable.LabelView_title_textStyle, Typeface.NORMAL);
            maxLines = styledAttributes.getInt(R.styleable.LabelView_content_max_lines, 1);
            textSize = styledAttributes.getDimension(R.styleable.LabelView_lv_font_size,
                    getResources().getDimension(R.dimen.font_title));
            minTitleWidth = styledAttributes.getDimensionPixelSize(R.styleable.LabelView_lv_min_title_width, 0);
        } finally {
            styledAttributes.recycle();
        }
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_label_view_seller, this);
        titleTextView = (TextView) view.findViewById(R.id.title_text_view);
        contentTextView = (TextView) view.findViewById(R.id.content_text_view);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleTextView.setText(titleText);
        setContent(contentText);
        contentTextView.setTextColor(contentColorValue);
        contentTextView.setTypeface(null, contentTextStyleValue);
        contentTextView.setMaxLines(maxLines);
        titleTextView.setTypeface(null, titleTextStyleValue);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        contentTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        titleTextView.setMinWidth(minTitleWidth);
        invalidate();
        requestLayout();
    }

    public void resetContentText() {
        setContent(contentText);
        invalidate();
        requestLayout();
    }

    public boolean isContentDefault(){
        return contentTextView.getText().equals(contentText);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        setClickable(enabled);
        if (enabled) {
            titleTextView.setTextColor(titleColorValue);
            contentTextView.setTextColor(contentColorValue);
        } else {
            titleTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.font_black_disabled_38));
            contentTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.font_black_disabled_38));
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String getTitle() {
        return titleTextView.getText().toString();
    }

    public void setTitle(String textTitle) {
        titleTextView.setText(textTitle);
        invalidate();
        requestLayout();
    }

    public void setTitleContentTypeFace(int typeFace) {
        titleTextView.setTypeface(null, typeFace);
        invalidate();
        requestLayout();
    }

    public void setContent(CharSequence textValue) {
        contentTextView.setText(textValue);
        invalidate();
        requestLayout();
    }

    public String getContent() {
        return contentTextView.getText().toString();
    }

    public void setContentTypeface(int typefaceType) {
        contentTextView.setTypeface(null, typefaceType);
        invalidate();
        requestLayout();
    }

    public void setContentColorValue(@ColorInt int colorValue) {
        contentColorValue = colorValue;
        contentTextView.setTextColor(colorValue);
        invalidate();
        requestLayout();
    }
}