package com.tokopedia.seller.shopscore.view.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by sebastianuskh on 2/23/17.
 */

public class ShopScoreMainProgressBar extends SquareProgressBar {
    private float limit;
    private ShopScoreMainProgressBarListener parent;

    public ShopScoreMainProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShopScoreMainProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initListener() {
        if (getParent() instanceof ShopScoreMainProgressBarListener) {
            parent = (ShopScoreMainProgressBarListener) getParent();
        } else {
            throw new RuntimeException("implement ShopScoreMainProgressBarListener to its parent");
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initListener();
    }

    public void setLimit(float limit) {
        this.limit = limit;
        parent.updateLimitViewOutside(calculateProgressWidth(limit));
    }

    @Override
    protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);
        parent.updateLimitViewOutside(calculateProgressWidth(limit));
    }

    private int calculateProgressWidth(float progress) {
        float ratio = getMax() / progress;
        return (int) ((getLayoutWidth() - (getPadding() * 2)) / ratio);
    }
}
