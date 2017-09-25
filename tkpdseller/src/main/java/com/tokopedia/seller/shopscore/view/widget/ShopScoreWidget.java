package com.tokopedia.seller.shopscore.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.tokopedia.seller.R;
import com.tokopedia.seller.shopscore.view.model.ShopScoreViewModel;
import com.tokopedia.seller.shopscore.view.model.ShopScoreViewModelData;

/**
 * @author sebastianuskh on 2/23/17.
 */

public class ShopScoreWidget extends FrameLayout {
    private ShopScoreMainDetailView shopScoreMainDetailView;
    private ShopScoreWidgetCallback callback;

    public ShopScoreWidget(Context context) {
        super(context);
        initView(context);
    }

    public ShopScoreWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ShopScoreWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @SuppressLint("NewApi")
    public ShopScoreWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (callback != null) {
            callback.getShopScoreData();
        }
    }

    private void initView(Context context, AttributeSet attrs) {
        initView(context);
    }

    private void initView(Context context) {
        View view = inflate(context, R.layout.widget_shop_score, this);
        shopScoreMainDetailView =
                (ShopScoreMainDetailView) view.findViewById(R.id.shop_score_progress_bar_group);
    }

    public void renderView(ShopScoreViewModel shopScoreViewModel) {
        setLimit(shopScoreViewModel.getBadgeScore());
        ShopScoreViewModelData data = shopScoreViewModel.getData();
        setDescription(data.getDescription());
        setProgressBarColor(data.getProgressBarColor());
        setProgress(data.getValue());
    }

    private void setProgressBarColor(int progressBarColor) {
        shopScoreMainDetailView.setProgressBarColor(progressBarColor);
    }

    public void setProgress(float progress) {
        shopScoreMainDetailView.setProgress(progress);
    }

    public void setLimit(float limit) {
        shopScoreMainDetailView.setLimit(limit);
    }

    public void setDescription(String description) {
        shopScoreMainDetailView.setVisibility(VISIBLE);
    }

    public void setCallback(ShopScoreWidgetCallback callback) {
        this.callback = callback;
    }
}
