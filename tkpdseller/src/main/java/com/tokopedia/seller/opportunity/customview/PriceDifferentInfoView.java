package com.tokopedia.seller.opportunity.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.seller.R;

/**
 * Created by normansyahputa on 1/15/18.
 */

public class PriceDifferentInfoView extends BaseCustomView {

    private TextView titleTextView;
    private TextView oldPriceTextView;
    private TextView newPriceTextView;

    private String titleText;

    public PriceDifferentInfoView(@NonNull Context context) {
        super(context);
        init();
    }

    public PriceDifferentInfoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PriceDifferentInfoView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.PriceDifferentInfoView);
        try {
            titleText = styledAttributes.getString(R.styleable.PriceDifferentInfoView_pdiv_title);
        } finally {
            styledAttributes.recycle();
        }
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_opportunity_price_info, this);
        titleTextView = view.findViewById(R.id.title);
        oldPriceTextView = view.findViewById(R.id.text_view_old_price);
        newPriceTextView = view.findViewById(R.id.text_view_new_price);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setTitle(titleText);
        invalidate();
        requestLayout();
    }


    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(title);
        }
    }


    public void setOldPrice(String price) {
        if (!TextUtils.isEmpty(price)) {
            oldPriceTextView.setVisibility(View.VISIBLE);
            oldPriceTextView.setText(price);
        }
    }

    public void setNewPrice(String price) {
        if (!TextUtils.isEmpty(price)) {
            newPriceTextView.setVisibility(View.VISIBLE);
            newPriceTextView.setText(price);
        }
    }
}