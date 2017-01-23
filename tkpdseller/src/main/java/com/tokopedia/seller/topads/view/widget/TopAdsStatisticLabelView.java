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

public class TopAdsStatisticLabelView extends FrameLayout {

    @BindView(R2.id.text_view_title)
    TextView titleTextView;

    @BindView(R2.id.text_view_content)
    TextView contentTextView;

    private String titleText;
    private String contentText;
    private int contentColorValue;

    public TopAdsStatisticLabelView(Context context) {
        super(context);
        init();
    }

    public TopAdsStatisticLabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TopAdsStatisticLabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();

        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.TopAdsLabelView);
        try {
            titleText = styledAttributes.getString(R.styleable.TopAdsLabelView_title);
            contentText = styledAttributes.getString(R.styleable.TopAdsLabelView_content);
            contentColorValue = styledAttributes.getColor(R.styleable.TopAdsLabelView_content_color, ContextCompat.getColor(getContext(), R.color.green_200));
        } finally {
            styledAttributes.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleTextView.setText(titleText);
        contentTextView.setText(contentText);
        contentTextView.setTextColor(contentColorValue);
        invalidate();
        requestLayout();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.custom_view_topads_statistic, null);
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

    public void setContentColorValue(@ColorInt int contentColorValue) {
        contentTextView.setTextColor(contentColorValue);
        invalidate();
        requestLayout();
    }

    public String getTitle() {
        return titleTextView.getText().toString();
    }

    public String getContent() {
        return contentTextView.getText().toString();
    }
}