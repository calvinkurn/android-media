package com.tokopedia.seller.shopscore.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tokopedia.seller.R;

/**
 * Created by sebastianuskh on 2/23/17.
 */

public class ShopScoreMainProgressBarGroup extends LinearLayout {
    private ImageView icShopScoreLimit;
    private ShopScoreMainProgressBar shopScoreMainProgressBar;
    private View shopScoreViewLimit;

    public ShopScoreMainProgressBarGroup(Context context) {
        super(context);
        initView(context);
    }

    public ShopScoreMainProgressBarGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ShopScoreMainProgressBarGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @SuppressLint("NewApi")
    public ShopScoreMainProgressBarGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(Context context, AttributeSet attrs) {
        initView(context);
    }

    private void initView(Context context) {
        View view = inflate(context, R.layout.view_main_shop_score_progressbar, this);
        addView(view);
        icShopScoreLimit = (ImageView) view.findViewById(R.id.ic_shop_score_limit);
        shopScoreMainProgressBar = (ShopScoreMainProgressBar) view.findViewById(R.id.shop_score_progress_bar);
        shopScoreViewLimit = view.findViewById(R.id.shop_score_view_limit);
    }

    public void setLimit(float limit) {
        shopScoreMainProgressBar.setProgress(limit);
        int progressWidth = shopScoreMainProgressBar.getProgressWidth();
        setShopScoreViewLimitLocation(progressWidth);
        setIcShopScoreLimitLocation(progressWidth);
    }

    private void setIcShopScoreLimitLocation(int progressWidth) {
        int halfWidth = icShopScoreLimit.getWidth() / 2;
        icShopScoreLimit.setPadding(
                progressWidth - halfWidth,
                icShopScoreLimit.getPaddingTop(),
                icShopScoreLimit.getPaddingRight(),
                icShopScoreLimit.getPaddingBottom()
        );
    }

    private void setShopScoreViewLimitLocation(int progressWidth) {
        shopScoreViewLimit.setPadding(
                progressWidth,
                shopScoreViewLimit.getPaddingTop(),
                shopScoreViewLimit.getRight(),
                shopScoreViewLimit.getBottom()
        );
    }


}
