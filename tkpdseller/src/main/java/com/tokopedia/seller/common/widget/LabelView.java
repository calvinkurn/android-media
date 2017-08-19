package com.tokopedia.seller.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.seller.R;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public class LabelView extends BaseCustomView {

    private TextView titleTextView;
    private TextView contentTextView;
    private ImageView arrow;

    private String titleText;
    @ColorInt
    private int titleColorValue;
    private int contentTextStyleValue;

    private String contentText;
    @ColorInt
    private int contentColorValue;
    private int titleTextStyleValue;

    private boolean showArrow;
    private int maxLines;
    private float textSize;

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
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.LabelView);
        try {
            titleText = styledAttributes.getString(R.styleable.LabelView_title);
            contentText = styledAttributes.getString(R.styleable.LabelView_content);
            contentColorValue = styledAttributes.getColor(R.styleable.LabelView_content_color, ContextCompat.getColor(getContext(), R.color.grey));
            contentTextStyleValue = styledAttributes.getInt(R.styleable.LabelView_content_textStyle, Typeface.NORMAL);
            titleTextStyleValue = styledAttributes.getInt(R.styleable.LabelView_title_textStyle, Typeface.NORMAL);
            showArrow = styledAttributes.getBoolean(R.styleable.LabelView_label_show_arrow, false);
            maxLines = styledAttributes.getInt(R.styleable.LabelView_content_max_lines, 1);
            textSize = styledAttributes.getDimension(com.tokopedia.design.R.styleable.LabelView_lv_font_size,
                    getResources().getDimension(com.tokopedia.design.R.dimen.font_subheading));
        } finally {
            styledAttributes.recycle();
        }
    }

    private void init() {
        View view = inflate(getContext(), R.layout.item_detail_topads_layout, this);
        titleTextView = (TextView) view.findViewById(R.id.title_text_view);
        contentTextView = (TextView) view.findViewById(R.id.content_text_view);
        arrow = (ImageView) view.findViewById(R.id.arrow_left);
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
        setVisibleArrow(showArrow);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        contentTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        invalidate();
        requestLayout();
    }

    public void resetContentText() {
        setContent(contentText);
        setVisibleArrow(showArrow);
        invalidate();
        requestLayout();
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            contentTextView.setTextColor(contentColorValue);
        }
    }

    public void setContent(CharSequence textValue) {
        contentTextView.setText(textValue);
        invalidate();
        requestLayout();
    }

    public void setTitleContentTypeFace(int typeFace){
        titleTextView.setTypeface(null, typeFace);
        invalidate();
        requestLayout();
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

    public void setVisibleArrow(boolean isVisible){
        if(isVisible){
            arrow.setVisibility(VISIBLE);
        }else{
            arrow.setVisibility(GONE);
        }
    }

    public String getTitle() {
        return titleTextView.getText().toString();
    }

    public void setTitle(String textTitle) {
        titleTextView.setText(textTitle);
        invalidate();
        requestLayout();
    }

    public String getValue() {
        return contentTextView.getText().toString();
    }
}