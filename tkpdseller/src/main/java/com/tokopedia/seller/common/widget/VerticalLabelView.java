package com.tokopedia.seller.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.seller.R;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public class VerticalLabelView extends BaseCustomView {
    private TextView titleTextView;

    private String titleText;
    private TextView summaryTextView;
    private float titleTextSize;
    private int titleTextColor;
    private float contentTextSize;
    private int contentTextColor;

    public VerticalLabelView(Context context) {
        super(context);
        init();
    }

    public VerticalLabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public VerticalLabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.VerticalLabelView);
        try {
            titleText = styledAttributes.getString(R.styleable.VerticalLabelView_vlv_title);
            titleTextSize = styledAttributes.getDimension(R.styleable.VerticalLabelView_vlv_title_text_size,
                    getContext().getResources().getDimension(R.dimen.font_small));
            titleTextColor = styledAttributes.getColor(R.styleable.VerticalLabelView_vlv_title_color,
                    getContext().getResources().getColor(R.color.font_black_secondary_54));
            contentTextSize = styledAttributes.getDimension(R.styleable.VerticalLabelView_vlv_content_text_size,
                    getContext().getResources().getDimension(R.dimen.font_title));
            contentTextColor = styledAttributes.getColor(R.styleable.VerticalLabelView_vlv_content_color,
                    getContext().getResources().getColor(R.color.font_black_primary_70));
        } finally {
            styledAttributes.recycle();
        }
        if (titleTextSize!= 0) {
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        }
        if (titleTextColor!= 0) {
            titleTextView.setTextColor(titleTextColor);
        }

        if (contentTextSize!= 0) {
            summaryTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, contentTextSize);
        }
        if (contentTextColor!= 0) {
            summaryTextView.setTextColor(contentTextColor);
        }
        titleTextView.setText(titleText);
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_vertical_label_view, this);
        titleTextView = (TextView) view.findViewById(R.id.title_text_view);
        summaryTextView = (TextView) view.findViewById(R.id.summary_text_view);
    }

    public void setTitle(String textTitle) {
        titleTextView.setText(textTitle);
    }

    public void setSummary(String summaryText) {
        if (TextUtils.isEmpty(summaryText)) {
            summaryTextView.setVisibility(View.GONE);
        } else {
            summaryTextView.setText(summaryText);
            summaryTextView.setVisibility(View.VISIBLE);
        }
    }

    public String getTitle() {
        return titleTextView.getText().toString();
    }

}
