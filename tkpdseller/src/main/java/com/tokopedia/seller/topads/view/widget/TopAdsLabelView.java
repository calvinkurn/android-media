package com.tokopedia.seller.topads.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.seller.R;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public class TopAdsLabelView extends FrameLayout {

    TextView titleTextView;
    TextView contentTextView;
    ImageView arrow;
    private String titleText;
    private String valueText;
    private int colorValue;
    private int contentTextStyleValue;
    private int titleTextStyleValue;

    public TopAdsLabelView(Context context) {
        super(context);
        init();
    }

    public TopAdsLabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TopAdsLabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.TopAdsLabelView);
        try {
            titleText = styledAttributes.getString(R.styleable.TopAdsLabelView_title);
            valueText = styledAttributes.getString(R.styleable.TopAdsLabelView_content);
            colorValue = styledAttributes.getColor(R.styleable.TopAdsLabelView_content_color, ContextCompat.getColor(getContext(), R.color.grey));
            contentTextStyleValue = styledAttributes.getInt(R.styleable.TopAdsLabelView_content_textStyle, Typeface.NORMAL);
            titleTextStyleValue = styledAttributes.getInt(R.styleable.TopAdsLabelView_title_textStyle, Typeface.NORMAL);
        } finally {
            styledAttributes.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleTextView.setText(titleText);
        contentTextView.setText(valueText);
        contentTextView.setTextColor(colorValue);
        contentTextView.setTypeface(null, contentTextStyleValue);
        titleTextView.setTypeface(null, titleTextStyleValue);
        invalidate();
        requestLayout();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.item_detail_topads_layout, this);
        titleTextView = (TextView) view.findViewById(R.id.title_text_view);
        contentTextView = (TextView) view.findViewById(R.id.content_text_view);
        arrow = (ImageView) view.findViewById(R.id.arrow_left);
    }

    public void setTitle(String textTitle) {
        titleTextView.setText(textTitle);
        invalidate();
        requestLayout();
    }

    public void setContent(String textValue) {
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

    public String getValue() {
        return contentTextView.getText().toString();
    }
}