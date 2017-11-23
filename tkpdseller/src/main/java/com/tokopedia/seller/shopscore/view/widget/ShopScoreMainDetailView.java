package com.tokopedia.seller.shopscore.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.seller.R;

/**
 * @author sebastianuskh on 2/24/17.
 */

public class ShopScoreMainDetailView extends RelativeLayout {

    private TextView shopScoreTextView;
    private ShopScoreMainProgressBarWithLimit progressBar;

    public ShopScoreMainDetailView(Context context) {
        super(context);
        initView(context);
    }

    public ShopScoreMainDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ShopScoreMainDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @SuppressLint("NewApi")
    public ShopScoreMainDetailView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        initView(context);
    }

    private void initView(Context context) {
        View view = inflate(context, R.layout.widget_shop_score_main_detail, this);
        shopScoreTextView = (TextView) view.findViewById(R.id.text_view_shop_score);
        progressBar = (ShopScoreMainProgressBarWithLimit) view.findViewById(R.id.progress_bar_shop_score_with_limit);
    }


    public void setProgress(float progress) {
        progressBar.setProgress(progress);
        shopScoreTextView.setText(shopScoreTextView.getContext().getString(R.string.shop_score_point_value, String.valueOf(Math.round(progress))));
    }

    public void setLimit(float limit) {
        progressBar.setLimit(limit);
    }

    public void setProgressBarColor(int progressBarColor) {
        progressBar.setColor(progressBarColor);
    }
}
