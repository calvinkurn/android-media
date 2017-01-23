package com.tokopedia.seller.topads.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public class TopAdsLabelView extends FrameLayout {

    @BindView(R2.id.title_text_view)
    TextView titleTextView;

    @BindView(R2.id.content_text_view)
    TextView contentTextView;
    private String titleText;
    private String valueText;
    private int colorValue;

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
        invalidate();
        requestLayout();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.item_detail_topads_layout, null);
        ButterKnife.bind(this, view);
        addView(view);
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

    public void setContentColorValue(@ColorInt int colorValue) {
        contentTextView.setTextColor(colorValue);
        invalidate();
        requestLayout();
    }

    public String getTitle() {
        return titleTextView.getText().toString();
    }

    public String getValue() {
        return contentTextView.getText().toString();
    }
}