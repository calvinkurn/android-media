package com.tokopedia.seller.shopscore.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tokopedia.seller.R;

/**
 * @author sebastianuskh on 2/23/17.
 */

public class ShopScoreMainProgressBarWithLimit
        extends RelativeLayout
        implements ShopScoreMainProgressBarListener {
    private ImageView icShopScoreLimit;
    private ShopScoreMainProgressBar shopScoreMainProgressBar;
    private View shopScoreViewLimit;

    public ShopScoreMainProgressBarWithLimit(Context context) {
        super(context);
        initView(context);
    }

    public ShopScoreMainProgressBarWithLimit(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ShopScoreMainProgressBarWithLimit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @SuppressLint("NewApi")
    public ShopScoreMainProgressBarWithLimit(
            Context context,
            AttributeSet attrs,
            int defStyleAttr,
            int defStyleRes
    ) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(Context context, AttributeSet attrs) {
        initView(context);
    }

    private void initView(Context context) {
        View view = inflate(context, R.layout.widget_shop_score_progressbar, this);
        icShopScoreLimit = (ImageView) view.findViewById(R.id.ic_shop_score_limit);
        shopScoreMainProgressBar = (ShopScoreMainProgressBar) view.findViewById(R.id.shop_score_progress_bar);
        shopScoreViewLimit = view.findViewById(R.id.shop_score_view_limit);
        shopScoreMainProgressBar.setListener(this);
    }

    public void setProgress(float progress) {
        shopScoreMainProgressBar.setProgress(progress);
    }

    public void setLimit(float limit) {
        shopScoreMainProgressBar.setLimit(limit);
    }

    @Override
    public void updateLimitViewOutside(int progress) {
        setIcShopScoreLimitLocation(progress);
        setShopScoreViewLimitLocation(progress);
    }

    private void setIcShopScoreLimitLocation(final int progressWidth) {
        icShopScoreLimit.post(new Runnable() {
            @Override
            public void run() {
                int halfWidth = icShopScoreLimit.getWidth() / 2;
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) icShopScoreLimit.getLayoutParams();
                params.setMargins(progressWidth - halfWidth, 0, 0, 0);
                icShopScoreLimit.setLayoutParams(params);
            }
        });
    }

    private void setShopScoreViewLimitLocation(int progressWidth) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) shopScoreViewLimit.getLayoutParams();
        params.setMargins(progressWidth, 0, 0, 0);
        shopScoreViewLimit.setLayoutParams(params);
    }

    public void setColor(int progressBarColor) {
        shopScoreMainProgressBar.setProgressColor(progressBarColor);
    }
}
