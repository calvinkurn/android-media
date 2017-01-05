package com.tokopedia.seller.topads.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public class TopAdsLabelView extends CardView {

    @BindView(R2.id.title_item)
    TextView title;

    @BindView(R2.id.value_item)
    TextView value;
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
            valueText = styledAttributes.getString(R.styleable.TopAdsLabelView_value);
            colorValue = styledAttributes.getColor(R.styleable.TopAdsLabelView_color_value, ContextCompat.getColor(getContext(), R.color.grey));

        }finally {
            styledAttributes.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        title.setText(titleText);
        value.setText(valueText);
        value.setTextColor(colorValue);
        invalidate();
        requestLayout();
    }

    private void init(){
        View view = inflate(getContext(), R.layout.item_detail_topads_layout, null);
        ButterKnife.bind(this, view);
        addView(view);
    }

    public void setTitle(String textTitle){
        title.setText(textTitle);
        invalidate();
        requestLayout();
    }

    public void setValue(String textValue){
        value.setText(textValue);
        invalidate();
        requestLayout();
    }

    public void setColorValue(@ColorInt int colorValue){
        value.setTextColor(colorValue);
        invalidate();
        requestLayout();
    }

    public String getTitle(){
        return title.getText().toString();
    }

    public String getValue(){
        return value.getText().toString();
    }
}
